package org.openbot.projects;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.webkit.JavascriptInterface;
import java.util.Objects;
import org.openbot.env.AudioPlayer;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.utils.Enums;
import org.openbot.vehicle.Control;
import org.openbot.vehicle.Vehicle;
import timber.log.Timber;

/** implement openBot functions according to block codes. */
public class BotFunctions implements SensorEventListener {
  private final Vehicle v;
  private final AudioPlayer ap;
  private final SharedPreferencesManager sp;
  private final String TAG = "sensor reading";

  /**
   * get vehicle and audioPlayer in parameters to control openBot commands.
   *
   * @param vehicle
   * @param audioPlayer
   * @param sharedPreferencesManager
   */
  public BotFunctions(
      Vehicle vehicle, AudioPlayer audioPlayer, SharedPreferencesManager sharedPreferencesManager) {
    v = vehicle;
    ap = audioPlayer;
    sp = sharedPreferencesManager;
  }

  /** openBot Movement functions */
  @JavascriptInterface
  public void moveForward(int speed) {
    double speedResult = (double) speed / (double) v.getSpeedMultiplier();
    v.setControl((float) speedResult, (float) speedResult);
  }

  @JavascriptInterface
  public void moveBackward(int speed) {
    double speedResult = (double) speed / (double) v.getSpeedMultiplier();
    v.setControl((float) -speedResult, (float) -speedResult);
  }

  @JavascriptInterface
  public void moveLeft(int speed) {
    double speedResult = (double) speed / (double) v.getSpeedMultiplier();
    v.setControl(0, (float) speedResult);
  }

  @JavascriptInterface
  public void moveRight(int speed) {
    double speedResult = (double) speed / (double) v.getSpeedMultiplier();
    v.setControl((float) speedResult, 0);
  }

  @JavascriptInterface
  public void moveOpenBot(int leftSpeed, int rightSpeed) {
    double leftSpeedResult = (double) leftSpeed / (double) v.getSpeedMultiplier();
    double rightSpeedResult = (double) rightSpeed / (double) v.getSpeedMultiplier();
    v.setControl((float) leftSpeedResult, (float) rightSpeedResult);
  }

  //    @JavascriptInterface
  //    public void openBotmoveCircular(int a) {
  //        Control control = new Control(1F, 1F);
  //        v.setControl(control);
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
    Control control = new Control(0, 0);
    v.setControl(control);
  }

  @JavascriptInterface
  public void stopRobot() {
    v.setControl(0, 0);
  }

  /**
   * openBot Sensor functions
   *
   * @return
   */
  @JavascriptInterface
  public float sonarReading() {
    Timber.tag(TAG).d("sonarReading - %s", v.getSonarReading());
    return v.getSonarReading();
  }

  @JavascriptInterface
  public void speedReading() {
    Timber.tag(TAG).d("Left speed - %s", v.getLeftWheelRpm());
    Timber.tag(TAG).d("Right speed - %s", v.getRightWheelRpm());
  }

  @JavascriptInterface
  public void voltageDividerReading() {
    Timber.tag(TAG).d("Battery Voltage - %s", v.getBatteryVoltage());
    Timber.tag(TAG).d("Battery Percentage - %s", v.getBatteryPercentage());
  }

  @JavascriptInterface
  public void frontWheelReading() {
    Timber.tag(TAG).d("Odometer Front - %s", v.isHasWheelOdometryFront());
  }

  @JavascriptInterface
  public void backWheelReading() {
    Timber.tag(TAG).d("Odometer Back - %s", v.isHasWheelOdometryBack());
  }

  @JavascriptInterface
  public void gyroscopeReading() {}

  @JavascriptInterface
  public void accelerationReading() {}

  @JavascriptInterface
  public void magneticReading() {}

  @JavascriptInterface
  public void indicatorReading() {
    Timber.tag(TAG).d("Indicator - %s", v.isHasIndicators());
  }

  /** service command to robot */
  @JavascriptInterface
  public void noiseEnable(boolean playSound) {
    ap.playNoise("matthew", playSound);
  }

  @JavascriptInterface
  public void playSoundSpeed(String speedMode) {
    if (Objects.equals(speedMode, "slow")) {
      ap.playSpeedMode("matthew", Enums.SpeedMode.SLOW);
    } else if (Objects.equals(speedMode, "medium")) {
      ap.playSpeedMode("matthew", Enums.SpeedMode.NORMAL);
    } else if (Objects.equals(speedMode, "fast")) {
      ap.playSpeedMode("matthew", Enums.SpeedMode.FAST);
    }
  }

  @JavascriptInterface
  public void rightIndicatorOn() {
    v.setIndicator(1);
  }

  @JavascriptInterface
  public void leftIndicatorOn() {
    v.setIndicator(-1);
  }

  @JavascriptInterface
  public void IndicatorOff() {
    v.setIndicator(0);
  }

  @JavascriptInterface
  public void rightIndicatorOff() {
    v.setIndicator(0);
  }

  @JavascriptInterface
  public void leftIndicatorOff() {
    v.setIndicator(0);
  }

  @JavascriptInterface
  public void IndicatorOn() {
    v.setIndicator(1);
    v.setIndicator(-1);
  }

  @JavascriptInterface
  public void ledBrightness(int value) {
    v.sendLightIntensity(value, value);
  }

  @JavascriptInterface
  public void switchController(String controllerMode) {
    if (Objects.equals(controllerMode, "gamepad")) {
      sp.setControlMode(0);
    } else if (Objects.equals(controllerMode, "phone")) {
      sp.setControlMode(1);
    }
  }

  @JavascriptInterface
  public void switchDriveMode(String driveMode) {
    if (Objects.equals(driveMode, "dual")) {
      sp.setDriveMode(0);
    } else if (Objects.equals(driveMode, "game")) {
      sp.setDriveMode(1);
    } else if (Objects.equals(driveMode, "joystick")) {
      sp.setDriveMode(2);
    }
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    switch (event.sensor.getType()) {
      case Sensor.TYPE_ACCELEROMETER:
        // Acceleration including gravity along the X, Y and Z axis
        // Units are m/s^2
        Timber.tag(TAG)
            .d(
                "Acceleration Reading - %s",
                event.timestamp
                    + ","
                    + event.values[0]
                    + ","
                    + event.values[1]
                    + ","
                    + event.values[2]);
        break;
      case Sensor.TYPE_GYROSCOPE:
        // Angular speed around the device's local X, Y and Z axis
        // Units are radians/second
        // The coordinate system is the same as is used by the acceleration sensor
        Timber.tag(TAG)
            .d(
                "Gyroscope Reading - %s",
                event.timestamp
                    + ","
                    + event.values[0]
                    + ","
                    + event.values[1]
                    + ","
                    + event.values[2]);
        break;
      case Sensor.TYPE_MAGNETIC_FIELD:
        // Ambient magnetic field in the X, Y and Z axis in micro-Tesla (uT).
        Timber.tag(TAG)
            .d(
                "Magnetic Reading - %s",
                event.timestamp
                    + ","
                    + event.values[0]
                    + ","
                    + event.values[1]
                    + ","
                    + event.values[2]);
        break;
      default:
        break;
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
