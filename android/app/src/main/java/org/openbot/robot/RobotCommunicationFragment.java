package org.openbot.robot;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;
import androidx.lifecycle.ViewModelProvider;

import com.github.anastr.speedviewlib.components.Section;
import com.google.android.material.internal.ViewUtils;

import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.Enums;
import org.openbot.databinding.FragmentRobotCommunicationBinding;
import org.openbot.env.GameController;
import org.openbot.env.Logger;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.env.UsbConnection;
import org.openbot.env.Vehicle;
import org.openbot.main.MainViewModel;
import org.openbot.robot.CameraFragment;
import org.openbot.robot.RobotCommunicationViewModel;

import java.util.Locale;

import static org.openbot.common.Enums.ControlMode;
import static org.openbot.common.Enums.DriveMode;
import static org.openbot.common.Enums.SpeedMode;

public class RobotCommunicationFragment extends CameraFragment implements AdapterView.OnItemSelectedListener {

  private FragmentRobotCommunicationBinding binding;
  protected Vehicle vehicle;

  private static final Logger LOGGER = new Logger();
  private SharedPreferencesManager preferencesManager;
  protected GameController gameController;
  protected ControlMode controlMode = ControlMode.GAMEPAD;
  protected SpeedMode speedMode = SpeedMode.NORMAL;
  protected DriveMode driveMode = DriveMode.GAME;
  private RobotCommunicationViewModel robotCommunicationViewModel;
  private MainViewModel mViewModel;
  private int baudRate = 115200;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentRobotCommunicationBinding.inflate(inflater, container, false);

    return inflateFragment(binding, inflater, container);
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    vehicle = new Vehicle(requireContext(), baudRate);
    preferencesManager = new SharedPreferencesManager(requireContext());
    gameController = new GameController(driveMode);
    mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    robotCommunicationViewModel =
        new ViewModelProvider(requireActivity()).get(RobotCommunicationViewModel.class);

    binding.voltageInfo.setText(getString(R.string.voltageInfo, "**.*"));
    binding.speedInfo.setText(getString(R.string.speedInfo, "***,***"));
    binding.sonarInfo.setText(getString(R.string.distanceInfo, "***"));

    listenUSBData();
    observeKeyEvents();
    binding.controlModeSpinner.setOnItemSelectedListener(this);
    binding.driveModeSpinner.setOnItemSelectedListener(this);
    binding.speedModeSpinner.setOnItemSelectedListener(this);
    LOGGER.d("Speeed: " + new Vehicle.Control(1, 1).getLeft());

