package org.openbot.vehicle;

import android.content.Context;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.openbot.env.GameController;
import org.openbot.env.SensorReading;
import org.openbot.utils.Enums;

public class Vehicle {

  private final Noise noise = new Noise(1000, 2000, 5000);
  private boolean noiseEnabled = false;

  private int indicator = 0;
  private int speedMultiplier = 192; // 128,192,255
  private Control control = new Control(0, 0);

  private final SensorReading batteryVoltage = new SensorReading();
  private final SensorReading leftWheelTicks = new SensorReading();
  private final SensorReading rightWheelTicks = new SensorReading();
  private final SensorReading sonarReading = new SensorReading();

  private final int diskHoles = 20;
  private final int millisPerMinute = 60000;

  private final float minBatteryVoltage = 9.6f;
  private final float maxBatteryVoltage = 12.6f;

  private UsbConnection usbConnection;
  protected boolean usbConnected;
  private final Context context;
  private final int baudRate;

  protected Enums.DriveMode driveMode = Enums.DriveMode.GAME;
  private final GameController gameController;
  private Timer heartbeatTimer;

  public Vehicle(Context context, int baudRate) {
    this.context = context;
    this.baudRate = baudRate;
    gameController = new GameController(driveMode);
  }

  public float getBatteryVoltage() {
    return batteryVoltage.getReading();
  }

  public int getBatteryPercentage() {
    return (int)
        ((batteryVoltage.getReading() - minBatteryVoltage)
            * 100
            / (maxBatteryVoltage - minBatteryVoltage));
  }

  public void setBatteryVoltage(float batteryVoltage) {
    this.batteryVoltage.setReading(batteryVoltage);
  }

  public float getLeftWheelTicks() {
    return leftWheelTicks.getReading();
  }

  public float getLeftWheelRPM() {
    if (leftWheelTicks.getAge() > 0 && leftWheelTicks.getReading() != 0)
      return leftWheelTicks.getReading() * millisPerMinute / leftWheelTicks.getAge() / diskHoles;
    else return 0;
  }

  public void setLeftWheelTicks(float leftWheelTicks) {
    this.leftWheelTicks.setReading(leftWheelTicks);
  }

  public float getRightWheelTicks() {
    return rightWheelTicks.getReading();
  }

  public float getRightWheelRPM() {
    if (rightWheelTicks.getAge() > 0 && rightWheelTicks.getReading() != 0)
      return rightWheelTicks.getReading() * millisPerMinute / rightWheelTicks.getAge() / diskHoles;
    else return 0;
  }

  public void setRightWheelTicks(float rightWheelTicks) {
    this.rightWheelTicks.setReading(rightWheelTicks);
  }

  public float getRotation() {
    float rotation = (getLeftSpeed() - getRightSpeed()) * 180 / (getLeftSpeed() + getRightSpeed());
    if (Float.isNaN(rotation) || Float.isInfinite(rotation)) rotation = 0f;
    return rotation;
  }

  public int getSpeedPercent() {
    float throttle = (getLeftSpeed() + getRightSpeed()) / 2;
    return Math.abs((int) (throttle * 100 / 255)); // 255 is the max speed
  }

  public String getDriveGear() {
    float throttle = (getLeftSpeed() + getRightSpeed()) / 2;
    if (throttle > 0) return "D";
    if (throttle < 0) return "R";
    return "P";
  }

  public float getSonarReading() {
    return sonarReading.getReading();
  }

  public void setSonarReading(float sonarReading) {
    this.sonarReading.setReading(sonarReading);
  }

  public Control getControl() {
    return control;
  }

  public void setControl(Control control) {
    this.control = control;
    sendControl();
  }

  public void setControl(float left, float right) {
    this.control = new Control(left, right);
    sendControl();
  }

  private Timer noiseTimer;

  public void toggleNoise() {
    if (noiseEnabled) stopNoise();
    else startNoise();
  }

  public boolean isNoiseEnabled() {
    return noiseEnabled;
  }

  public void setDriveMode(Enums.DriveMode driveMode) {
    this.driveMode = driveMode;
    gameController.setDriveMode(driveMode);
  }

  public Enums.DriveMode getDriveMode() {
    return driveMode;
  }

  public GameController getGameController() {
    return gameController;
  }

