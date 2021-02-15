package org.openbot.logging;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
import org.openbot.env.ImageUtils;
import org.openbot.robot.CameraFragment;
import org.openbot.robot.SensorService;
import org.openbot.robot.ServerCommunication;
import org.openbot.tflite.Model;
import org.openbot.utils.PermissionUtils;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;
import timber.log.Timber;

public class LoggerFragment extends CameraFragment implements ServerCommunication.ServerListener {

  private FragmentLoggerBinding binding;
  private Handler handler;
  private HandlerThread handlerThread;
  private Intent intentSensorService;
  private ServerCommunication serverCommunication;
  protected String logFolder;

  protected boolean loggingEnabled;
  private Matrix frameToCropTransform;
  private Bitmap croppedBitmap;
  private int sensorOrientation;
  private RectF cropRect;
  private boolean maintainAspectRatio;

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
    binding.controllerContainer.speedInfo.setText(getString(R.string.speedInfo, "---,---"));

    intentSensorService = new Intent(requireActivity(), SensorService.class);
    setSpeedMode(Enums.SpeedMode.getByID(preferencesManager.getSpeedMode()));
    setControlMode(Enums.ControlMode.getByID(preferencesManager.getControlMode()));
    setDriveMode(Enums.DriveMode.getByID(preferencesManager.getDriveMode()));

    binding.controllerContainer.controlMode.setOnClickListener(
        v -> {
          Enums.ControlMode controlMode =
              Enums.ControlMode.getByID(preferencesManager.getControlMode());
          if (controlMode != null) setControlMode(Enums.switchControlMode(controlMode));
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

    binding.cameraToggle.setOnClickListener(v -> toggleCamera());

    binding.modelSpinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            updateCropImageInfo(selected);
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
    binding.resolutionSpinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
              case 0:
                setAnalyserResolution(Enums.Preview.FULL_HD.getValue());
                break;
              case 1:
                setAnalyserResolution(Enums.Preview.HD.getValue());
                break;
              case 2:
                setAnalyserResolution(Enums.Preview.SD.getValue());
                break;
            }
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
    BottomSheetBehavior.from(binding.loggerBottomSheet)
        .setState(BottomSheetBehavior.STATE_EXPANDED);
  }

  private void updateCropImageInfo(String selected) {
    frameToCropTransform = null;
    binding.cropInfo.setText(
        String.format(
            Locale.US,
            "%d x %d",
            Model.getCroppedImageSize(selected).getWidth(),
            Model.getCroppedImageSize(selected).getHeight()));

    croppedBitmap =
        Bitmap.createBitmap(
            Model.getCroppedImageSize(selected).getWidth(),
            Model.getCroppedImageSize(selected).getHeight(),
            Bitmap.Config.ARGB_8888);

    sensorOrientation = 90 - ImageUtils.getScreenOrientation(requireActivity());
    if (Model.fromId(selected) == Model.AUTOPILOT_F) {
      cropRect = new RectF(0.0f, 240.0f / 720.0f, 0.0f, 0.0f);
      maintainAspectRatio = true;
    } else {
      cropRect = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
      maintainAspectRatio = false;
    }
  }

  @Override
  public synchronized void onResume() {
    super.onResume();

    handlerThread = new HandlerThread("inference");
    handlerThread.start();
    handler = new Handler(handlerThread.getLooper());
    serverCommunication = new ServerCommunication(requireContext(), this);
    serverCommunication.start();
  }

