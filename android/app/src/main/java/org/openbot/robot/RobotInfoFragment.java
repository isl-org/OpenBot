package org.openbot.robot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentRobotInfoBinding;

public class RobotInfoFragment extends ControlsFragment {
  private FragmentRobotInfoBinding binding;

  @Override
  public View onCreateView(
      @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentRobotInfoBinding.inflate(inflater, container, false);
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

    binding.lightsSlider.addOnChangeListener(
        (slider, value, fromUser) -> {
          vehicle.sendLightIntensity(value / 100, value / 100);
        });

    binding.motorsForwardButton.setOnClickListener(
        v -> {
          vehicle.setControl(0.5f, 0.5f);
        });

    binding.motorsBackwardButton.setOnClickListener(
        v -> {
          vehicle.setControl(-0.5f, -0.5f);
        });

    binding.motorsStopButton.setOnClickListener(
        v -> {
          vehicle.setControl(0.0f, 0.0f);
        });

    refreshGui();
  }

  private void refreshGui() {
    updateGui(false);
    binding.refreshToggle.setChecked(false);
    if (vehicle.isReady()) {
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
      binding.voltageInfo.setText(R.string.voltage);
      binding.speedInfo.setText(R.string.rpm);
      binding.sonarInfo.setText(R.string.distance);
    }
  }

  @Override
  protected void processControllerKeyData(String command) {
    // Do nothing
  }

  @Override
  protected void processUSBData(String data) {
    if (!vehicle.isReady()) {
      vehicle.setReady(true);
      vehicle.requestVehicleConfig();
    }
    char header = data.charAt(0);
    String body = data.substring(1);
    int type = -1;
    switch (header) {
      case 'r':
        vehicle.requestVehicleConfig();
      case 'f':
        binding.refreshToggle.setChecked(vehicle.isReady());
        updateGui(vehicle.isReady());
        break;
      case 'v':
        binding.voltageInfo.setText(
            String.format(Locale.US, "%2.1f V", vehicle.getBatteryVoltage()));
        break;
      case 'w':
        binding.speedInfo.setText(
            String.format(
                Locale.US,
                "%3.0f,%3.0f rpm",
                vehicle.getLeftWheelRpm(),
                vehicle.getRightWheelRpm()));
        break;
      case 's':
        binding.sonarInfo.setText(String.format(Locale.US, "%3.0f cm", vehicle.getSonarReading()));
        break;
    }
  }
}