    binding.speed.getSections().clear();
    binding.speed.addSections(new Section(0f,0.7f,getResources().getColor(R.color.green), ViewUtils.dpToPx(requireContext(),24)),
            new Section(0.7f,0.8f,getResources().getColor(R.color.yellow), ViewUtils.dpToPx(requireContext(),24)),
            new Section(0.8f,1.0f,getResources().getColor(R.color.red), ViewUtils.dpToPx(requireContext(),24)));
  }

  private void observeKeyEvents() {
    robotCommunicationViewModel
        .getKeyEvent()
        .observe(
            getViewLifecycleOwner(),
            keyCode -> {
              if (controlMode == ControlMode.GAMEPAD)
                switch (keyCode) {
                  case KeyEvent.KEYCODE_BUTTON_A:
                    //            handleLogging();
                    break;
                  case KeyEvent.KEYCODE_BUTTON_B:
                    vehicle.setIndicator(Enums.VehicleIndicator.LEFT.getValue());
                    //    sendIndicatorStatus(vehicle.getIndicator());
                    break;
                  case KeyEvent.KEYCODE_BUTTON_Y:
                    vehicle.setIndicator(Enums.VehicleIndicator.STOP.getValue());
                    //    sendIndicatorStatus(vehicle.getIndicator());
                    break;
                  case KeyEvent.KEYCODE_BUTTON_X:
                    vehicle.setIndicator(Enums.VehicleIndicator.RIGHT.getValue());
                    //    sendIndicatorStatus(vehicle.getIndicator());

                    break;
                  case KeyEvent.KEYCODE_BUTTON_START:
                    //            handleNoise();
                    break;
                  case KeyEvent.KEYCODE_BUTTON_L1:
                    handleDriveMode();
                    break;
                  case KeyEvent.KEYCODE_BUTTON_R1:
                    //            handleNetwork();
                    break;
                  default:
                    break;
                }
            });

    robotCommunicationViewModel
        .getGenericMotionEvent()
        .observe(
            getViewLifecycleOwner(),
            motionEvent -> {
              if (controlMode == ControlMode.GAMEPAD)
                handleDriveCommand(gameController.processJoystickInput(motionEvent, -1));
            });
  }

  private void listenUSBData() {
    mViewModel
        .getUsbData()
        .observe(
            getViewLifecycleOwner(),
            data -> {
              // Data has the following form: voltage, lWheel, rWheel, obstacle
              //      sendVehicleDataToSensorService(timestamp, data);
              String[] itemList = data.split(",");
              vehicle.setBatteryVoltage(Float.parseFloat(itemList[0]));
              vehicle.setLeftWheelTicks(Float.parseFloat(itemList[1]));
              vehicle.setRightWheelTicks(Float.parseFloat(itemList[2]));
              vehicle.setSonarReading(Float.parseFloat(itemList[3]));
              getActivity()
                  .runOnUiThread(
                      () -> {
                        binding.voltageInfo.setText(
                            getString(
                                R.string.voltageInfo,
                                String.format(Locale.US, "%2.1f V", vehicle.getBatteryVoltage())));
                        binding.speedInfo.setText(
                            getString(
                                R.string.speedInfo,
                                String.format(
                                    Locale.US,
                                    "%3.0f,%3.0f rpm",
                                    vehicle.getLeftWheelRPM(),
                                    vehicle.getRightWheelRPM())));
                        binding.sonarInfo.setText(
                            getString(
                                R.string.distanceInfo,
                                String.format(Locale.US, "%3.0f cm", vehicle.getSonarReading())));
                      });
            });
  }

  @Override
  protected void processFrame(ImageProxy image) {}

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    if (parent == binding.controlModeSpinner) {
      setControlMode(
          ControlMode.valueOf(parent.getItemAtPosition(position).toString().toUpperCase()));
    } else if (parent == binding.speedModeSpinner) {
      setSpeedMode(SpeedMode.valueOf(parent.getItemAtPosition(position).toString().toUpperCase()));
    } else if (parent == binding.driveModeSpinner) {
      setDriveMode(DriveMode.valueOf(parent.getItemAtPosition(position).toString().toUpperCase()));
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {}

  private void setSpeedMode(SpeedMode speedMode) {
    if (this.speedMode != speedMode) {
      LOGGER.d("Updating  controlSpeed: " + speedMode);
      this.speedMode = speedMode;
      preferencesManager.setSpeedMode(speedMode.ordinal());
      vehicle.setSpeedMultiplier(speedMode.getValue());
    }
  }

  private void setControlMode(ControlMode controlMode) {
    if (this.controlMode != controlMode) {
      LOGGER.d("Updating  controlMode: " + controlMode);
      this.controlMode = controlMode;
      preferencesManager.setControlMode(controlMode.ordinal());
      switch (controlMode) {
        case GAMEPAD:
        case PHONE:
        case WEBRTC:
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + controlMode);
      }
    }
  }

  protected void setDriveMode(DriveMode driveMode) {
    if (this.driveMode != driveMode) {
      LOGGER.d("Updating  driveMode: " + driveMode);
      this.driveMode = driveMode;
      preferencesManager.setDriveMode(driveMode.ordinal());
      gameController.setDriveMode(driveMode);
      binding.driveModeSpinner.setSelection(driveMode.ordinal());
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    vehicle.disconnectUsb();
  }

  protected void handleDriveCommand(Vehicle.Control control) {
    vehicle.setControl(control);
    //    updateVehicleState();
  }

  protected void handleDriveCommand(Float l, Float r) {
    vehicle.setControl(l, r);
    //    updateVehicleState();
  }

  protected void handleDriveMode() {
    //    if (networkEnabled) return;
    switch (driveMode) {
      case DUAL:
        setDriveMode(DriveMode.GAME);
        break;
      case GAME:
        setDriveMode(DriveMode.JOYSTICK);
        break;
      case JOYSTICK:
        setDriveMode(DriveMode.DUAL);
        break;
    }
    binding.driveModeSpinner.setSelection(driveMode.ordinal());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

}