  private class NoiseTask extends TimerTask {
    @Override
    public void run() {
      noise.update();
      sendControl();
    }
  }

  public void startNoise() {
    noiseTimer = new Timer();
    NoiseTask noiseTask = new NoiseTask();
    noiseTimer.schedule(noiseTask, 0, 50); // no delay 50ms intervals
    noiseEnabled = true;
  }

  public void stopNoise() {
    noiseEnabled = false;
    noiseTimer.cancel();
    sendControl();
  }

  public int getSpeedMultiplier() {
    return speedMultiplier;
  }

  public void setSpeedMultiplier(int speedMultiplier) {
    this.speedMultiplier = speedMultiplier;
  }

  public int getIndicator() {
    return indicator;
  }

  public void setIndicator(int indicator) {
    this.indicator = indicator;
    sendStringToUsb(String.format(Locale.US, "i%d\n", indicator));
  }

  public UsbConnection getUsbConnection() {
    return usbConnection;
  }

  public void connectUsb() {
    if (usbConnection == null) usbConnection = new UsbConnection(context, baudRate);
    usbConnected = usbConnection.startUsbConnection();
    if (usbConnected) {
      if (heartbeatTimer == null) {
        startHeartbeat();
      }
      setSonarFrequency(100);
      setVoltageFrequency(250);
      setWheelOdometryFrequency(500);
    }
  }

  public void disconnectUsb() {
    if (usbConnection != null) {
      stopBot();
      stopHeartbeat();
      usbConnection.stopUsbConnection();
      usbConnection = null;
      usbConnected = false;
    }
  }

  public boolean isUsbConnected() {
    return usbConnected;
  }

  private void sendStringToUsb(String message) {
    if (usbConnection != null) usbConnection.send(message);
  }

  public float getLeftSpeed() {
    return control.getLeft() * speedMultiplier;
  }

  public float getRightSpeed() {
    return control.getRight() * speedMultiplier;
  }

  public void sendControl() {
    int left = (int) (getLeftSpeed());
    int right = (int) (getRightSpeed());
    if (noiseEnabled && noise.getDirection() < 0)
      left =
          (int)
              ((control.getLeft() - noise.getValue())
                  * speedMultiplier); // since noise value does not have speedMultiplier component,
    // raw control value is used
    if (noiseEnabled && noise.getDirection() > 0)
      right = (int) ((control.getRight() - noise.getValue()) * speedMultiplier);
    sendStringToUsb(String.format(Locale.US, "c%d,%d\n", left, right));
  }

  protected void sendHeartbeat(int timeout_ms) {
    if (usbConnection != null && usbConnection.isOpen() && !usbConnection.isBusy()) {
      usbConnection.send(String.format(Locale.getDefault(), "h%d\n", timeout_ms));
    }
  }

  protected void setSonarFrequency(int interval_ms) {
    if (usbConnection != null && usbConnection.isOpen() && !usbConnection.isBusy()) {
      usbConnection.send(String.format(Locale.getDefault(), "s%d\n", interval_ms));
    }
  }

  protected void setVoltageFrequency(int interval_ms) {
    if (usbConnection != null && usbConnection.isOpen() && !usbConnection.isBusy()) {
      usbConnection.send(String.format(Locale.getDefault(), "v%d\n", interval_ms));
    }
  }

  protected void setWheelOdometryFrequency(int interval_ms) {
    if (usbConnection != null && usbConnection.isOpen() && !usbConnection.isBusy()) {
      usbConnection.send(String.format(Locale.getDefault(), "w%d\n", interval_ms));
    }
  }

  private class HeartBeatTask extends TimerTask {

    @Override
    public void run() {
      sendHeartbeat(750);
    }
  }

  public void startHeartbeat() {
    heartbeatTimer = new Timer();
    HeartBeatTask heartBeatTask = new HeartBeatTask();
    heartbeatTimer.schedule(heartBeatTask, 250, 250); // 250ms delay and 250ms intervals
  }

  public void stopHeartbeat() {
    if (heartbeatTimer != null) {
      heartbeatTimer.cancel();
      heartbeatTimer.purge();
      heartbeatTimer = null;
    }
  }

  public void stopBot() {
    Control control = new Control(0, 0);
    setControl(control);
  }
}
