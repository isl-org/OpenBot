package org.openbot.robot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentRobotTypeBinding;

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
    super.onViewCreated(view, savedInstanceState);

    mViewModel
        .getUsbStatus()
        .observe(getViewLifecycleOwner(), status -> binding.usbToggle.setChecked(status));

    binding.usbToggle.setChecked(vehicle.isUsbConnected());

    binding.usbToggle.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          refreshGui();
        });
    binding.usbToggle.setOnClickListener(
        v -> {
          binding.usbToggle.setChecked(vehicle.isUsbConnected());
          Navigation.findNavController(requireView()).navigate(R.id.open_settings_fragment);
        });
    binding.refreshToggle.setOnClickListener(
        v -> {
          refreshGui();
        });
    refreshGui();
  }

  private void refreshGui() {
    updateGui(false);
    binding.refreshToggle.setChecked(false);
    if (vehicle.isUsbConnected()) {
      vehicle.requestVehicleConfig();
    }
  }

  private void updateGui(boolean isConnected) {
    if (isConnected) {
      binding.robotTypeInfo.setText(vehicle.getVehicleType());
      switch (vehicle.getVehicleType()) {
        case "DIY":
          binding.robotIcon.setImageResource(R.drawable.diy);
          break;
        case "RTR_TT":
          binding.robotIcon.setImageResource(R.drawable.rtr_tt);
          break;
        case "RTR_520":
          binding.robotIcon.setImageResource(R.drawable.rtr_520);
          break;
        default:
          binding.robotIcon.setImageResource(R.drawable.ic_openbot);
          break;
      }
      binding.voltageSwitch.setChecked(vehicle.isHasVoltageDivider());
      binding.sonarSwitch.setChecked(vehicle.isHasSonar());
      binding.indicatorLedsSwitch.setChecked(vehicle.isHasIndicators());
      binding.ledsFrontSwitch.setChecked(vehicle.isHasLedsFront());
      binding.ledsBackSwitch.setChecked(vehicle.isHasLedsBack());
      binding.ledsStatusSwitch.setChecked(vehicle.isHasLedsStatus());
      binding.bumpersSwitch.setChecked(vehicle.isHasBumpSensor());
      binding.wheelOdometryFrontSwitch.setChecked(vehicle.isHasWheelOdometryFront());
      binding.wheelOdometryBackSwitch.setChecked(vehicle.isHasWheelOdometryBack());
    } else {
      binding.robotTypeInfo.setText(getString(R.string.n_a));
      binding.robotIcon.setImageResource(R.drawable.ic_openbot);
      binding.voltageSwitch.setChecked(false);
      binding.sonarSwitch.setChecked(false);
      binding.indicatorLedsSwitch.setChecked(false);
      binding.ledsFrontSwitch.setChecked(false);
      binding.ledsBackSwitch.setChecked(false);
      binding.ledsStatusSwitch.setChecked(false);
      binding.bumpersSwitch.setChecked(false);
      binding.wheelOdometryFrontSwitch.setChecked(false);
      binding.wheelOdometryBackSwitch.setChecked(false);
    }
  }

  @Override
  protected void processControllerKeyData(String command) {
    // Do nothing
  }

  @Override
  protected void processUSBData(String data) {
    char header = data.charAt(0);
    if (header == 'f') {
      binding.refreshToggle.setChecked(true);
      updateGui(vehicle.isUsbConnected());
    }
  }
}
