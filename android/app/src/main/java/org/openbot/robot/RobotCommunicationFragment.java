package org.openbot.robot;

import static org.openbot.common.Enums.ControlMode;
import static org.openbot.common.Enums.DriveMode;
import static org.openbot.common.Enums.SpeedMode;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.anastr.speedviewlib.components.Section;
import com.google.android.material.internal.ViewUtils;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.openbot.R;
import org.openbot.common.Constants;
import org.openbot.common.Enums;
import org.openbot.common.Utils;
import org.openbot.databinding.FragmentRobotCommunicationBinding;
import org.openbot.env.BotToControllerEventBus;
import org.openbot.env.Control;
import org.openbot.env.ControllerToBotEventBus;
import org.openbot.env.GameController;
import org.openbot.env.PhoneController;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.env.Vehicle;
import org.openbot.main.MainViewModel;
import org.openbot.utils.PermissionUtils;
import timber.log.Timber;

public class RobotCommunicationFragment extends Fragment {

  private FragmentRobotCommunicationBinding binding;

  private SharedPreferencesManager preferencesManager;
  protected GameController gameController;
  private final PhoneController phoneController = new PhoneController();
  protected DriveMode driveMode = DriveMode.GAME;
  private MainViewModel mViewModel;
  private Animation startAnimation;
  private Vehicle vehicle;

  @Override
  public View onCreateView(
      @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentRobotCommunicationBinding.inflate(inflater, container, false);

    return binding.getRoot();
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    startAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink);

    preferencesManager = new SharedPreferencesManager(requireContext());
    gameController = new GameController(driveMode);
    mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

    vehicle = mViewModel.getVehicle().getValue();
    binding.voltageInfo.setText(getString(R.string.voltageInfo, "--.-"));
    binding.speedInfo.setText(getString(R.string.speedInfo, "---,---"));
    binding.sonarInfo.setText(getString(R.string.distanceInfo, "---"));

    listenUSBData();

    setSpeedMode(SpeedMode.getByID(preferencesManager.getSpeedMode()));
    setControlMode(ControlMode.getByID(preferencesManager.getControlMode()));
    setDriveMode(DriveMode.getByID(preferencesManager.getDriveMode()));

    binding.controlMode.setOnClickListener(
        v -> {
          ControlMode controlMode = ControlMode.getByID(preferencesManager.getControlMode());
          if (controlMode != null) {
            switch (controlMode) {
              case GAMEPAD:
                setControlMode(ControlMode.PHONE);
                break;
              case PHONE:
                setControlMode(ControlMode.GAMEPAD);
                break;
            }
          }
        });
    binding.driveMode.setOnClickListener(v -> changeDriveMode());

    binding.speedMode.setOnClickListener(v -> toggleSpeed(Enums.Direction.CYCLIC.getValue()));

    binding.speed.getSections().clear();
    binding.speed.addSections(
        new Section(
            0f,
            0.7f,
            getResources().getColor(R.color.green),
            ViewUtils.dpToPx(requireContext(), 24)),
        new Section(
            0.7f,
            0.8f,
            getResources().getColor(R.color.yellow),
            ViewUtils.dpToPx(requireContext(), 24)),
        new Section(
            0.8f,
            1.0f,
            getResources().getColor(R.color.red),
            ViewUtils.dpToPx(requireContext(), 24)));

