package org.openbot.utils;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

  public static JSONObject createStatusBulk(List<Pair<String, String>> nameValues) {
    JSONObject status = new JSONObject();
    try {
      JSONObject statusValue = new JSONObject();

      for (Pair nameValue : nameValues) {
        statusValue.put(String.valueOf(nameValue.first), nameValue.second);
      }

      status.put("status", statusValue);

    } catch (JSONException e) {
      e.printStackTrace();
    }
    return status;
  }

  public static String getIPAddress(boolean useIPv4) {
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        ArrayList<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            String sAddr = addr.getHostAddress();
            // boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
            boolean isIPv4 = sAddr.indexOf(':') < 0;

            if (useIPv4) {
              if (isIPv4) return sAddr;
            } else {
              if (!isIPv4) {
                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
              }
            }
          }
        }
      }
    } catch (Exception ignored) {
    } // for now eat exceptions
    return "";
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

  public static void copyFile(InputStream inputFile, String name, String outputPath) {

    InputStream in = null;
    OutputStream out = null;
    try {

      // create output directory if it doesn't exist
      File dir = new File(outputPath);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      in = inputFile;
      out = new FileOutputStream(outputPath + '/' + name);

      byte[] buffer = new byte[1024];
      int read;
      while ((read = in.read(buffer)) != -1) {
        out.write(buffer, 0, read);
      }
      in.close();
      in = null;

      // write the output file (You have now copied the file)
      out.flush();
      out.close();
      out = null;

    } catch (FileNotFoundException fnfe1) {
      Log.e("tag", fnfe1.getMessage());
    } catch (Exception e) {
      Log.e("tag", e.getMessage());
    }
  }

  public static boolean checkFileExistence(Activity activity, String name) {
    boolean found = false;
    for (String s : Objects.requireNonNull(activity.getFilesDir().list()))
      if (s.equals(name)) {
        found = true;
        break;
      }
    return found;
  }
}
