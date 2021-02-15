package org.openbot.common;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

  public static JSONObject createStatus(String name, Boolean value) {
    return createStatus(name, value ? "true" : "false");
  }

  public static JSONObject createStatus(String name, String value) {
    try {
      return new JSONObject().put("status", new JSONObject().put(name, value));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return new JSONObject();
  }

  public static JSONObject getStatus(
      boolean loggingEnabled,
      boolean noiseEnabled,
      boolean networkEnabled,
      String driveMode,
      int indicator) {
    JSONObject status = new JSONObject();
    try {
      JSONObject statusValue = new JSONObject();

      statusValue.put("LOGS", loggingEnabled);
      statusValue.put("NOISE", noiseEnabled);
      statusValue.put("NETWORK", networkEnabled);
      statusValue.put("DRIVE_MODE", driveMode);

      // Possibly can only send the value of the indicator here, but this makes it clearer.
      // Also, the controller need not have to know implementation details.
      statusValue.put("INDICATOR_LEFT", indicator == -1);
      statusValue.put("INDICATOR_RIGHT", indicator == 1);
      statusValue.put("INDICATOR_STOP", indicator == 0);

      status.put("status", statusValue);

    } catch (JSONException e) {
      e.printStackTrace();
    }
    return status;
  }

  public static boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }
}
