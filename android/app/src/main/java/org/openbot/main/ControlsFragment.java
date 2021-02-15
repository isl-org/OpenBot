package org.openbot.main;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import org.json.JSONObject;
import org.openbot.R;
import org.openbot.common.Constants;
import org.openbot.common.Enums;
import org.openbot.common.Utils;
import org.openbot.env.BotToControllerEventBus;
import org.openbot.env.Control;
import org.openbot.env.ControllerToBotEventBus;
import org.openbot.env.GameController;
import org.openbot.env.PhoneController;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.env.Vehicle;
import org.openbot.utils.PermissionUtils;
import timber.log.Timber;

public abstract class ControlsFragment extends Fragment {
  protected MainViewModel mViewModel;
  protected Vehicle vehicle;
  protected Animation startAnimation;
  protected SharedPreferencesManager preferencesManager;
  protected final PhoneController phoneController = new PhoneController();
  protected Enums.DriveMode currentDriveMode = Enums.DriveMode.GAME;
  protected GameController gameController = new GameController(currentDriveMode);

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    preferencesManager = new SharedPreferencesManager(requireContext());

    requireActivity()
        .getSupportFragmentManager()
        .setFragmentResultListener(
            Constants.GENERIC_MOTION_EVENT,
            this,
            (requestKey, result) -> {
              MotionEvent motionEvent = result.getParcelable(Constants.DATA);
              vehicle.setControl(gameController.processJoystickInput(motionEvent, -1));
              processControllerKeyData(Constants.CMD_DRIVE);
            });
    requireActivity()
        .getSupportFragmentManager()
        .setFragmentResultListener(
            Constants.KEY_EVENT,
            this,
            (requestKey, result) -> processKeyEvent(result.getParcelable(Constants.DATA)));

    mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

    vehicle = mViewModel.getVehicle().getValue();
    startAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink);

    mViewModel
        .getUsbData()
        .observe(
            getViewLifecycleOwner(),
            data -> {
              String[] itemList = data.split(",");
              if (itemList.length == 4) {
                if (Utils.isNumeric(itemList[0]))
                  vehicle.setBatteryVoltage(Float.parseFloat(itemList[0]));

                if (Utils.isNumeric(itemList[1]))
                  vehicle.setLeftWheelTicks(Float.parseFloat(itemList[1]));

                if (Utils.isNumeric(itemList[2]))
                  vehicle.setRightWheelTicks(Float.parseFloat(itemList[2]));

                if (Utils.isNumeric(itemList[3]))
                  vehicle.setSonarReading(Float.parseFloat(itemList[3]));

                processUSBData(data);
              }
            });

    handlePhoneControllerEvents();
  }

  private void processKeyEvent(KeyEvent keyCode) {
    if (Enums.ControlMode.getByID(preferencesManager.getControlMode())
        == Enums.ControlMode.GAMEPAD) {
      switch (keyCode.getKeyCode()) {
        case KeyEvent.KEYCODE_BUTTON_X: // square
          toggleIndicatorEvent(Enums.VehicleIndicator.LEFT.getValue());
          processControllerKeyData(Constants.CMD_INDICATOR_LEFT);
          break;
        case KeyEvent.KEYCODE_BUTTON_Y: // triangle
          toggleIndicatorEvent(Enums.VehicleIndicator.STOP.getValue());
          processControllerKeyData(Constants.CMD_INDICATOR_STOP);
          break;
        case KeyEvent.KEYCODE_BUTTON_B: // circle
          toggleIndicatorEvent(Enums.VehicleIndicator.RIGHT.getValue());
          processControllerKeyData(Constants.CMD_INDICATOR_RIGHT);
          break;

        case KeyEvent.KEYCODE_BUTTON_A: // x
          processControllerKeyData(Constants.CMD_LOGS);
          break;
        case KeyEvent.KEYCODE_BUTTON_START: // options
          processControllerKeyData(Constants.CMD_NOISE);
          break;
        case KeyEvent.KEYCODE_BUTTON_L1:
          processControllerKeyData(Constants.CMD_DRIVE_MODE);
          break;
        case KeyEvent.KEYCODE_BUTTON_R1:
          processControllerKeyData(Constants.CMD_NETWORK);
          break;
        case KeyEvent.KEYCODE_BUTTON_THUMBL:
          processControllerKeyData(Constants.CMD_SPEED_DOWN);
          break;
        case KeyEvent.KEYCODE_BUTTON_THUMBR:
          processControllerKeyData(Constants.CMD_SPEED_UP);
          break;

        default:
          break;
      }
    }
  }

  private void handlePhoneControllerEvents() {
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
                commandType = Constants.CMD_DRIVE;
              } else {
                Timber.d("Got invalid command from controller: %s", event.toString());
                return;
              }

              switch (commandType) {
                case Constants.CMD_DRIVE:
                  JSONObject driveValue = event.getJSONObject("driveCmd");
                  vehicle.setControl(
                      new Control(
                          Float.parseFloat(driveValue.getString("l")),
                          Float.parseFloat(driveValue.getString("r"))));
                  break;

                case Constants.CMD_INDICATOR_LEFT:
                  toggleIndicatorEvent(Enums.VehicleIndicator.LEFT.getValue());
                  break;

                case Constants.CMD_INDICATOR_RIGHT:
                  toggleIndicatorEvent(Enums.VehicleIndicator.RIGHT.getValue());
                  break;

                case Constants.CMD_INDICATOR_STOP:
                  toggleIndicatorEvent(Enums.VehicleIndicator.STOP.getValue());
                  break;

                  // We re connected to the controller, send back status info
                case Constants.CMD_CONNECTED:
                  // PhoneController class will receive this event and resent it to the controller.
                  // Other controllers can subscribe to this event as well.
                  // That is why we are not calling phoneController.send() here directly.
                  BotToControllerEventBus.emitEvent(
                      Utils.getStatus(
                          false,
                          false,
                          false,
                          currentDriveMode.toString(),
                          vehicle.getIndicator()));

                  break;
                case Constants.CMD_DISCONNECTED:
                  vehicle.setControl(0, 0);
                  break;
              }

              processControllerKeyData(commandType);
            });
  }

  private void toggleIndicatorEvent(int value) {
    vehicle.setIndicator(value);
    BotToControllerEventBus.emitEvent(Utils.createStatus("INDICATOR_LEFT", value == -1));
    BotToControllerEventBus.emitEvent(Utils.createStatus("INDICATOR_RIGHT", value == 1));
    BotToControllerEventBus.emitEvent(Utils.createStatus("INDICATOR_STOP", value == 0));
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
    vehicle.setControl(0, 0);
  }

  @Override
  public synchronized void onPause() {
    super.onPause();
    phoneController.disconnect(getContext());
  }

  protected abstract void processControllerKeyData(String command);

  protected abstract void processUSBData(String data);
}
