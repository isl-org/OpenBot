package org.openbot.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import org.openbot.R;
import org.openbot.env.Vehicle;
import timber.log.Timber;

public class SettingsFragment extends PreferenceFragmentCompat {
  private MainViewModel mViewModel;
  private SwitchPreferenceCompat connection;
  private Vehicle vehicle;

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

    SwitchPreferenceCompat camera = findPreference("camera");
    if (camera != null)
      camera.setChecked(
          ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
              == PackageManager.PERMISSION_GRANTED);

    SwitchPreferenceCompat storage = findPreference("storage");
    if (storage != null)
      storage.setChecked(
          ContextCompat.checkSelfPermission(
                  requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
              == PackageManager.PERMISSION_GRANTED);
    SwitchPreferenceCompat location = findPreference("location");
    if (location != null)
      location.setChecked(
          ContextCompat.checkSelfPermission(
                  requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
              == PackageManager.PERMISSION_GRANTED);

    SwitchPreferenceCompat mic = findPreference("mic");
    if (mic != null)
      mic.setChecked(
          ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
              == PackageManager.PERMISSION_GRANTED);
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
}
