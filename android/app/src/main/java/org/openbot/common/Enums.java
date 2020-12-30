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
    SLOW,
    NORMAL,
    FAST
  }

  public enum DriveMode {
    DUAL,
    GAME,
    JOYSTICK
  }
}
