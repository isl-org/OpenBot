package org.openbot.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import org.openbot.common.Constants;

public class PermissionUtils {

  public static boolean hasPermission(Context context, String permission) {
    return ContextCompat.checkSelfPermission(context, permission)
        == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean hasPermissions(Context context, String[] permissions) {
    boolean status = false;
    for (String permission : permissions)
      status =
          ContextCompat.checkSelfPermission(context, permission)
              == PackageManager.PERMISSION_GRANTED;

    return status;
  }

  public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
    ActivityCompat.requestPermissions(activity, permissions, requestCode);
  }

  public static void requestCameraPermission(Fragment fragment) {
    requestPermissions(
        fragment, new String[] {Constants.PERMISSION_CAMERA}, Constants.REQUEST_CAMERA_PERMISSION);
  }

  public static void requestLocationPermissionLogging(Fragment fragment) {
    requestPermissions(
        fragment,
        new String[] {Constants.PERMISSION_LOCATION},
        Constants.REQUEST_LOCATION_PERMISSION_LOGGING);
  }

  public static void requestStoragePermission(Fragment fragment) {
    requestPermissions(
        fragment,
        new String[] {Constants.PERMISSION_STORAGE},
        Constants.REQUEST_STORAGE_PERMISSION);
  }

  public static void requestPermissions(Fragment fragment, String[] permissions, int requestCode) {
    fragment.requestPermissions(permissions, requestCode);
  }

  public static boolean shouldShowRational(Activity activity, String permission) {
    return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
  }

  public static boolean shouldAskForPermission(Activity activity, String permission) {
    return !hasPermission(activity, permission)
        && (!hasAskedForPermission(activity, permission)
            || shouldShowRational(activity, permission));
  }

  public static boolean hasAskedForPermission(Activity activity, String permission) {
    return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(permission, false);
  }

  public static void markedPermissionAsAsked(Activity activity, String permission) {
    PreferenceManager.getDefaultSharedPreferences(activity)
        .edit()
        .putBoolean(permission, true)
        .apply();
  }
}
