package org.openbot.common;

public class Enums {
  public enum LogMode {
    ALL_IMGS,
    CROP_IMG,
    PREVIEW_IMG,
    ONLY_SENSORS
  }

  public enum ControlMode {
    GAMEPAD,
    PHONE,
    WEBRTC
  }

  public enum SpeedMode {
    SLOW(128),
    NORMAL(192),
    FAST(255);

    private final int value;

    SpeedMode(final int newValue) {
      value = newValue;
    }

    public int getValue() {
      return value;
    }
  }

  public enum DriveMode {
    DUAL,
    GAME,
    JOYSTICK
  }

  public enum VehicleIndicator {
    LEFT(-1),
    STOP(0),
    RIGHT(1);

    private final int value;

    VehicleIndicator(final int newValue) {
      value = newValue;
    }

    public int getValue() {
      return value;
    }
  }
}
