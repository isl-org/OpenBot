package org.openbot.projects;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.webkit.JavascriptInterface;
import java.util.Objects;
import org.openbot.databinding.FragmentBlocklyExecutingBinding;
import org.openbot.env.AudioPlayer;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.utils.Enums;
import org.openbot.vehicle.Vehicle;
import timber.log.Timber;

/** implement openBot functions according to block codes. */
public class BotFunctions implements SensorEventListener {
  private final Vehicle vehicle;
  private final AudioPlayer audioPlayer;
  private final SharedPreferencesManager sharedPreferencesManager;
  private final FragmentBlocklyExecutingBinding binding;
  private final Activity mActivity;
  private final Context mContext;
  private final SensorManager sensorManager;
  private final Sensor accelerometerSensor;
  private final Sensor gyroscopeSensor;
  private final Sensor magneticSensor;
  private final float[] gyroscopeValues = new float[3]; // Array to store gyroscope values
  private final float[] accelerometerValues = new float[3]; // Array to store accelerometer values
  private final float[] magneticFieldValues = new float[3]; // Array to store magnetic values

  /**
   * get vehicle and audioPlayer in parameters to control openBot commands.
   *
   */
  public BotFunctions(
      Vehicle getVehicle,
      AudioPlayer getAudioPlayer,
      SharedPreferencesManager getSharedPreferencesManager,
      Context getContext,
      FragmentBlocklyExecutingBinding getBinding,
      Activity getActivity) {
    vehicle = getVehicle;
    audioPlayer = getAudioPlayer;
    sharedPreferencesManager = getSharedPreferencesManager;
    sensorManager = (SensorManager) getContext.getSystemService(Context.SENSOR_SERVICE);
    accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    binding = getBinding;
    mActivity = getActivity;
    mContext = getContext;
  }

  /** openBot Movement functions */
  @JavascriptInterface
  public void moveForward(int speed) {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Move Forward at " + speed));
    double speedResult = (double) speed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl((float) speedResult, (float) speedResult);
  }

  @JavascriptInterface
  public void moveBackward(int speed) {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Move Backward at " + speed));
    double speedResult = (double) speed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl((float) -speedResult, (float) -speedResult);
  }

  @JavascriptInterface
  public void moveLeft(int speed) {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Move Left at + " + speed));
    double speedResult = (double) speed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl(0, (float) speedResult);
  }

  @JavascriptInterface
  public void moveRight(int speed) {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Move Right at + " + speed));
    double speedResult = (double) speed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl((float) speedResult, 0);
  }

  @JavascriptInterface
  public void moveOpenBot(int leftSpeed, int rightSpeed) {
    mActivity.runOnUiThread(
        () ->
            binding.jsCommand.setText(
                "Move Left at " + leftSpeed + " Move Right at " + rightSpeed));
    double leftSpeedResult = (double) leftSpeed / (double) vehicle.getSpeedMultiplier();
    double rightSpeedResult = (double) rightSpeed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl((float) leftSpeedResult, (float) rightSpeedResult);
  }

  //    @JavascriptInterface
  //    public void openBotmoveCircular(int a) {
  //        mActivity.runOnUiThread(() -> binding.jsCommand.setText("Move Circular"));
  //        Control control = new Control(1F, 1F);
  //        vehicle.setControl(control);
  //    }

