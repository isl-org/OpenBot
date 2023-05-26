package org.openbot.projects;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.webkit.JavascriptInterface;
import java.util.Objects;
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
   * @param getVehicle
   * @param getAudioPlayer
   * @param getSharedPreferencesManager
   */
  public BotFunctions(
      Vehicle getVehicle,
      AudioPlayer getAudioPlayer,
      SharedPreferencesManager getSharedPreferencesManager,
      Context mContext) {
    vehicle = getVehicle;
    audioPlayer = getAudioPlayer;
    sharedPreferencesManager = getSharedPreferencesManager;
    sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
  }

  /** openBot Movement functions */
  @JavascriptInterface
  public void moveForward(int speed) {
    double speedResult = (double) speed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl((float) speedResult, (float) speedResult);
  }

  @JavascriptInterface
  public void moveBackward(int speed) {
    double speedResult = (double) speed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl((float) -speedResult, (float) -speedResult);
  }

  @JavascriptInterface
  public void moveLeft(int speed) {
    double speedResult = (double) speed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl(0, (float) speedResult);
  }

  @JavascriptInterface
  public void moveRight(int speed) {
    double speedResult = (double) speed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl((float) speedResult, 0);
  }

  @JavascriptInterface
  public void moveOpenBot(int leftSpeed, int rightSpeed) {
    double leftSpeedResult = (double) leftSpeed / (double) vehicle.getSpeedMultiplier();
    double rightSpeedResult = (double) rightSpeed / (double) vehicle.getSpeedMultiplier();
    vehicle.setControl((float) leftSpeedResult, (float) rightSpeedResult);
  }

  //    @JavascriptInterface
  //    public void openBotmoveCircular(int a) {
  //        Control control = new Control(1F, 1F);
  //        vehicle.setControl(control);
  //    }

  @JavascriptInterface
  public void pause(int ms) {
    try {
      synchronized (this) {
        wait(ms); // Pauses the thread for 5 seconds
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    //    Control control = new Control(0, 0);
    //    vehicle.setControl(control);
  }

  @JavascriptInterface
  public void stopRobot() {
    vehicle.setControl(0, 0);
  }

  /**
   * openBot Sensor functions
   *
   * @return
   */
  @JavascriptInterface
  public float sonarReading() {
    return vehicle.getSonarReading();
  }

  @JavascriptInterface
  public float speedReading() {
    float speedReading = (vehicle.getLeftWheelRpm() + vehicle.getRightWheelRpm()) / 2;
    return speedReading;
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
  public void noiseEnable(boolean playSound) {
    audioPlayer.playNoise("matthew", playSound);
  }

  @JavascriptInterface
  public void playSoundSpeed(String speedMode) {
    if (Objects.equals(speedMode, "slow")) {
      audioPlayer.playSpeedMode("matthew", Enums.SpeedMode.SLOW);
    } else if (Objects.equals(speedMode, "medium")) {
      audioPlayer.playSpeedMode("matthew", Enums.SpeedMode.NORMAL);
    } else if (Objects.equals(speedMode, "fast")) {
      audioPlayer.playSpeedMode("matthew", Enums.SpeedMode.FAST);
    }
  }

  @JavascriptInterface
  public void playSoundMode(String driveMode) {
    if (Objects.equals(driveMode, "dual drive")) {
      audioPlayer.playDriveMode("matthew", Enums.DriveMode.DUAL);
    } else if (Objects.equals(driveMode, "joystick control")) {
      audioPlayer.playDriveMode("matthew", Enums.DriveMode.JOYSTICK);
    } else if (Objects.equals(driveMode, "gamepad")) {
      audioPlayer.playDriveMode("matthew", Enums.DriveMode.GAME);
    }
  }

  @JavascriptInterface
  public void rightIndicatorOn() {
    vehicle.setIndicator(1);
  }

  @JavascriptInterface
  public void leftIndicatorOn() {
    vehicle.setIndicator(-1);
  }

  @JavascriptInterface
  public void IndicatorOff() {
    vehicle.setIndicator(0);
  }

  @JavascriptInterface
  public void rightIndicatorOff() {
    vehicle.setIndicator(0);
  }

  @JavascriptInterface
  public void leftIndicatorOff() {
    vehicle.setIndicator(0);
  }

  @JavascriptInterface
  public void IndicatorOn() {
    vehicle.setIndicator(1);
    vehicle.setIndicator(-1);
  }

  @JavascriptInterface
  public void ledBrightness(int value) {
    vehicle.sendLightIntensity(value, value);
  }

  @JavascriptInterface
  public void switchController(String controllerMode) {
    if (Objects.equals(controllerMode, "gamepad")) {
      sharedPreferencesManager.setControlMode(0);
    } else if (Objects.equals(controllerMode, "phone")) {
      sharedPreferencesManager.setControlMode(1);
    }
  }

  @JavascriptInterface
  public void switchDriveMode(String driveMode) {
    if (Objects.equals(driveMode, "dual")) {
      sharedPreferencesManager.setDriveMode(0);
    } else if (Objects.equals(driveMode, "game")) {
      sharedPreferencesManager.setDriveMode(1);
    } else if (Objects.equals(driveMode, "joystick")) {
      sharedPreferencesManager.setDriveMode(2);
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
  public void follow(String object) {
    Timber.tag("Ai Blocks").i(object);
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