  @Override
  public synchronized void onPause() {

    handlerThread.quitSafely();
    try {
      handlerThread.join();
      handlerThread = null;
      handler = null;
      serverCommunication.stop();
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
          Timber.d("SensorServiceConnection: connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
          sensorMessenger = null;
          Timber.d("SensorServiceConnection: disconnected");
        }
      };

  protected void sendFrameNumberToSensorService(long frameNumber) {
    if (sensorMessenger != null) {
      try {
        sensorMessenger.send(LogDataUtils.generateFrameNumberMessage(frameNumber));
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void sendInferenceTimeToSensorService(long frameNumber, long inferenceTime) {
    if (sensorMessenger != null) {
      try {
        sensorMessenger.send(LogDataUtils.generateInferenceTimeMessage(frameNumber, inferenceTime));
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void sendControlToSensorService() {
    if (sensorMessenger != null) {
      try {
        sensorMessenger.send(
            LogDataUtils.generateControlDataMessage(
                (int) vehicle.getLeftSpeed(), (int) vehicle.getRightSpeed()));
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void sendVehicleDataToSensorService(long timestamp, String data) {
    if (sensorMessenger != null) {
      try {
        sensorMessenger.send(LogDataUtils.generateVehicleDataMessage(timestamp, data));
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void sendIndicatorToSensorService() {
    if (sensorMessenger != null) {
      try {
        sensorMessenger.send(LogDataUtils.generateIndicatorMessage(vehicle.getIndicator()));
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
            // These two lines below are messy and may cause bugs. needs to be looked into
            ZipUtil.pack(folder, zip);
            FileUtils.deleteQuietly(folder);
            serverCommunication.upload(zip);
          } catch (InterruptedException e) {
            Timber.e(e, "Got interrupted.");
          }
        });
  }

  protected void setIsLoggingActive(boolean loggingActive) {
    if (loggingActive && !loggingEnabled) {
      if (!PermissionUtils.hasPermission(requireContext(), Constants.PERMISSION_CAMERA)
          && (binding.previewCheckBox.isChecked() || binding.trainingDataCheckBox.isChecked())) {
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

    binding.loggerSwitch.setChecked(loggingEnabled);
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
  protected void processControllerKeyData(String commandType) {
    switch (commandType) {
      case Constants.CMD_DRIVE:
        handleDriveCommand();
        break;

      case Constants.CMD_LOGS:
        handleLogging();
        break;
        //      case "Constants.CMD_NOISE":
        //        handleNoise();
        //        break;
      case Constants.CMD_INDICATOR_LEFT:
      case Constants.CMD_INDICATOR_RIGHT:
      case Constants.CMD_INDICATOR_STOP:
        sendIndicatorToSensorService();
        break;
      case Constants.CMD_DRIVE_MODE:
        setDriveMode(Enums.switchDriveMode(currentDriveMode));
        break;

      case Constants.CMD_DISCONNECTED:
        handleDriveCommand();
        setControlMode(Enums.ControlMode.GAMEPAD);
        break;

      case Constants.CMD_SPEED_DOWN:
        setSpeedMode(
            Enums.toggleSpeed(
                Enums.Direction.DOWN.getValue(),
                Enums.SpeedMode.getByID(preferencesManager.getSpeedMode())));
        break;
      case Constants.CMD_SPEED_UP:
        setSpeedMode(
            Enums.toggleSpeed(
                Enums.Direction.UP.getValue(),
                Enums.SpeedMode.getByID(preferencesManager.getSpeedMode())));
        break;
      case Constants.CMD_NETWORK:
        //        handleNetwork();
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
      phoneController.disconnect(getContext());
    }
    setDriveMode(Enums.DriveMode.getByID(preferencesManager.getDriveMode()));
    binding.controllerContainer.driveMode.setEnabled(true);
    binding.controllerContainer.driveMode.setAlpha(1.0f);
  }

  private long frameNum = 0;

  @Override
  protected void processFrame(Bitmap bitmap, ImageProxy image) {
    ++frameNum;
    if (binding != null) {
      requireActivity()
          .runOnUiThread(
              () ->
                  binding.frameInfo.setText(
                      String.format(Locale.US, "%d x %d", image.getWidth(), image.getHeight())));

      if (!binding.loggerSwitch.isChecked()) return;

      if (binding.previewCheckBox.isChecked() || binding.trainingDataCheckBox.isChecked()) {
        sendFrameNumberToSensorService(frameNum);
      }

      if (binding.previewCheckBox.isChecked()) {
        if (bitmap != null)
          ImageUtils.saveBitmap(
              bitmap, logFolder + File.separator + "images", frameNum + "_preview.jpeg");
      }
      if (binding.trainingDataCheckBox.isChecked()) {
        if (frameToCropTransform == null)
          frameToCropTransform =
              ImageUtils.getTransformationMatrix(
                  getMaxAnalyseImageSize().getWidth(),
                  getMaxAnalyseImageSize().getHeight(),
                  croppedBitmap.getWidth(),
                  croppedBitmap.getHeight(),
                  sensorOrientation,
                  cropRect,
                  maintainAspectRatio);

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(bitmap, frameToCropTransform, null);
        ImageUtils.saveBitmap(
            croppedBitmap, logFolder + File.separator + "images", frameNum + "_crop.jpeg");
      }
    }
  }

  @Override
  public void onConnectionEstablished(String ipAddress) {
    requireActivity().runOnUiThread(() -> binding.ipAddress.setText(ipAddress));
  }

  @Override
  public void onAddModel(String model) {}

  @Override
  public void onRemoveModel(String model) {}
}
