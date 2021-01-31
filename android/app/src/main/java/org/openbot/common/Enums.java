package org.openbot.common;

public class Enums {
  public enum LogMode {
    ALL_IMGS(0),
    CROP_IMG(1),
    PREVIEW_IMG(2),
    ONLY_SENSORS(3);

    private final int value;

    LogMode(final int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public enum ControlMode {
    GAMEPAD(0),
    PHONE(1),
    WEBRTC(2);

    private final int value;

    ControlMode(final int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public enum SpeedMode {
    SLOW(128),
    NORMAL(192),
    FAST(255);

    private final int value;

    SpeedMode(final int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public enum DriveMode {
    DUAL(0),
    GAME(1),
    JOYSTICK(2);

    private final int value;

    DriveMode(final int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public enum VehicleIndicator {
    LEFT(-1),
    STOP(0),
    RIGHT(1);

    private final int value;

    VehicleIndicator(final int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }
}
