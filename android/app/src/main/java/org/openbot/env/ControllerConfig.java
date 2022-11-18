package org.openbot.env;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class ControllerConfig {
  private static ControllerConfig _controllerConfig;
  SharedPreferences preferences;

  enum VIDEO_SERVER_TYPE {
    WEBRTC,
    RTSP,
    RTP
  }

  private String currentServerType;

  public static ControllerConfig getInstance() {
    if (_controllerConfig == null) {
      synchronized (PhoneController.class) {
        if (_controllerConfig == null) _controllerConfig = new ControllerConfig();
      }
    }

    return _controllerConfig;
  }

  void init(Context context) {
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    currentServerType = get("video_server", "RTP");
  }

  private void set(String name, String value) {
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(name, value);
    editor.apply();
  }

  private String get(String name, String defaultValue) {
    try {
      return preferences.getString(name, defaultValue);
    } catch (ClassCastException e) {
      return defaultValue;
    }
  }

  private Boolean getBoolean(String name, Boolean defaultValue) {
    try {
      return preferences.getBoolean(name, defaultValue);
    } catch (ClassCastException e) {
      return defaultValue;
    }
  }

  private void setBoolean(String name, boolean value) {
    SharedPreferences.Editor editor = preferences.edit();
    editor.putBoolean(name, value);
    editor.apply();
  }

  public String getVideoServerType() {
    return get("video_server", "RTP");
  }

  public String[] getVideoServerAddress() {
    String ip = get("ip", "172.217.22.14");
    String port_stream = get("port_stream", "8046");
    String port_control = get("port_control", "8040");
    return new String[] {ip, port_stream, port_control};
  }

  public void setVideoServerType(String type) {
    set("video_server", type);
  }
}
