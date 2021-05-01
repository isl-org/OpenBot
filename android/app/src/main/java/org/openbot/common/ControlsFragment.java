package org.openbot.common;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;
import org.json.JSONObject;
import org.openbot.R;
import org.openbot.env.AudioPlayer;
import org.openbot.env.BotToControllerEventBus;
import org.openbot.env.Control;
import org.openbot.env.ControllerToBotEventBus;
import org.openbot.env.PhoneController;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.env.Vehicle;
import org.openbot.main.MainViewModel;
import org.openbot.tflite.Model;
import org.openbot.utils.ConnectionUtils;
import org.openbot.utils.Constants;
import org.openbot.utils.Enums;
import org.openbot.utils.FileUtils;
import org.openbot.utils.FormatUtils;
import org.openbot.utils.PermissionUtils;
import timber.log.Timber;

public abstract class ControlsFragment extends Fragment {
  protected MainViewModel mViewModel;
  protected Vehicle vehicle;
  protected Animation startAnimation;
  protected SharedPreferencesManager preferencesManager;
  protected PhoneController phoneController;
  protected Enums.DriveMode currentDriveMode = Enums.DriveMode.GAME;

  protected AudioPlayer audioPlayer;

  protected final String voice = "matthew";
  protected List<Model> masterList;

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    requireActivity()
        .getWindow()
        .addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    phoneController = PhoneController.getInstance(requireContext());

    preferencesManager = new SharedPreferencesManager(requireContext());
    audioPlayer = new AudioPlayer(requireContext());
    masterList = FileUtils.loadConfigJSONFromAsset(requireActivity());

    requireActivity()
        .getSupportFragmentManager()
        .setFragmentResultListener(
            Constants.GENERIC_MOTION_EVENT,
            this,
            (requestKey, result) -> {
              MotionEvent motionEvent = result.getParcelable(Constants.DATA);
              vehicle.setControl(vehicle.getGameController().processJoystickInput(motionEvent, -1));
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
                if (FormatUtils.isNumeric(itemList[0]))
                  vehicle.setBatteryVoltage(Float.parseFloat(itemList[0]));

                if (FormatUtils.isNumeric(itemList[1]))
                  vehicle.setLeftWheelTicks(Float.parseFloat(itemList[1]));

                if (FormatUtils.isNumeric(itemList[2]))
                  vehicle.setRightWheelTicks(Float.parseFloat(itemList[2]));

                if (FormatUtils.isNumeric(itemList[3]))
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
          toggleNoise();
          processControllerKeyData(Constants.CMD_NOISE);
          break;
        case KeyEvent.KEYCODE_BUTTON_L1:
          processControllerKeyData(Constants.CMD_DRIVE_MODE);
          audioPlayer.playDriveMode(voice, vehicle.getDriveMode());
          break;
        case KeyEvent.KEYCODE_BUTTON_R1:
          processControllerKeyData(Constants.CMD_NETWORK);
          break;
        case KeyEvent.KEYCODE_BUTTON_THUMBL:
          processControllerKeyData(Constants.CMD_SPEED_DOWN);
          audioPlayer.playSpeedMode(
              voice, Enums.SpeedMode.getByID(preferencesManager.getSpeedMode()));
          break;
        case KeyEvent.KEYCODE_BUTTON_THUMBR:
          processControllerKeyData(Constants.CMD_SPEED_UP);
          audioPlayer.playSpeedMode(
              voice, Enums.SpeedMode.getByID(preferencesManager.getSpeedMode()));
          break;

        default:
          break;
      }
    }
  }

  private void handlePhoneControllerEvents() {
    ControllerToBotEventBus.subscribe(
        this.getClass().getSimpleName(),
        event -> {
          String commandType;
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
              // PhoneController class will receive this event and resent it to the
              // controller.
              // Other controllers can subscribe to this event as well.
              // That is why we are not calling phoneController.send() here directly.
              BotToControllerEventBus.emitEvent(
                  ConnectionUtils.getStatus(
                      false, false, false, currentDriveMode.toString(), vehicle.getIndicator()));
              break;

            case Constants.CMD_DISCONNECTED:
              vehicle.setControl(0, 0);
              break;
          }

          processControllerKeyData(commandType);
        },
        error -> {
          Log.d(null, "Error occurred in ControllerToBotEventBus: " + error);
        });
  }

  protected void toggleNoise() {
    vehicle.toggleNoise();
    BotToControllerEventBus.emitEvent(
        ConnectionUtils.createStatus("NOISE", vehicle.isNoiseEnabled()));
    audioPlayer.playNoise(voice, vehicle.isNoiseEnabled());
  }

  private void toggleIndicatorEvent(int value) {
    vehicle.setIndicator(value);
    BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("INDICATOR_LEFT", value == -1));
    BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("INDICATOR_RIGHT", value == 1));
    BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("INDICATOR_STOP", value == 0));
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case Constants.REQUEST_CONTROLLER_PERMISSIONS:
        // If the permission is granted, start advertising to controller,
        // otherwise, show a Toast
        if (grantResults.length > 1
            && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
          phoneController.connect(requireContext());
        } else {
          if (PermissionUtils.shouldShowRational(
              requireActivity(), Constants.PERMISSION_LOCATION)) {
            Toast.makeText(
                    requireActivity().getApplicationContext(),
                    R.string.location_permission_denied_controller,
                    Toast.LENGTH_LONG)
                .show();
          }
          if (PermissionUtils.shouldShowRational(
              requireActivity(), Constants.PERMISSION_AUDIO_RECORDING)) {
            Toast.makeText(
                    requireActivity().getApplicationContext(),
                    R.string.record_audio_permission_denied_controller,
                    Toast.LENGTH_LONG)
                .show();
          }
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_CAMERA)) {
            Toast.makeText(
                    requireActivity().getApplicationContext(),
                    R.string.camera_permission_denied_controller,
                    Toast.LENGTH_LONG)
                .show();
          }
          break;
        }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onDestroy() {
    Timber.d("onDestroy");
    ControllerToBotEventBus.unsubscribe(this.getClass().getSimpleName());
    vehicle.setControl(0, 0);
    super.onDestroy();
  }

  @Override
  public synchronized void onPause() {
    Timber.d("onPause");
    vehicle.setControl(0, 0);
    super.onPause();
  }

  @Override
  public void onStop() {
    Timber.d("onStop");
    super.onStop();
  }

  protected abstract void processControllerKeyData(String command);

  protected abstract void processUSBData(String data);
}
