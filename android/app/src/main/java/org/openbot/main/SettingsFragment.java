package org.openbot.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import org.openbot.R;
import org.openbot.env.Vehicle;
import org.openbot.utils.Constants;
import org.openbot.utils.PermissionUtils;
import timber.log.Timber;

public class SettingsFragment extends PreferenceFragmentCompat {
  private MainViewModel mViewModel;
  private SwitchPreferenceCompat connection;
  private Vehicle vehicle;
  private SwitchPreferenceCompat camera;
  private SwitchPreferenceCompat storage;
  private SwitchPreferenceCompat location;
  private SwitchPreferenceCompat mic;

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.root_preferences, rootKey);

    mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    vehicle = mViewModel.getVehicle().getValue();

    connection = findPreference("connection");
    if (connection != null) {
      connection.setTitle("No Device");
      if (vehicle != null && vehicle.isUsbConnected()) {
        connection.setChecked(true);
        connection.setTitle(vehicle.getUsbConnection().getProductName());
      } else {
        connection.setTitle("No Device");
        connection.setChecked(false);
      }
      connection.setOnPreferenceClickListener(
          preference -> {
            Timber.d(String.valueOf(connection.isChecked()));
            if (vehicle != null) {
              if (connection.isChecked()) {
                vehicle.connectUsb();
                if (vehicle.isUsbConnected())
                  connection.setTitle(vehicle.getUsbConnection().getProductName());
                else {
                  connection.setTitle("No Device");
                  connection.setChecked(false);
                  Toast.makeText(
                          requireContext().getApplicationContext(),
                          "Please check the USB connection.",
                          Toast.LENGTH_SHORT)
                      .show();
                }
              } else {
                vehicle.disconnectUsb();
                connection.setTitle("No Device");
              }
              mViewModel.setUsbStatus(vehicle.isUsbConnected());
            }
            return true;
          });
    }

    camera = findPreference("camera");
    if (camera != null) {
      camera.setChecked(PermissionUtils.hasCameraPermission(requireActivity()));
      camera.setOnPreferenceChangeListener(
          (preference, newValue) -> {
            if (camera.isChecked()) startInstalledAppDetailsActivity(requireActivity());
            else {
              if (PermissionUtils.shouldShowRational(
                  requireActivity(), Manifest.permission.CAMERA)) {
                Toast.makeText(
                        requireActivity().getApplicationContext(),
                        getResources().getString(R.string.camera_permission_denied)
                            + " "
                            + getResources().getString(R.string.permission_reason_settings),
                        Toast.LENGTH_LONG)
                    .show();
                startInstalledAppDetailsActivity(requireActivity());
              } else PermissionUtils.requestCameraPermission(requireActivity());
            }

            return false;
          });
    }

    storage = findPreference("storage");
    if (storage != null) {
      storage.setChecked(PermissionUtils.hasStoragePermission(requireActivity()));
      storage.setOnPreferenceChangeListener(
          (preference, newValue) -> {
            if (storage.isChecked()) startInstalledAppDetailsActivity(requireActivity());
            else {
              if (PermissionUtils.shouldShowRational(
                  requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                startInstalledAppDetailsActivity(requireActivity());
                Toast.makeText(
                        requireContext().getApplicationContext(),
                        getResources().getString(R.string.storage_permission_denied)
                            + " "
                            + getResources().getString(R.string.permission_reason_settings),
                        Toast.LENGTH_LONG)
                    .show();
              } else PermissionUtils.requestStoragePermission(requireActivity());
            }

            return false;
          });
    }

    location = findPreference("location");
    if (location != null) {
      location.setChecked(PermissionUtils.hasLocationPermission(requireActivity()));
      location.setOnPreferenceChangeListener(
          (preference, newValue) -> {
            if (location.isChecked()) startInstalledAppDetailsActivity(requireActivity());
            else {
              if (PermissionUtils.shouldShowRational(
                  requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(
                        requireActivity().getApplicationContext(),
                        getResources().getString(R.string.location_permission_denied)
                            + " "
                            + getResources().getString(R.string.permission_reason_settings),
                        Toast.LENGTH_LONG)
                    .show();
                startInstalledAppDetailsActivity(requireActivity());
              } else PermissionUtils.requestLocationPermission(requireActivity());
            }

            return false;
          });
    }

    mic = findPreference("mic");
    if (mic != null) {
      mic.setChecked(PermissionUtils.hasAudioPermission(requireActivity()));
      mic.setOnPreferenceChangeListener(
          (preference, newValue) -> {
            if (mic.isChecked()) startInstalledAppDetailsActivity(requireActivity());
            else {
              if (PermissionUtils.shouldShowRational(
                  requireActivity(), Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(
                        requireActivity().getApplicationContext(),
                        getResources().getString(R.string.record_audio_permission_denied)
                            + " "
                            + getResources().getString(R.string.permission_reason_settings),
                        Toast.LENGTH_LONG)
                    .show();
                startInstalledAppDetailsActivity(requireActivity());
              } else PermissionUtils.requestAudioPermission(getActivity());
            }
            return false;
          });
    }
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewModel
        .getUsbStatus()
        .observe(
            getViewLifecycleOwner(),
            status -> {
              if (connection != null) {
                connection.setChecked(status);
                connection.setTitle(
                    status ? vehicle.getUsbConnection().getProductName() : "No Device");
              }
            });
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case Constants.REQUEST_LOCATION_PERMISSION:
        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          location.setChecked(true);
        } else {
          location.setChecked(false);
          if (PermissionUtils.shouldShowRational(
              requireActivity(), Constants.PERMISSION_LOCATION)) {
            Toast.makeText(
                    requireActivity().getApplicationContext(),
                    getResources().getString(R.string.location_permission_denied)
                        + " "
                        + getResources().getString(R.string.permission_reason_settings),
                    Toast.LENGTH_LONG)
                .show();
          }
        }
        break;
      case Constants.REQUEST_STORAGE_PERMISSION:
        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          storage.setChecked(true);
        } else {
          storage.setChecked(false);
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_STORAGE)) {
            Toast.makeText(
                    requireContext().getApplicationContext(),
                    getResources().getString(R.string.storage_permission_denied)
                        + " "
                        + getResources().getString(R.string.permission_reason_settings),
                    Toast.LENGTH_LONG)
                .show();
          }
        }
        break;
      case Constants.REQUEST_AUDIO_PERMISSION:
        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          mic.setChecked(true);
        } else {
          mic.setChecked(false);
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_AUDIO)) {
            Toast.makeText(
                    requireActivity().getApplicationContext(),
                    getResources().getString(R.string.record_audio_permission_denied)
                        + " "
                        + getResources().getString(R.string.permission_reason_settings),
                    Toast.LENGTH_LONG)
                .show();
          }
        }
        break;
      case Constants.REQUEST_CAMERA_PERMISSION:
        if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          camera.setChecked(true);
        } else {
          camera.setChecked(false);
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_CAMERA)) {
            Toast.makeText(
                    requireActivity().getApplicationContext(),
                    getResources().getString(R.string.camera_permission_denied)
                        + " "
                        + getResources().getString(R.string.permission_reason_settings),
                    Toast.LENGTH_LONG)
                .show();
          }
        }
        break;
    }
  }

  public void startInstalledAppDetailsActivity(final Activity context) {
    if (context == null) {
      return;
    }
    final Intent i = new Intent();
    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    i.addCategory(Intent.CATEGORY_DEFAULT);
    i.setData(Uri.parse("package:" + context.getPackageName()));
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
    context.startActivity(i);
  }
}
