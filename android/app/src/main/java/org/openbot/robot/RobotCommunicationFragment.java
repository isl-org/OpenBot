package org.openbot.robot;

import static org.openbot.common.Enums.ControlMode;
import static org.openbot.common.Enums.DriveMode;
import static org.openbot.common.Enums.SpeedMode;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
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
import org.openbot.env.Control;
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
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    SharedPreferences sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(requireActivity());
    int baudRate = Integer.parseInt(sharedPreferences.getString("baud_rate", "115200"));

    startAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink);

    vehicle = new Vehicle(requireContext(), baudRate);
    preferencesManager = new SharedPreferencesManager(requireContext());
    gameController = new GameController(driveMode);
    mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

    binding.voltageInfo.setText(getString(R.string.voltageInfo, "--.-"));
    binding.speedInfo.setText(getString(R.string.speedInfo, "---,---"));
    binding.sonarInfo.setText(getString(R.string.distanceInfo, "---"));

    listenUSBData();

    setSpeedMode(SpeedMode.getByID(preferencesManager.getSpeedMode()));
    setControlMode(ControlMode.getByID(preferencesManager.getControlMode()));
    setDriveMode(DriveMode.getByID(preferencesManager.getDriveMode()));

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
              break;
            case NORMAL:
              setSpeedMode(SpeedMode.FAST);
              break;
            case FAST:
              setSpeedMode(SpeedMode.SLOW);
              break;
          }
        });

    //    Timber.d("Speed: %s", new Control(1, 1).getLeft());

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
    if (controlMode == ControlMode.GAMEPAD)
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
          handleDriveMode();
          break;
        case KeyEvent.KEYCODE_BUTTON_R1:
          //            handleNetwork();
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
  public void onDestroy() {
    super.onDestroy();
    vehicle.disconnectUsb();
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
    if (this.speedMode != speedMode && speedMode != null) {
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
      this.speedMode = speedMode;
      preferencesManager.setSpeedMode(speedMode.getValue());
      vehicle.setSpeedMultiplier(speedMode.getValue());
    }
  }

  private void setControlMode(ControlMode controlMode) {
    if (this.controlMode != controlMode && controlMode != null) {
      switch (controlMode) {
        case GAMEPAD:
          binding.controlMode.setImageResource(R.drawable.ic_controller);
          break;
        case PHONE:
          binding.controlMode.setImageResource(R.drawable.ic_phone);
          break;
      }
      Timber.d("Updating  controlMode: %s", controlMode);
      this.controlMode = controlMode;
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
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }
}
