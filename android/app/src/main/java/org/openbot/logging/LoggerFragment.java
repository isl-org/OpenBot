package org.openbot.logging;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.Constants;
import org.openbot.common.Enums;
import org.openbot.common.Utils;
import org.openbot.databinding.FragmentLoggerBinding;
import org.openbot.env.BotToControllerEventBus;
import org.openbot.robot.CameraFragment;
import org.openbot.robot.SensorService;
import org.openbot.robot.ServerService;
import org.openbot.utils.PermissionUtils;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;
import timber.log.Timber;

public class LoggerFragment extends CameraFragment implements ServerService.ServerListener {

  private FragmentLoggerBinding binding;
  private Handler handler;
  private HandlerThread handlerThread;
  private Intent intentSensorService;
  private ServerService serverService;
  protected String logFolder;
  protected boolean loggingEnabled;
  protected Enums.LogMode logMode = Enums.LogMode.CROP_IMG;

  @Override
  public View onCreateView(
      @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentLoggerBinding.inflate(inflater, container, false);

    return inflateFragment(binding, inflater, container);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    intentSensorService = new Intent(requireActivity(), SensorService.class);

    setSpeedMode(Enums.SpeedMode.getByID(preferencesManager.getSpeedMode()));
    setControlMode(Enums.ControlMode.getByID(preferencesManager.getControlMode()));
    setDriveMode(Enums.DriveMode.getByID(preferencesManager.getDriveMode()));

    binding.controllerContainer.controlMode.setOnClickListener(
        v -> {
          Enums.ControlMode controlMode =
              Enums.ControlMode.getByID(preferencesManager.getControlMode());
          if (controlMode != null)
            setControlMode(Enums.switchControlMode(controlMode));
        });
    binding.controllerContainer.driveMode.setOnClickListener(
        v -> setDriveMode(Enums.switchDriveMode(currentDriveMode)));

    binding.controllerContainer.speedMode.setOnClickListener(
        v ->
            setSpeedMode(
                Enums.toggleSpeed(
                    Enums.Direction.CYCLIC.getValue(),
                    Enums.SpeedMode.getByID(preferencesManager.getSpeedMode()))));

    binding.loggerSwitch.setOnCheckedChangeListener(
        (buttonView, isChecked) -> setIsLoggingActive(isChecked));

    binding.logSpinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Enums.LogMode mode =
                Enums.LogMode.valueOf(parent.getItemAtPosition(position).toString().toUpperCase());
            if (logMode != mode) {
              logMode = mode;
              preferencesManager.setLogMode(mode.ordinal());
            }
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

    binding.cameraToggleSwitch.setOnCheckedChangeListener(
        (buttonView, isChecked) -> toggleCamera());
  }

  @Override
  public synchronized void onResume() {
    super.onResume();

    handlerThread = new HandlerThread("inference");
    handlerThread.start();
    handler = new Handler(handlerThread.getLooper());
    serverService = new ServerService(requireContext(), this);
    serverService.start();
  }

  @Override
  public synchronized void onPause() {

    handlerThread.quitSafely();
    try {
      handlerThread.join();
      handlerThread = null;
      handler = null;
      serverService.stop();
    } catch (final InterruptedException e) {
    }

    super.onPause();
  }

  Messenger sensorMessenger;