    requireActivity()
        .getSupportFragmentManager()
        .setFragmentResultListener(
            "dispatchGenericMotionEvent",
            this,
            (requestKey, result) -> onMotionEvent(result.getParcelable("event")));
    requireActivity()
        .getSupportFragmentManager()
        .setFragmentResultListener(
            "dispatchKeyEvent",
            this,
            (requestKey, result) -> onKeyEvent(result.getParcelable("keyEvent")));
  }

  public void onKeyEvent(KeyEvent keyCode) {
    if (ControlMode.getByID(preferencesManager.getControlMode()) == ControlMode.GAMEPAD)
      switch (keyCode.getKeyCode()) {
        case KeyEvent.KEYCODE_BUTTON_A: // x
          //            handleLogging();
          break;
        case KeyEvent.KEYCODE_BUTTON_X: // square
          toggleIndicator(Enums.VehicleIndicator.LEFT.getValue());
          break;
        case KeyEvent.KEYCODE_BUTTON_Y: // triangle
          toggleIndicator(Enums.VehicleIndicator.STOP.getValue());
          break;
        case KeyEvent.KEYCODE_BUTTON_B: // circle
          toggleIndicator(Enums.VehicleIndicator.RIGHT.getValue());

          break;
        case KeyEvent.KEYCODE_BUTTON_START: // options
          //            handleNoise();
          break;
        case KeyEvent.KEYCODE_BUTTON_L1:
          changeDriveMode();
          break;
        case KeyEvent.KEYCODE_BUTTON_R1:
          //            handleNetwork();
          break;
        case KeyEvent.KEYCODE_BUTTON_THUMBL:
          toggleSpeed(Enums.Direction.DOWN.getValue());
          break;
        case KeyEvent.KEYCODE_BUTTON_THUMBR:
          toggleSpeed(Enums.Direction.UP.getValue());
          break;

        default:
          break;
      }
  }

  public void onMotionEvent(MotionEvent motionEvent) {
    handleDriveCommand(gameController.processJoystickInput(motionEvent, -1));
  }

  private void toggleIndicator(int value) {
    vehicle.setIndicator(value);
    binding.indicatorRight.clearAnimation();
    binding.indicatorLeft.clearAnimation();
    binding.indicatorRight.setVisibility(View.INVISIBLE);
    binding.indicatorLeft.setVisibility(View.INVISIBLE);

    if (value == Enums.VehicleIndicator.RIGHT.getValue()) {
      binding.indicatorRight.startAnimation(startAnimation);
      binding.indicatorRight.setVisibility(View.VISIBLE);
    } else if (value == Enums.VehicleIndicator.LEFT.getValue()) {
      binding.indicatorLeft.startAnimation(startAnimation);
      binding.indicatorLeft.setVisibility(View.VISIBLE);
    }
    BotToControllerEventBus.emitEvent(Utils.createStatus("INDICATOR_LEFT", value == -1));
    BotToControllerEventBus.emitEvent(Utils.createStatus("INDICATOR_RIGHT", value == 1));
    BotToControllerEventBus.emitEvent(Utils.createStatus("INDICATOR_STOP", value == 0));
  }

  private void toggleSpeed(int direction) {
    SpeedMode speedMode = SpeedMode.getByID(preferencesManager.getSpeedMode());
    if (speedMode != null)
      switch (speedMode) {
        case SLOW:
          if (direction != Enums.Direction.DOWN.getValue()) setSpeedMode(SpeedMode.NORMAL);
          break;
        case NORMAL:
          setSpeedMode(
              direction == Enums.Direction.DOWN.getValue() ? SpeedMode.SLOW : SpeedMode.FAST);
          break;
        case FAST:
          if (direction == Enums.Direction.DOWN.getValue()) setSpeedMode(SpeedMode.NORMAL);
          else if (direction == Enums.Direction.CYCLIC.getValue()) setSpeedMode(SpeedMode.SLOW);
          break;
      }
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

              binding.speedInfo.setText(
                  getString(
                      R.string.speedInfo,
                      String.format(
                          Locale.US,
                          "%3.0f,%3.0f",
                          vehicle.getLeftWheelRPM(),
                          vehicle.getRightWheelRPM())));

              binding.voltageInfo.setText(
                  getString(
                      R.string.voltageInfo,
                      String.format(Locale.US, "%2.1f", vehicle.getBatteryVoltage())));
              binding.battery.setProgress(vehicle.getBatteryPercentage());
              if (vehicle.getBatteryPercentage() < 15) {
                binding.battery.setProgressTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.red)));
                binding.battery.setProgressBackgroundTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.red)));
              } else {
                binding.battery.setProgressTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.green)));
                binding.battery.setProgressBackgroundTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.green)));
              }

              binding.sonar.setProgress((int) (vehicle.getSonarReading() / 3));
              if (vehicle.getSonarReading() / 3 < 15) {
                binding.sonar.setProgressTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.red)));
              } else if (vehicle.getSonarReading() / 3 < 45) {
                binding.sonar.setProgressTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
              } else {
                binding.sonar.setProgressTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.green)));
              }

              binding.sonarInfo.setText(
                  getString(
                      R.string.distanceInfo,
                      String.format(Locale.US, "%3.0f", vehicle.getSonarReading())));
            });
  }

  protected void handleDriveCommand(Control control) {
    vehicle.setControl(control);
    float left = vehicle.getLeftSpeed();
    float right = vehicle.getRightSpeed();
    binding.controlInfo.setText(String.format(Locale.US, "%.0f,%.0f", left, right));

    binding.speed.speedPercentTo(vehicle.getSpeedPercent());

    binding.steering.setRotation(vehicle.getRotation());

    binding.driveGear.setText(vehicle.getDriveGear());
  }

  private void setSpeedMode(SpeedMode speedMode) {
    if (speedMode != null) {
      switch (speedMode) {
        case SLOW:
          binding.speedMode.setImageResource(R.drawable.ic_speed_low);
          break;
        case NORMAL:
          binding.speedMode.setImageResource(R.drawable.ic_speed_medium);
          break;
        case FAST:
          binding.speedMode.setImageResource(R.drawable.ic_speed_high);
          break;
      }

      Timber.d("Updating  controlSpeed: %s", speedMode);
      preferencesManager.setSpeedMode(speedMode.getValue());
      vehicle.setSpeedMultiplier(speedMode.getValue());
    }
  }

  private void setControlMode(ControlMode controlMode) {
    if (controlMode != null) {
      switch (controlMode) {
        case GAMEPAD:
          binding.controlMode.setImageResource(R.drawable.ic_controller);
          disconnectPhoneController();
          break;
        case PHONE:
          binding.controlMode.setImageResource(R.drawable.ic_phone);
          handleControllerEvents();
          if (!PermissionUtils.hasPermission(requireContext(), Constants.PERMISSION_LOCATION))
            PermissionUtils.requestPermissions(
                this,
                new String[] {Constants.PERMISSION_LOCATION},
                Constants.REQUEST_LOCATION_PERMISSION_CONTROLLER);
          else connectPhoneController();

          break;
      }
      Timber.d("Updating  controlMode: %s", controlMode);
      preferencesManager.setControlMode(controlMode.getValue());
    }
  }

  protected void setDriveMode(DriveMode driveMode) {
    if (this.driveMode != driveMode && driveMode != null) {
      switch (driveMode) {
        case DUAL:
          binding.driveMode.setImageResource(R.drawable.ic_dual);
          break;
        case GAME:
          binding.driveMode.setImageResource(R.drawable.ic_game);
          break;
        case JOYSTICK:
          binding.driveMode.setImageResource(R.drawable.ic_joystick);
          break;
      }

      Timber.d("Updating  driveMode: %s", driveMode);
      this.driveMode = driveMode;
      preferencesManager.setDriveMode(driveMode.getValue());
      gameController.setDriveMode(driveMode);
    }
  }

  protected void changeDriveMode() {
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
  }

  private void connectPhoneController() {
    if (!phoneController.isConnected()) {
      phoneController.connect(requireContext());
    }
    DriveMode oldDriveMode = driveMode;
    // Currently only dual drive mode supported
    setDriveMode(DriveMode.DUAL);
    binding.driveMode.setAlpha(0.5f);
    binding.driveMode.setEnabled(false);
    preferencesManager.setDriveMode(oldDriveMode.getValue());
  }

  private void disconnectPhoneController() {
    if (phoneController.isConnected()) {
      phoneController.disconnect();
    }
    setDriveMode(DriveMode.getByID(preferencesManager.getDriveMode()));
    binding.driveMode.setEnabled(true);
    binding.driveMode.setAlpha(1.0f);
  }

  private void handleControllerEvents() {
    // Prevent multiple subscriptions. This happens if we select "Phone control multiple times.
    if (ControllerToBotEventBus.getProcessor().hasObservers()) {
      return;
    }

    ControllerToBotEventBus.getProcessor()
        .subscribe(
            event -> {
              String commandType = "";
              if (event.has("command")) {
                commandType = event.getString("command");
              } else if (event.has("driveCmd")) {
                commandType = "DRIVE_CMD";
              } else {
                Timber.d("Got invalid command from controller: %s", event.toString());
                return;
              }

              switch (commandType) {
                case "DRIVE_CMD":
                  JSONObject driveValue = event.getJSONObject("driveCmd");
                  handleDriveCommand(
                      new Control(
                          Float.parseFloat(driveValue.getString("l")),
                          Float.parseFloat(driveValue.getString("r"))));
                  break;

                case "INDICATOR_LEFT":
                  toggleIndicator(Enums.VehicleIndicator.LEFT.getValue());
                  break;

                case "INDICATOR_RIGHT":
                  toggleIndicator(Enums.VehicleIndicator.RIGHT.getValue());
                  break;

                case "INDICATOR_STOP":
                  toggleIndicator(Enums.VehicleIndicator.STOP.getValue());
                  break;

                case "DRIVE_MODE":
                  changeDriveMode();
                  break;

                  // We re connected to the controller, send back status info
                case "CONNECTED":
                  // PhoneController class will receive this event and resent it to the controller.
                  // Other controllers can subscribe to this event as well.
                  // That is why we are not calling phoneController.send() here directly.
                  BotToControllerEventBus.emitEvent(
                      Utils.getStatus(
                          false, false, false, driveMode.getValue(), vehicle.getIndicator()));

                  break;
                case "DISCONNECTED":
                  handleDriveCommand(new Control(0.f, 0.f));
                  setControlMode(ControlMode.GAMEPAD);
                  break;
              }
            });
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case Constants.REQUEST_LOCATION_PERMISSION_CONTROLLER:
        // If the permission is granted, start advertising to controller,
        // otherwise, show a Toast
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          if (!phoneController.isConnected()) {
            phoneController.connect(requireContext());
          }
        } else {
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_LOCATION))
            Toast.makeText(
                    requireActivity().getApplicationContext(),
                    R.string.location_permission_denied_controller,
                    Toast.LENGTH_LONG)
                .show();
        }
        break;
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    handleDriveCommand(new Control(0.f, 0.f));
    vehicle.disconnectUsb();
  }
}
