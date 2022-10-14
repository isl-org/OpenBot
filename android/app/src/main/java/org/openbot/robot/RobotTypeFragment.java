package org.openbot.robot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentRobotTypeBinding;
import org.openbot.R;

public class RobotTypeFragment extends ControlsFragment {
  private FragmentRobotTypeBinding binding;

  @Override
  public View onCreateView(
          @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentRobotTypeBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
    vehicle = mViewModel.getVehicle().getValue();

    mViewModel
            .getUsbStatus()
            .observe(getViewLifecycleOwner(), status -> binding.usbToggle.setChecked(status));

    binding.usbToggle.setChecked(vehicle.isUsbConnected());

    binding.usbToggle.setOnClickListener(
            v -> {
              binding.usbToggle.setChecked(vehicle.isUsbConnected());
              Navigation.findNavController(requireView()).navigate(R.id.open_settings_fragment);
            });
  }

  @Override
  protected void processControllerKeyData(String command) {
    //Do nothing
  }


  @Override
  protected void processUSBData(String data) {
    binding.robotTypeInfo.setText(vehicle.getVehicleType());
    binding.voltageSwitch.setChecked(vehicle.isHasVoltageDivider());
    binding.sonarSwitch.setChecked(vehicle.isHasSonar());
    binding.indicatorLedsSwitch.setChecked(vehicle.isHasIndicators());
    binding.ledsFrontSwitch.setChecked(vehicle.isHasLedsFront());
    binding.ledsBackSwitch.setChecked(vehicle.isHasLedsBack());
    binding.ledsStatusSwitch.setChecked(vehicle.isHasLedsStatus());
    binding.bumpersSwitch.setChecked(vehicle.isHasBumpSensor());
    binding.wheelOdometryFrontSwitch.setChecked(vehicle.isHasWheelOdometryFront());
    binding.wheelOdometryBackSwitch.setChecked(vehicle.isHasWheelOdometryBack());
  }
}
