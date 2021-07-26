package org.openbot.env;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.preference.PreferenceManager;
import org.openbot.utils.ConnectionUtils;

public class ControllerConfig {
  private static ControllerConfig _controllerConfig;
  SharedPreferences preferences;

  enum VIDEO_SERVER_TYPE {
    WEBRTC,
    RTSP
  }

  private final boolean isMirrored = true;
  private final boolean mute = true;

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
    currentServerType = get("video_server", "WEBRTC");
    monitorSettingUpdates();
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

  // specific settings
  public boolean isMirrored() {
    return getBoolean("MIRRORED", true);
  }

  public void setMirrored(boolean mirrored) {
    setBoolean("MIRRORED", mirrored);
  }

  public boolean isMute() {
    return getBoolean("MUTE", true);
  }

  public void setMute(boolean mute) {
    setBoolean("MUTE", mute);
  }

  public String getVideoServerType() {
    return get("video_server", "WEBRTC");
  }

  public void setVideoServerType(String type) {
    set("video_server", type);
  }

  private void monitorSettingUpdates() {
    ControllerToBotEventBus.subscribe(
        this.getClass().getSimpleName(),
        event -> {
          String commandType = event.getString("command");

          switch (commandType) {
            case "TOGGLE_MIRROR":
              setMirrored(!isMirrored());

              // inform the controller of current state
              BotToControllerEventBus.emitEvent(
                  ConnectionUtils.createStatus("TOGGLE_MIRROR", isMirrored()));
              break;

            case "TOGGLE_SOUND":
              setMute(!isMute());

              // inform the controller of current state
              BotToControllerEventBus.emitEvent(
                  ConnectionUtils.createStatus("TOGGLE_SOUND", isMute()));
              break;
          }
        },
        error -> {
          Log.d(null, "Error occurred in monitorConnection: " + error);
        },
        event ->
            event.has("command")
                && (event.getString("command").contains("TOGGLE_MIRROR")
                    || event.getString("command").contains("TOGGLE_SOUND")));
  }
}