  ServiceConnection sensorConnection =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
          sensorMessenger = new Messenger(binder);
          Log.d("SensorServiceConnection", "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
          sensorMessenger = null;
          Log.d("SensorServiceConnection", "disconnected");
        }
      };

  protected void sendFrameNumberToSensorService(long frameNumber) {
    if (sensorMessenger != null) {
      Message msg = Message.obtain();
      Bundle bundle = new Bundle();
      bundle.putLong("frameNumber", frameNumber);
      bundle.putLong("timestamp", SystemClock.elapsedRealtimeNanos());
      msg.setData(bundle);
      msg.what = SensorService.MSG_FRAME;
      try {
        sensorMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void sendInferenceTimeToSensorService(long frameNumber, long inferenceTime) {
    if (sensorMessenger != null) {
      Message msg = Message.obtain();
      Bundle bundle = new Bundle();
      bundle.putLong("frameNumber", frameNumber);
      bundle.putLong("inferenceTime", inferenceTime);
      msg.setData(bundle);
      msg.what = SensorService.MSG_INFERENCE;
      try {
        sensorMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void sendControlToSensorService() {
    if (sensorMessenger != null) {
      Message msg = Message.obtain();
      msg.arg1 = (int) (vehicle.getLeftSpeed());
      msg.arg2 = (int) (vehicle.getRightSpeed());
      msg.what = SensorService.MSG_CONTROL;
      try {
        sensorMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void sendVehicleDataToSensorService(long timestamp, String data) {
    if (sensorMessenger != null) {
      Message msg = Message.obtain();
      Bundle bundle = new Bundle();
      bundle.putLong("timestamp", timestamp);
      bundle.putString("data", data);
      msg.setData(bundle);
      msg.what = SensorService.MSG_VEHICLE;
      try {
        sensorMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void sendIndicatorToSensorService() {
    if (sensorMessenger != null) {
      Message msg = Message.obtain();
      msg.arg1 = vehicle.getIndicator();
      msg.what = SensorService.MSG_INDICATOR;
      try {
        sensorMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  private void startLogging() {
    logFolder =
        Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + getString(R.string.app_name)
            + File.separator
            + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    intentSensorService.putExtra("logFolder", logFolder + File.separator + "sensor_data");
    requireActivity().startService(intentSensorService);
    requireActivity().bindService(intentSensorService, sensorConnection, Context.BIND_AUTO_CREATE);
    // Send current vehicle state to log
    runInBackground(
        () -> {
          try {
            TimeUnit.MILLISECONDS.sleep(500);
            sendControlToSensorService();
            sendIndicatorToSensorService();
          } catch (InterruptedException e) {
            Timber.e(e, "Got interrupted.");
          }
        });
  }

  private void stopLogging() {
    if (sensorConnection != null) requireActivity().unbindService(sensorConnection);
    requireActivity().stopService(intentSensorService);

    // Pack and upload the collected data
    runInBackground(
        () -> {
          String logZipFile = logFolder + ".zip";
          // Zip the log folder and then delete it
          File folder = new File(logFolder);
          File zip = new File(logZipFile);
          try {
            TimeUnit.MILLISECONDS.sleep(500);
            ZipUtil.pack(folder, zip);
            FileUtils.deleteQuietly(folder);
            serverService.upload(zip);
          } catch (InterruptedException e) {
            Timber.e(e, "Got interrupted.");
          }
        });
  }

  protected void setIsLoggingActive(boolean loggingActive) {
    if (loggingActive && !loggingEnabled) {
      if (!PermissionUtils.hasPermission(requireContext(), Constants.PERMISSION_CAMERA)
          && logMode != Enums.LogMode.ONLY_SENSORS) {
        PermissionUtils.requestCameraPermission(this);
        loggingEnabled = false;
      } else if (!PermissionUtils.hasPermission(requireContext(), Constants.PERMISSION_LOCATION)) {
        PermissionUtils.requestLocationPermissionLogging(this);
        loggingEnabled = false;
      } else if (!PermissionUtils.hasPermission(requireContext(), Constants.PERMISSION_STORAGE)) {
        PermissionUtils.requestStoragePermission(this);
        loggingEnabled = false;
      } else {
        startLogging();
        loggingEnabled = true;
      }
    } else if (!loggingActive && loggingEnabled) {
      stopLogging();
      loggingEnabled = false;
    }
    BotToControllerEventBus.emitEvent(Utils.createStatus("LOGS", loggingEnabled));

    binding.logSpinner.setEnabled(!loggingEnabled);
    if (loggingEnabled) binding.logSpinner.setAlpha(0.5f);
    else binding.logSpinner.setAlpha(1.0f);
    binding.loggerSwitch.setChecked(loggingEnabled);
    if (loggingEnabled) binding.loggerSwitch.setText(R.string.logging);
    else binding.loggerSwitch.setText(R.string.not_logging);
  }

  protected synchronized void runInBackground(final Runnable r) {
    if (handler != null) {
      handler.post(r);
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case Constants.REQUEST_LOCATION_PERMISSION_LOGGING:
        // If the permission is granted, start logging,
        // otherwise, show a Toast
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setIsLoggingActive(true);
        } else {
          if (PermissionUtils.shouldShowRational(
              requireActivity(), Constants.PERMISSION_LOCATION)) {
            Toast.makeText(
                    requireContext().getApplicationContext(),
                    R.string.location_permission_denied_logging,
                    Toast.LENGTH_LONG)
                .show();
          }
        }
        break;
      case Constants.REQUEST_STORAGE_PERMISSION:
        // If the permission is granted, start logging,
        // otherwise, show a Toast
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setIsLoggingActive(true);
        } else {
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_STORAGE)) {
            Toast.makeText(
                    requireContext().getApplicationContext(),
                    R.string.storage_permission_denied,
                    Toast.LENGTH_LONG)
                .show();
          }
        }
        break;
    }
  }

  protected void handleLogging() {
    setIsLoggingActive(!loggingEnabled);
    //    audioPlayer.playLogging(voice, loggingEnabled);
  }

  @Override
  protected void processUSBData(String data) {
    sendVehicleDataToSensorService(SystemClock.elapsedRealtimeNanos(), data);
  }

  @Override
  public void onMotionEvent(MotionEvent motionEvent) {
    handleDriveCommand();
  }

  @Override
  protected void onKeyEvent(KeyEvent keyCode) {
    switch (keyCode.getKeyCode()) {
      case KeyEvent.KEYCODE_BUTTON_A: // x
        handleLogging();
        break;
      case KeyEvent.KEYCODE_BUTTON_START: // options
        //            handleNoise();
        break;
      case KeyEvent.KEYCODE_BUTTON_X: // square
      case KeyEvent.KEYCODE_BUTTON_Y: // triangle
      case KeyEvent.KEYCODE_BUTTON_B: // circle
        sendIndicatorToSensorService();
        break;
      case KeyEvent.KEYCODE_BUTTON_L1:
        setDriveMode(Enums.switchDriveMode(currentDriveMode));
        break;
      case KeyEvent.KEYCODE_BUTTON_R1:
        //            handleNetwork();
        break;
      case KeyEvent.KEYCODE_BUTTON_THUMBL:
        setSpeedMode(
            Enums.toggleSpeed(
                Enums.Direction.DOWN.getValue(),
                Enums.SpeedMode.getByID(preferencesManager.getSpeedMode())));
        break;
      case KeyEvent.KEYCODE_BUTTON_THUMBR:
        setSpeedMode(
            Enums.toggleSpeed(
                Enums.Direction.UP.getValue(),
                Enums.SpeedMode.getByID(preferencesManager.getSpeedMode())));
        break;
    }
  }

  @Override
  protected void processPhoneControllerData(String commandType) {
    switch (commandType) {
      case Constants.CMD_LOGS:
        handleLogging();
        break;
        //      case "NOISE":
        //        handleNoise();
        //        break;
      case Constants.CMD_INDICATOR_LEFT:
      case Constants.CMD_INDICATOR_RIGHT:
      case Constants.CMD_INDICATOR_STOP:
        sendIndicatorToSensorService();
        break;
      case Constants.CMD_DRIVE:
        handleDriveCommand();
        break;
      case Constants.CMD_DRIVE_MODE:
        setDriveMode(Enums.switchDriveMode(currentDriveMode));
        break;

      case Constants.CMD_DISCONNECTED:
        handleDriveCommand();
        setControlMode(Enums.ControlMode.GAMEPAD);
        break;
    }
  }

  protected void handleDriveCommand() {
    float left = vehicle.getLeftSpeed();
    float right = vehicle.getRightSpeed();
    binding.controllerContainer.controlInfo.setText(
        String.format(Locale.US, "%.0f,%.0f", left, right));
    runInBackground(this::sendControlToSensorService);
  }

  private void setSpeedMode(Enums.SpeedMode speedMode) {
    if (speedMode != null) {
      switch (speedMode) {
        case SLOW:
          binding.controllerContainer.speedMode.setImageResource(R.drawable.ic_speed_low);
          break;
        case NORMAL:
          binding.controllerContainer.speedMode.setImageResource(R.drawable.ic_speed_medium);
          break;
        case FAST:
          binding.controllerContainer.speedMode.setImageResource(R.drawable.ic_speed_high);
          break;
      }

      Timber.d("Updating  controlSpeed: %s", speedMode);
      preferencesManager.setSpeedMode(speedMode.getValue());
      vehicle.setSpeedMultiplier(speedMode.getValue());
    }
  }

  private void setControlMode(Enums.ControlMode controlMode) {
    if (controlMode != null) {
      switch (controlMode) {
        case GAMEPAD:
          binding.controllerContainer.controlMode.setImageResource(R.drawable.ic_controller);
          disconnectPhoneController();
          break;
        case PHONE:
          binding.controllerContainer.controlMode.setImageResource(R.drawable.ic_phone);
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

  protected void setDriveMode(Enums.DriveMode driveMode) {
    if (this.currentDriveMode != driveMode && driveMode != null) {
      switch (driveMode) {
        case DUAL:
          binding.controllerContainer.driveMode.setImageResource(R.drawable.ic_dual);
          break;
        case GAME:
          binding.controllerContainer.driveMode.setImageResource(R.drawable.ic_game);
          break;
        case JOYSTICK:
          binding.controllerContainer.driveMode.setImageResource(R.drawable.ic_joystick);
          break;
      }

      Timber.d("Updating  driveMode: %s", driveMode);
      this.currentDriveMode = driveMode;
      preferencesManager.setDriveMode(driveMode.getValue());
      gameController.setDriveMode(driveMode);
    }
  }

  private void connectPhoneController() {
    if (!phoneController.isConnected()) {
      phoneController.connect(requireContext());
    }
    Enums.DriveMode oldDriveMode = currentDriveMode;
    // Currently only dual drive mode supported
    setDriveMode(Enums.DriveMode.DUAL);
    binding.controllerContainer.driveMode.setAlpha(0.5f);
    binding.controllerContainer.driveMode.setEnabled(false);
    preferencesManager.setDriveMode(oldDriveMode.getValue());
  }

  private void disconnectPhoneController() {
    if (phoneController.isConnected()) {
      phoneController.disconnect();
    }
    setDriveMode(Enums.DriveMode.getByID(preferencesManager.getDriveMode()));
    binding.controllerContainer.driveMode.setEnabled(true);
    binding.controllerContainer.driveMode.setAlpha(1.0f);
  }

  @Override
  protected void processFrame(ImageProxy image) {

    requireActivity()
        .runOnUiThread(
            () -> {
              binding.frameInfo.setText(
                  String.format(
                      Locale.US,
                      "%d x %d",
                      getPreviewSize().getWidth(),
                      getPreviewSize().getHeight()));
              binding.cropInfo.setText(
                  String.format(Locale.US, "%d x %d", image.getWidth(), image.getHeight()));
            });
  }

  @Override
  public void onAddModel(String model) {}

  @Override
  public void onRemoveModel(String model) {}
}