  @JavascriptInterface
  public void pause(int ms) {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Wait for " + ms + "ms"));
    try {
      synchronized (this) {
        wait(ms); // Pauses the thread for 5 seconds
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @JavascriptInterface
  public void stopRobot() {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Stop Car Immediately"));
    BlocklyExecutingFragment.isFollow = false;
    vehicle.stopBot();
  }

  /**
   * openBot Sensor functions
   *
   */
  @JavascriptInterface
  public float sonarReading() {
    return vehicle.getSonarReading();
  }

  @JavascriptInterface
  public float speedReading() {
    return (vehicle.getLeftWheelRpm() + vehicle.getRightWheelRpm()) / 2;
  }

  @JavascriptInterface
  public float voltageDividerReading() {
    return vehicle.getBatteryVoltage();
  }

  @JavascriptInterface
  public boolean frontWheelReading() {
    return vehicle.isHasWheelOdometryFront();
  }

  @JavascriptInterface
  public boolean backWheelReading() {
    return vehicle.isHasWheelOdometryBack();
  }

  @JavascriptInterface
  public float gyroscopeReadingX() {
    sensorManager.registerListener(
        this, gyroscopeSensor, sharedPreferencesManager.getDelay() * 1000);
    return gyroscopeValues[0];
  }

  @JavascriptInterface
  public float gyroscopeReadingY() {
    sensorManager.registerListener(
        this, gyroscopeSensor, sharedPreferencesManager.getDelay() * 1000);
    return gyroscopeValues[1];
  }

  @JavascriptInterface
  public float gyroscopeReadingZ() {
    sensorManager.registerListener(
        this, gyroscopeSensor, sharedPreferencesManager.getDelay() * 1000);
    return gyroscopeValues[2];
  }

  @JavascriptInterface
  public float accelerationReadingX() {
    sensorManager.registerListener(
        this, accelerometerSensor, sharedPreferencesManager.getDelay() * 1000);
    return accelerometerValues[0];
  }

  @JavascriptInterface
  public float accelerationReadingY() {
    sensorManager.registerListener(
        this, accelerometerSensor, sharedPreferencesManager.getDelay() * 1000);
    return accelerometerValues[1];
  }

  @JavascriptInterface
  public float accelerationReadingZ() {
    sensorManager.registerListener(
        this, accelerometerSensor, sharedPreferencesManager.getDelay() * 1000);
    return accelerometerValues[2];
  }

  @JavascriptInterface
  public float magneticReadingX() {
    sensorManager.registerListener(
        this, magneticSensor, sharedPreferencesManager.getDelay() * 1000);
    return magneticFieldValues[0];
  }

  @JavascriptInterface
  public float magneticReadingY() {
    sensorManager.registerListener(
        this, magneticSensor, sharedPreferencesManager.getDelay() * 1000);
    return magneticFieldValues[1];
  }

  @JavascriptInterface
  public float magneticReadingZ() {
    sensorManager.registerListener(
        this, magneticSensor, sharedPreferencesManager.getDelay() * 1000);
    return magneticFieldValues[2];
  }

  @JavascriptInterface
  public void indicatorReading() {
    //    Timber.tag(TAG).d("Indicator - %s", vehicle.isHasIndicators());
  }

  /** service command to robot */
  @JavascriptInterface
  public void noiseEnable(boolean value) {
    audioPlayer.playNoise("matthew", value);
  }

  @JavascriptInterface
  public void playSoundSpeed(String speedMode) {
    if (Objects.equals(speedMode, "slow")) {
      mActivity.runOnUiThread(
          () -> binding.jsCommand.setText("Play Sound " + speedMode + " Speed"));
      audioPlayer.playSpeedMode("matthew", Enums.SpeedMode.SLOW);
    } else if (Objects.equals(speedMode, "medium")) {
      mActivity.runOnUiThread(
          () -> binding.jsCommand.setText("Play Sound " + speedMode + " Speed"));
      audioPlayer.playSpeedMode("matthew", Enums.SpeedMode.NORMAL);
    } else if (Objects.equals(speedMode, "fast")) {
      mActivity.runOnUiThread(
          () -> binding.jsCommand.setText("Play Sound " + speedMode + " Speed"));
      audioPlayer.playSpeedMode("matthew", Enums.SpeedMode.FAST);
    }
  }

  @JavascriptInterface
  public void playSoundMode(String driveMode) {
    if (Objects.equals(driveMode, "dual drive")) {
      mActivity.runOnUiThread(() -> binding.jsCommand.setText("Play Sound " + driveMode + " Mode"));
      audioPlayer.playDriveMode("matthew", Enums.DriveMode.DUAL);
    } else if (Objects.equals(driveMode, "joystick control")) {
      mActivity.runOnUiThread(() -> binding.jsCommand.setText("Play Sound " + driveMode + " Mode"));
      audioPlayer.playDriveMode("matthew", Enums.DriveMode.JOYSTICK);
    } else if (Objects.equals(driveMode, "gamepad")) {
      mActivity.runOnUiThread(() -> binding.jsCommand.setText("Play Sound " + driveMode + " Mode"));
      audioPlayer.playDriveMode("matthew", Enums.DriveMode.GAME);
    }
  }

  @JavascriptInterface
  public void rightIndicatorOn() {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Right Indicator On"));
    vehicle.setIndicator(1);
  }

  @JavascriptInterface
  public void leftIndicatorOn() {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Left Indicator On"));
    vehicle.setIndicator(-1);
  }

  @JavascriptInterface
  public void IndicatorOff() {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Indicator Off"));
    vehicle.setIndicator(0);
  }

  @JavascriptInterface
  public void rightIndicatorOff() {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Right Indicator Off"));
    vehicle.setIndicator(0);
  }

  @JavascriptInterface
  public void leftIndicatorOff() {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Left Indicator Off"));
    vehicle.setIndicator(0);
  }

  @JavascriptInterface
  public void IndicatorOn() {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Indicator On"));
    vehicle.setIndicator(1);
    vehicle.setIndicator(-1);
  }

  @JavascriptInterface
  public void ledBrightness(int value) {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Led Brightness " + value));
    vehicle.sendLightIntensity(value, value);
  }

  @JavascriptInterface
  public void toggleLed(String value) {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Toggle Led " + value));
    if (Objects.equals(value, "ON")) {
      vehicle.sendLightIntensity(100, 100);
    } else if (Objects.equals(value, "OFF")) {
      vehicle.sendLightIntensity(0, 0);
    }
  }

  @JavascriptInterface
  public void switchController(String controllerMode) {
    if (Objects.equals(controllerMode, "gamepad")) {
      mActivity.runOnUiThread(
          () -> binding.jsCommand.setText("Switch Controller to " + controllerMode));
      sharedPreferencesManager.setControlMode(0);
    } else if (Objects.equals(controllerMode, "phone")) {
      mActivity.runOnUiThread(
          () -> binding.jsCommand.setText("Switch Controller to " + controllerMode));
      sharedPreferencesManager.setControlMode(1);
    }
  }

  @JavascriptInterface
  public void switchDriveMode(String driveMode) {
    if (Objects.equals(driveMode, "dual")) {
      mActivity.runOnUiThread(() -> binding.jsCommand.setText("Switch Drive Mode to " + driveMode));
      sharedPreferencesManager.setDriveMode(0);
    } else if (Objects.equals(driveMode, "game")) {
      mActivity.runOnUiThread(() -> binding.jsCommand.setText("Switch Drive Mode to " + driveMode));
      sharedPreferencesManager.setDriveMode(1);
    } else if (Objects.equals(driveMode, "joystick")) {
      mActivity.runOnUiThread(() -> binding.jsCommand.setText("Switch Drive Mode to " + driveMode));
      sharedPreferencesManager.setDriveMode(2);
    }
  }

  @JavascriptInterface
  public void setSpeed(String speed) {
    if (Objects.equals(speed, "slow")) {
      mActivity.runOnUiThread(() -> binding.jsCommand.setText("Set speed to " + speed));
      sharedPreferencesManager.setSpeedMode(128);
    } else if (Objects.equals(speed, "medium")) {
      mActivity.runOnUiThread(() -> binding.jsCommand.setText("Set speed to " + speed));
      sharedPreferencesManager.setDriveMode(192);
    } else if (Objects.equals(speed, "fast")) {
      mActivity.runOnUiThread(() -> binding.jsCommand.setText("Set speed to " + speed));
      sharedPreferencesManager.setDriveMode(255);
    }
  }

  @JavascriptInterface
  public void navigationModel(String AIModel) {
    Timber.tag("Ai Blocks").i(AIModel);
  }

  @JavascriptInterface
  public void reachGoal(int leftSpeed, int rightSpeed) {
    Timber.tag("Ai Blocks").i(leftSpeed + ", " + rightSpeed);
  }

  @JavascriptInterface
  public void follow(String object, String modelName) {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("Follow " + object + " using " + modelName));
    BlocklyExecutingFragment.modelName = modelName;
    BlocklyExecutingFragment.classType = object;
    BlocklyExecutingFragment.isFollow = true;
  }

  @JavascriptInterface
  public void enableAutopilot(String modelName) {
    mActivity.runOnUiThread(() -> binding.jsCommand.setText("startAutopilot using " + modelName));
    BlocklyExecutingFragment.modelName = modelName;
    BlocklyExecutingFragment.isAutopilot = true;
  }

  @JavascriptInterface
  public void reachPosition(int x, int y) {
    Timber.tag("Ai Blocks").i(x + ", " + y);
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    switch (event.sensor.getType()) {
      case Sensor.TYPE_ACCELEROMETER:
        // Acceleration including gravity along the X, Y and Z axis
        // Units are m/s^2
        accelerometerValues[0] = event.values[0]; // Accelerometer X-axis value
        accelerometerValues[1] = event.values[1]; // Accelerometer Y-axis value
        accelerometerValues[2] = event.values[2]; // Accelerometer Z-axis value
        break;
      case Sensor.TYPE_GYROSCOPE:
        // Angular speed around the device's local X, Y and Z axis
        // Units are radians/second
        // The coordinate system is the same as is used by the acceleration sensor
        gyroscopeValues[0] = event.values[0]; // Gyroscope X-axis value
        gyroscopeValues[1] = event.values[1]; // Gyroscope Y-axis value
        gyroscopeValues[2] = event.values[2]; // Gyroscope Z-axis value
        break;
      case Sensor.TYPE_MAGNETIC_FIELD:
        // Ambient magnetic field in the X, Y and Z axis in micro-Tesla (uT).
        magneticFieldValues[0] = event.values[0]; // magneticField X-axis value
        magneticFieldValues[1] = event.values[1]; // magneticField Y-axis value
        magneticFieldValues[2] = event.values[2]; // magneticField Z-axis value
        break;
      default:
        break;
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
