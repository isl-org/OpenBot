package org.openbot.utils;

import android.Manifest;

public class Constants {

  public static final String USB_ACTION_DATA_RECEIVED = "usb.data_received";

  public static final int REQUEST_CAMERA_PERMISSION = 1;
  public static final int REQUEST_AUDIO_PERMISSION = 2;
  public static final int REQUEST_STORAGE_PERMISSION = 3;
  public static final int REQUEST_LOCATION_PERMISSION = 4;
  public static final int REQUEST_BLUETOOTH_PERMISSION = 5;
  public static final int REQUEST_LOGGING_PERMISSIONS = 6;
  public static final int REQUEST_CONTROLLER_PERMISSIONS = 7;
  public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
  public static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
  public static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
  public static final String PERMISSION_BLUETOOTH = Manifest.permission.BLUETOOTH;
  public static final String PERMISSION_AUDIO = Manifest.permission.RECORD_AUDIO;

  public static final String[] PERMISSIONS_LOGGING =
      new String[] {PERMISSION_CAMERA, PERMISSION_STORAGE, PERMISSION_AUDIO, PERMISSION_LOCATION};
  public static final String[] PERMISSIONS_CONTROLLER =
      new String[] {PERMISSION_CAMERA, PERMISSION_AUDIO, PERMISSION_LOCATION};

  public static final String GENERIC_MOTION_EVENT = "dispatchGenericMotionEvent";
  public static final String KEY_EVENT = "dispatchKeyEvent";
  public static final String DATA = "data";

  public static final String KEY_EVENT_CONTINUOUS = "dispatchKeyEvent_continuous";
  public static final String DATA_CONTINUOUS = "data_continuous";

  // Controller Commands
  public static final String CMD_DRIVE = "DRIVE_CMD";
  public static final String CMD_LOGS = "LOGS";
  public static final String CMD_NOISE = "NOISE";
  public static final String CMD_INDICATOR_LEFT = "INDICATOR_LEFT";
  public static final String CMD_INDICATOR_RIGHT = "INDICATOR_RIGHT";
  public static final String CMD_INDICATOR_STOP = "INDICATOR_STOP";
  public static final String CMD_NETWORK = "NETWORK";
  public static final String CMD_DRIVE_MODE = "DRIVE_MODE";
  public static final String CMD_CONNECTED = "CONNECTED";
  public static final String CMD_DISCONNECTED = "DISCONNECTED";
  public static final String CMD_SPEED_UP = "SPEED_UP";
  public static final String CMD_SPEED_DOWN = "SPEED_DOWN";
  // endregion
}
