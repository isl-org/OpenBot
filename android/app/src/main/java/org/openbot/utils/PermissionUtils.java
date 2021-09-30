package org.openbot.utils;

import static org.openbot.utils.Constants.PERMISSION_AUDIO;
import static org.openbot.utils.Constants.PERMISSION_CAMERA;
import static org.openbot.utils.Constants.PERMISSION_LOCATION;
import static org.openbot.utils.Constants.PERMISSION_STORAGE;
import static org.openbot.utils.Constants.REQUEST_CAMERA_PERMISSION;
import static org.openbot.utils.Constants.REQUEST_CONTROLLER_PERMISSIONS;
import static org.openbot.utils.Constants.REQUEST_LOGGING_PERMISSIONS;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import org.openbot.R;

public class PermissionUtils {

  public static boolean hasPermission(Context context, String permission) {
    return ContextCompat.checkSelfPermission(context, permission)
        == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean hasPermissions(Context context, String[] permissions) {
    boolean status = true;
    for (String permission : permissions) status = hasPermission(context, permission) && status;
    return status;
  }

  public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
    ActivityCompat.requestPermissions(activity, permissions, requestCode);
  }

  public static boolean hasCameraPermission(Activity activity) {
    return ContextCompat.checkSelfPermission(activity, PERMISSION_CAMERA)
        == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean hasStoragePermission(Activity activity) {
    return ContextCompat.checkSelfPermission(activity, PERMISSION_STORAGE)
        == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean hasLocationPermission(Activity activity) {
    return ContextCompat.checkSelfPermission(activity, PERMISSION_LOCATION)
        == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean hasAudioPermission(Activity activity) {
    return ContextCompat.checkSelfPermission(activity, PERMISSION_AUDIO)
        == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean hasLoggingPermissions(Activity activity) {
    return hasPermissions(
        activity,
        new String[] {
          PERMISSION_CAMERA, PERMISSION_AUDIO, PERMISSION_STORAGE, PERMISSION_LOCATION
        });
  }

  public static boolean hasControllerPermissions(Activity activity) {
    return hasPermissions(
        activity, new String[] {PERMISSION_CAMERA, PERMISSION_AUDIO, PERMISSION_LOCATION});
  }

  public static void requestCameraPermission(Activity activity) {
    ActivityCompat.requestPermissions(
        activity, new String[] {PERMISSION_CAMERA}, REQUEST_CAMERA_PERMISSION);
  }

  public static void requestStoragePermission(Activity activity) {
    requestPermissions(
        activity,
        new String[] {Constants.PERMISSION_STORAGE},
        Constants.REQUEST_STORAGE_PERMISSION);
  }

  public static void requestLocationPermission(Activity activity) {
    requestPermissions(
        activity, new String[] {PERMISSION_LOCATION}, Constants.REQUEST_LOCATION_PERMISSION);
  }

  public static void requestAudioPermission(Activity activity) {
    requestPermissions(
        activity, new String[] {PERMISSION_AUDIO}, Constants.REQUEST_AUDIO_PERMISSION);
  }

  public static void requestLoggingPermissions(Activity activity) {
    requestPermissions(
        activity,
        new String[] {PERMISSION_CAMERA, PERMISSION_AUDIO, PERMISSION_STORAGE, PERMISSION_LOCATION},
        REQUEST_LOGGING_PERMISSIONS);
  }

  public static void requestControllerPermissions(Activity activity) {
    requestPermissions(
        activity,
        new String[] {PERMISSION_CAMERA, PERMISSION_AUDIO, PERMISSION_LOCATION},
        REQUEST_CONTROLLER_PERMISSIONS);
  }

  public static boolean checkControllerPermissions(int[] grantResults) {
    return grantResults.length > 2
        && grantResults[0] == PackageManager.PERMISSION_GRANTED
        && grantResults[1] == PackageManager.PERMISSION_GRANTED
        && grantResults[2] == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean checkLoggingPermissions(int[] grantResults) {
    return grantResults.length > 3
        && grantResults[0] == PackageManager.PERMISSION_GRANTED
        && grantResults[1] == PackageManager.PERMISSION_GRANTED
        && grantResults[2] == PackageManager.PERMISSION_GRANTED
        && grantResults[3] == PackageManager.PERMISSION_GRANTED;
  }

  public static void showControllerPermissionsToast(Activity activity) {

    if (shouldShowRational(activity, Constants.PERMISSION_LOCATION)) {
      showLocationPermissionControllerToast(activity);
    }
    if (shouldShowRational(activity, Constants.PERMISSION_AUDIO)) {
      showAudioPermissionControllerToast(activity);
    }
    if (shouldShowRational(activity, Constants.PERMISSION_CAMERA)) {
      showCameraPermissionControllerToast(activity);
    }
  }

  public static void showLocationPermissionControllerToast(Activity activity) {
    Toast.makeText(
            activity.getApplicationContext(),
            activity.getResources().getString(R.string.location_permission_denied)
                + " "
                + activity.getResources().getString(R.string.permission_reason_find_controller),
            Toast.LENGTH_LONG)
        .show();
  }

  public static void showAudioPermissionControllerToast(Activity activity) {
    Toast.makeText(
            activity.getApplicationContext(),
            activity.getResources().getString(R.string.record_audio_permission_denied)
                + " "
                + activity.getResources().getString(R.string.permission_reason_stream_audio),
            Toast.LENGTH_LONG)
        .show();
  }

  public static void showAudioPermissionLoggingToast(Activity activity) {
    Toast.makeText(
            activity.getApplicationContext(),
            activity.getResources().getString(R.string.record_audio_permission_denied)
                + " "
                + activity.getResources().getString(R.string.permission_reason_save_audio),
            Toast.LENGTH_LONG)
        .show();
  }

  public static void showCameraPermissionControllerToast(Activity activity) {
    Toast.makeText(
            activity.getApplicationContext(),
            activity.getResources().getString(R.string.camera_permission_denied)
                + " "
                + activity.getResources().getString(R.string.permission_reason_stream_video),
            Toast.LENGTH_LONG)
        .show();
  }

  public static void showCameraPermissionsPreviewToast(Activity activity) {
    Toast.makeText(
            activity.getApplicationContext(),
            activity.getResources().getString(R.string.camera_permission_denied)
                + " "
                + activity.getResources().getString(R.string.permission_reason_preview_video),
            Toast.LENGTH_LONG)
        .show();
  }

  public static void showLoggingPermissionsToast(Activity activity) {
    if (shouldShowRational(activity, Constants.PERMISSION_LOCATION)) {
      showLocationPermissionLoggingToast(activity);
    }

    if (shouldShowRational(activity, Constants.PERMISSION_CAMERA)) {
      showCameraPermissionLoggingToast(activity);
    }

    if (shouldShowRational(activity, Constants.PERMISSION_STORAGE)) {
      showStoragePermissionLoggingToast(activity);
    }

    if (shouldShowRational(activity, PERMISSION_AUDIO)) {
      showAudioPermissionLoggingToast(activity);
    }
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

  public static void showPermissionsSettingsToast(Activity activity, String permission) {
    Toast.makeText(
            activity.getApplicationContext(),
            permission
                + " "
                + activity.getResources().getString(R.string.permission_reason_settings),
            Toast.LENGTH_LONG)
        .show();
  }

  public static void showPermissionsModelManagementToast(Activity activity, String permission) {
    Toast.makeText(
            activity.getApplicationContext(),
            permission
                + " "
                + activity.getResources().getString(R.string.permission_reason_model_from_phone),
            Toast.LENGTH_LONG)
        .show();
  }

  public static void showPermissionsLoggingToast(Activity activity, String permission) {
    Toast.makeText(
            activity.getApplicationContext(),
            permission
                + " "
                + activity.getResources().getString(R.string.permission_reason_logging),
            Toast.LENGTH_LONG)
        .show();
  }

  public static void showStoragePermissionSettingsToast(Activity activity) {
    showPermissionsSettingsToast(
        activity, activity.getResources().getString(R.string.storage_permission_denied));
  }

  public static void showCameraPermissionSettingsToast(Activity activity) {
    showPermissionsSettingsToast(
        activity, activity.getResources().getString(R.string.camera_permission_denied));
  }

  public static void showLocationPermissionSettingsToast(Activity activity) {
    showPermissionsSettingsToast(
        activity, activity.getResources().getString(R.string.location_permission_denied));
  }

  public static void showAudioPermissionSettingsToast(Activity activity) {
    showPermissionsSettingsToast(
        activity, activity.getResources().getString(R.string.record_audio_permission_denied));
  }

  public static void showStoragePermissionModelManagementToast(Activity activity) {
    showPermissionsModelManagementToast(
        activity, activity.getResources().getString(R.string.storage_permission_denied));
  }

  public static void showStoragePermissionLoggingToast(Activity activity) {
    showPermissionsLoggingToast(
        activity, activity.getResources().getString(R.string.storage_permission_denied));
  }

  public static void showCameraPermissionLoggingToast(Activity activity) {
    showPermissionsLoggingToast(
        activity, activity.getResources().getString(R.string.camera_permission_denied));
  }

  public static void showLocationPermissionLoggingToast(Activity activity) {
    showPermissionsLoggingToast(
        activity, activity.getResources().getString(R.string.location_permission_denied));
  }
}
