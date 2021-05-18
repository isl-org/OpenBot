package org.openbot.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import org.jetbrains.annotations.NotNull;
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
    if (camera != null)
    {
      camera.setChecked(PermissionUtils.hasPermission(requireContext(), Manifest.permission.CAMERA));
      camera.setOnPreferenceChangeListener((preference, newValue) -> {
        PermissionUtils.requestPermissions(
                SettingsFragment.this,
                new String[] {Constants.PERMISSION_CAMERA},
                Constants.REQUEST_CAMERA_PERMISSION);

        return false;
      });
    }

    storage = findPreference("storage");
    if (storage != null)
    {
      storage.setChecked(PermissionUtils.hasPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE));
      storage.setOnPreferenceChangeListener((preference, newValue) -> {
        PermissionUtils.requestPermissions(
                SettingsFragment.this,
                new String[] {Constants.PERMISSION_STORAGE},
                Constants.REQUEST_STORAGE_PERMISSION);

        return false;
      });

    }

    location = findPreference("location");
    if (location != null)
    {
      location.setChecked(PermissionUtils.hasPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION));
      location.setOnPreferenceChangeListener((preference, newValue) -> {
        PermissionUtils.requestPermissions(
                SettingsFragment.this,
                new String[] {Constants.PERMISSION_LOCATION},
                Constants.REQUEST_LOCATION_PERMISSION_CONTROLLER);

        return false;
      });
    }


    mic = findPreference("mic");
    if (mic != null)
    {
      mic.setChecked(PermissionUtils.hasPermission(requireContext(), Manifest.permission.RECORD_AUDIO));
      mic.setOnPreferenceChangeListener((preference, newValue) -> {
        PermissionUtils.requestPermissions(
                SettingsFragment.this,
                new String[] {Constants.PERMISSION_AUDIO_RECORDING},
                Constants.REQUEST_AUDIO_RECORDING_PERMISSIONS);

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
      case Constants.REQUEST_LOCATION_PERMISSION_CONTROLLER:
      if (grantResults.length >0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
        location.setChecked(true);
      } else {
        location.setChecked(false);
        if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_LOCATION)) {
          Toast.makeText(
                  requireActivity().getApplicationContext(),
                  R.string.location_permission_denied_controller,
                  Toast.LENGTH_LONG)
                  .show();
        }
      }
      break;
      case Constants.REQUEST_STORAGE_PERMISSION:
        if (grantResults.length >0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          storage.setChecked(true);
        } else {
          storage.setChecked(false);
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_STORAGE)) {
            Toast.makeText(
                    requireContext().getApplicationContext(),
                    R.string.storage_permission_denied_logging,
                    Toast.LENGTH_LONG)
                    .show();
          }
        }
        break;
      case Constants.REQUEST_AUDIO_RECORDING_PERMISSIONS:
        if (grantResults.length >0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          mic.setChecked(true);
        } else {
          mic.setChecked(false);
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_AUDIO_RECORDING)) {
            Toast.makeText(
                    requireActivity().getApplicationContext(),
                    R.string.record_audio_permission_denied_controller,
                    Toast.LENGTH_LONG)
                    .show();
          }
        }
        break;
      case Constants.REQUEST_CAMERA_PERMISSION:
        if (grantResults.length >0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          camera.setChecked(true);
        } else {
          camera.setChecked(false);
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_CAMERA)) {
            Toast.makeText(
                    requireActivity().getApplicationContext(),
                    R.string.camera_permission_denied_controller,
                    Toast.LENGTH_LONG)
                    .show();
          }
        }
        break;
    }

  }

}
