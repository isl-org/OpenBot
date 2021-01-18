package org.openbot.robot;

import static org.openbot.common.Enums.ControlMode;
import static org.openbot.common.Enums.DriveMode;
import static org.openbot.common.Enums.SpeedMode;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import com.github.anastr.speedviewlib.components.Section;
import com.google.android.material.internal.ViewUtils;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.Enums;
import org.openbot.databinding.FragmentRobotCommunicationBinding;
import org.openbot.env.GameController;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.env.Vehicle;
import org.openbot.main.MainViewModel;
import timber.log.Timber;

public class RobotCommunicationFragment extends Fragment {

  private FragmentRobotCommunicationBinding binding;
  protected Vehicle vehicle;

  private SharedPreferencesManager preferencesManager;
  protected GameController gameController;
  protected ControlMode controlMode = ControlMode.GAMEPAD;
  protected SpeedMode speedMode = SpeedMode.NORMAL;
  protected DriveMode driveMode = DriveMode.GAME;
  private RobotCommunicationViewModel robotCommunicationViewModel;
  private MainViewModel mViewModel;
  Animation startAnimation;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentRobotCommunicationBinding.inflate(inflater, container, false);

    return binding.getRoot();
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    SharedPreferences sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(requireActivity());
    int baudRate = Integer.parseInt(sharedPreferences.getString("baud_rate", "115200"));

    startAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink);

    vehicle = new Vehicle(requireContext(), baudRate);
    preferencesManager = new SharedPreferencesManager(requireContext());
    gameController = new GameController(driveMode);
    mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    robotCommunicationViewModel =
        new ViewModelProvider(requireActivity()).get(RobotCommunicationViewModel.class);

    binding.voltageInfo.setText(getString(R.string.voltageInfo, "--.-"));
    binding.speedInfo.setText(getString(R.string.speedInfo, "---,---"));
    binding.sonarInfo.setText(getString(R.string.distanceInfo, "---"));

    listenUSBData();
    observeKeyEvents();
    binding.controlMode.setImageResource(R.drawable.ic_controller);
    binding.speedMode.setImageResource(R.drawable.ic_speed_medium);
    binding.driveMode.setImageResource(R.drawable.ic_game);

    binding.controlMode.setOnClickListener(
        v -> {
          switch (controlMode) {
            case GAMEPAD:
              setControlMode(ControlMode.PHONE);
              binding.controlMode.setImageResource(R.drawable.ic_phone);
              break;
            case PHONE:
              setControlMode(ControlMode.GAMEPAD);
              binding.controlMode.setImageResource(R.drawable.ic_controller);
              break;
          }
        });
    binding.driveMode.setOnClickListener(v -> handleDriveMode());

    binding.speedMode.setOnClickListener(
        v -> {
          switch (speedMode) {
            case SLOW:
              setSpeedMode(SpeedMode.NORMAL);
              binding.speedMode.setImageResource(R.drawable.ic_speed_medium);
              break;
            case NORMAL:
              setSpeedMode(SpeedMode.FAST);
              binding.speedMode.setImageResource(R.drawable.ic_speed_high);
              break;
            case FAST:
              setSpeedMode(SpeedMode.SLOW);
              binding.speedMode.setImageResource(R.drawable.ic_speed_low);
              break;
          }
        });

    Timber.d("Speed: %s", new Vehicle.Control(1, 1).getLeft());

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
  }

  private void observeKeyEvents() {
    robotCommunicationViewModel
        .getKeyEvent()
        .observe(
            getViewLifecycleOwner(),
            keyCode -> {
              if (controlMode == ControlMode.GAMEPAD)
                switch (keyCode) {
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

              binding.sonar.setProgress((int) (vehicle.getSonarReading() / 3));
              binding.sonarInfo.setText(
                  getString(
                      R.string.distanceInfo,
                      String.format(Locale.US, "%3.0f", vehicle.getSonarReading())));
            });
  }

  @Override
  public void onStop() {
    super.onStop();
    vehicle.disconnectUsb();
  }

  protected void handleDriveCommand(Vehicle.Control control) {
    vehicle.setControl(control);
    float left = vehicle.getControl().getLeft();
    float right = vehicle.getControl().getRight();
    binding.controlInfo.setText(String.format(Locale.US, "%.0f,%.0f", left, right));

    float throttle = (left + right) / 2;

    binding.speed.speedPercentTo(Math.abs((int) (throttle * 100 / 255))); // 255 is the max speed

    //    Log.i("Steer", "listenUSBData: " + (left - right) / (left + right));

    float rotation = (left - right) * 180 / (left + right);
    if (Float.isNaN(rotation) || Float.isInfinite(rotation)) rotation = 0f;
    binding.steering.setRotation(rotation);

    if (throttle > 0) binding.driveGear.setText("D");
    else if (throttle < 0) binding.driveGear.setText("R");
    else binding.driveGear.setText("P");
  }

  private void setSpeedMode(SpeedMode speedMode) {
    if (this.speedMode != speedMode) {
      Timber.d("Updating  controlSpeed: %s", speedMode);
      this.speedMode = speedMode;
      preferencesManager.setSpeedMode(speedMode.ordinal());
      vehicle.setSpeedMultiplier(speedMode.getValue());
    }
  }

  private void setControlMode(ControlMode controlMode) {
    if (this.controlMode != controlMode) {
      Timber.d("Updating  controlMode: %s", controlMode);
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
      Timber.d("Updating  driveMode: %s", driveMode);
      this.driveMode = driveMode;
      preferencesManager.setDriveMode(driveMode.ordinal());
      gameController.setDriveMode(driveMode);
    }
  }

  protected void handleDriveMode() {
    //    if (networkEnabled) return;
    switch (driveMode) {
      case DUAL:
        setDriveMode(DriveMode.GAME);
        binding.driveMode.setImageResource(R.drawable.ic_game);
        break;
      case GAME:
        setDriveMode(DriveMode.JOYSTICK);
        binding.driveMode.setImageResource(R.drawable.ic_joystick);
        break;
      case JOYSTICK:
        setDriveMode(DriveMode.DUAL);
        binding.driveMode.setImageResource(R.drawable.ic_dual);
        break;
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }
}
