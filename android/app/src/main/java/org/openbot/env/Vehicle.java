package org.openbot.env;

import android.os.SystemClock;
import java.util.Random;

public class Vehicle {

  private Control control = new Control(0, 0);
  private Control noisyControl = new Control(0, 0);
  private final Noise noise = new Noise(1000, 2000, 5000);
  private int indicator = 0;
  private static int speedMultiplier = 192; // 128,192,255

  private final SensorReading batteryVoltage = new SensorReading();
  private final SensorReading leftWheelTicks = new SensorReading();
  private final SensorReading rightWheelTicks = new SensorReading();
  private final SensorReading sonarReading = new SensorReading();

  private static final int diskHoles = 20;
  private static final int millisPerMinute = 60000;

  public static class Control {
    private float left = 0;
    private float right = 0;

    public Control(float left, float right) {
      this.left = Math.max(-1.f, Math.min(1.f, left));
      this.right = Math.max(-1.f, Math.min(1.f, right));
    }

    public float getLeft() {
      return left * speedMultiplier;
    }

    public float getRight() {
      return right * speedMultiplier;
    }
  }

  private static class Noise {

    public Noise(int minDuration, int maxDuration, long timeout) {
      this.minDuration = minDuration;
      this.maxDuration = maxDuration;
      this.timeout = timeout;
    }

    int minDuration;
    int maxDuration;
    long timeout;
    float value = 0;
    int direction = 0;
    long duration = 0;
    long startTime = 0;

    public void update() {
      long currentTime = SystemClock.elapsedRealtime();
      if (currentTime > startTime + duration + timeout) {
        startTime = currentTime;
        duration = generateRandomInt(minDuration, maxDuration);
        value = 0;
        Random r = new Random();
        direction = r.nextBoolean() ? 1 : -1;
      }
      if (currentTime < startTime + duration) {
        if (currentTime < startTime + duration / 2) {
          value += (float) generateRandomInt(1, 8) / 255;
        } else {
          value -= (float) generateRandomInt(1, 8) / 255;
        }
        value = Math.max(0, Math.min(value, 0.5f));
      } else value = 0;
    }

    private int generateRandomInt(int min, int max) {
      Random r = new Random();
      return r.nextInt((max - min) + 1) + min;
    }

    private float generateRandomFloat() {
      Random r = new Random();
      return r.nextFloat();
    }
  }

  private static class SensorReading {

    public SensorReading() {
      this.age = 1000;
      this.timestamp = 0;
      this.reading = 0;
    }

    public long getAge() {
      return age;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public float getReading() {
      return reading;
    }

    public void setReading(float reading) {
      long currentTime = SystemClock.elapsedRealtime();
      if (currentTime > timestamp + 5 && timestamp > 0) this.age = currentTime - this.timestamp;
      this.timestamp = currentTime;
      this.reading = reading;
    }

    private long age;
    private long timestamp;
    private float reading;
  }

  public float getBatteryVoltage() {
    return batteryVoltage.getReading();
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

  public float getSonarReading() {
    return sonarReading.getReading();
  }

  public void setSonarReading(float sonarReading) {
    this.sonarReading.setReading(sonarReading);
  }

  public Control getNoisyControl() {
    return noisyControl;
  }

  public Control getControl() {
    return control;
  }

  public void setControl(Control control) {
    this.control = control;
  }

  public void setControl(float left, float right) {
    this.control = new Control(left, right);
  }

  public void applyNoise() {
    noise.update();
    if (noise.direction < 0) noisyControl = new Control(control.left - noise.value, control.right);
    else noisyControl = new Control(control.left, control.right - noise.value);
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
  }
}
