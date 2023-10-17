package org.openbot.logging;

import static java.lang.Math.abs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;
import androidx.navigation.Navigation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.CameraFragment;
import org.openbot.databinding.FragmentLoggerBinding;
import org.openbot.env.BotToControllerEventBus;
import org.openbot.env.ImageUtils;
import org.openbot.tflite.Model;
import org.openbot.tflite.Network;
import org.openbot.utils.ConnectionUtils;
import org.openbot.utils.Constants;
import org.openbot.utils.Enums;
import org.openbot.utils.FormatUtils;
import org.openbot.utils.PermissionUtils;
import org.openbot.vehicle.Control;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;
import timber.log.Timber;
import org.openbot.tflite.Autopilot;

public class LoggerFragment extends CameraFragment {

  private FragmentLoggerBinding binding;
  private Handler handler;
  private HandlerThread handlerThread;
  private Intent intentSensorService;
  protected String logFolder;

  protected boolean loggingEnabled;
  private boolean loggingCanceled;

  private Matrix frameToCropTransform;
  private Bitmap croppedBitmap;
  private int sensorOrientation;
  private RectF cropRect;
  private boolean maintainAspectRatio;

  private Model tfModel;
  private Autopilot autopilot;
  private boolean autonomousControlEnabled = false;

  private boolean autonomousStarted = true;

  private long reward = 4;


  @Override
  public View onCreateView(
      @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentLoggerBinding.inflate(inflater, container, false);
    if (!OpenCVLoader.initDebug()) {
      // OpenCV initialization failed, handle the error
      Log.e("OPENCV TEST", "OpenCV initialization failed");
    } else {
      // OpenCV initialized successfully, proceed with your code
      Log.d("OPENCV TEST", "OpenCV initialized successfully");
    }

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

    binding.sensorDataButton.setOnClickListener(
        v -> {
          SensorsDialog sensorsDialog = new SensorsDialog();
          sensorsDialog.show(getChildFragmentManager(), sensorsDialog.getTag());
        });
    binding.controllerContainer.controlMode.setOnClickListener(
        v -> {
          Enums.ControlMode controlMode =
              Enums.ControlMode.getByID(preferencesManager.getControlMode());
          if (controlMode != null) setControlMode(Enums.switchControlMode(controlMode));
        });
    binding.controllerContainer.driveMode.setOnClickListener(
        v -> setDriveMode(Enums.switchDriveMode(vehicle.getDriveMode())));

    binding.controllerContainer.speedMode.setOnClickListener(
        v ->
            setSpeedMode(
                Enums.toggleSpeed(
                    Enums.Direction.CYCLIC.getValue(),
                    Enums.SpeedMode.getByID(preferencesManager.getSpeedMode()))));

    binding.loggerSwitch.setOnCheckedChangeListener(
        (buttonView, isChecked) -> setLoggingActive(isChecked));

    binding.autonomousSwitch.setOnCheckedChangeListener(
            (buttonView, isChecked) -> {
              autonomousControlEnabled = isChecked;
            });



    binding.cameraToggle.setOnClickListener(v -> toggleCamera());

    List<String> models = getModelNames(f -> f.pathType != Model.PATH_TYPE.URL);
    initModelSpinner(binding.modelSpinner, models, "");
    initServerSpinner(binding.serverSpinner);

    binding.resolutionSpinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
              case 0:
                setAnalyserResolution(Enums.Preview.SD.getValue());
                break;
              case 1:
                setAnalyserResolution(Enums.Preview.HD.getValue());
                break;
              case 2:
                setAnalyserResolution(Enums.Preview.FULL_HD.getValue());
                break;
            }
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
    BottomSheetBehavior.from(binding.loggerBottomSheet)
        .setState(BottomSheetBehavior.STATE_EXPANDED);

    mViewModel
        .getUsbStatus()
        .observe(getViewLifecycleOwner(), status -> binding.usbToggle.setChecked(status));

    binding.usbToggle.setChecked(vehicle.isUsbConnected());

    binding.usbToggle.setOnClickListener(
        v -> {
          binding.usbToggle.setChecked(vehicle.isUsbConnected());
          Navigation.findNavController(requireView()).navigate(R.id.open_settings_fragment);
        });
  }
  private void stopAutonomousDriving() {

    // Close the autopilot if it was initialized
    vehicle.setControl((float)0, (float) 0);
    if (autopilot != null) {
      Log.e("AUTONOMOUS STOPPING", "AUTONOMOUS STOP SUCCESS !");
      autopilot.close();
      autopilot = null;
      // autonomousControlEnabled = false;
      //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         autonomousStarted = false;
    }
  }



  @Override
  protected void setModel(Model selected) {
    frameToCropTransform = null;
    binding.cropInfo.setText(
        String.format(
            Locale.US,
            "%d x %d",
            selected.getInputSize().getWidth(),
            selected.getInputSize().getHeight()));

    croppedBitmap =
        Bitmap.createBitmap(
            selected.getInputSize().getWidth(),
            selected.getInputSize().getHeight(),
            Bitmap.Config.ARGB_8888);

    sensorOrientation = 90 - ImageUtils.getScreenOrientation(requireActivity());
    if (selected.type == Model.TYPE.CMDNAV) {
      cropRect = new RectF(0.0f, 240.0f / 720.0f, 0.0f, 0.0f);
      maintainAspectRatio = true;
    } else {
      cropRect = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
      maintainAspectRatio = false;
    }
  }

  @Override
  public synchronized void onResume() {
    handlerThread = new HandlerThread("logging");
    handlerThread.start();
    handler = new Handler(handlerThread.getLooper());
    super.onResume();
  }

  @Override
  public synchronized void onPause() {
    handlerThread.quitSafely();
    try {
      handlerThread.join();
      handlerThread = null;
      handler = null;
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
    super.onPause();
  }

  protected synchronized void runInBackground(final Runnable r) {
    if (handler != null) {
      handler.post(r);
    }
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

  protected void sendRewardToSensorService(){
    if (sensorMessenger != null){
      try {
        sensorMessenger.send(LogDataUtils.generateRewardMessage(reward));
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  protected void sendVehicleDataToSensorService(long timestamp, String data, int type) {
    if (sensorMessenger != null) {
      try {
        sensorMessenger.send(LogDataUtils.generateVehicleDataMessage(timestamp, data, type));
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
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .getAbsolutePath()
            + File.separator
            + getString(R.string.app_name)
            + File.separator
            + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    intentSensorService.putExtra("logFolder", logFolder + File.separator + "sensor_data");
    requireActivity().startService(intentSensorService);
    requireActivity().bindService(intentSensorService, sensorConnection, Context.BIND_AUTO_CREATE);
    runInBackground(
        () -> {
          try {
            // Send current vehicle state to log
            TimeUnit.MILLISECONDS.sleep(500);
            sendControlToSensorService();
            sendIndicatorToSensorService();
            //sendRewardToSensorService(reward);

          } catch (InterruptedException e) {
            Timber.e(e, "Got interrupted.");
          }
        });
    if(autonomousControlEnabled){
      startAutonomousDriving();
    }
  }

  private void startAutonomousDriving() {
    try {
      // Initialize the autopilot if not already initialized
      if (autopilot == null) {
        tfModel = new Model(1, Model.CLASS.AUTOPILOT, Model.TYPE.CMDNAV,
                "CIL-Mobile-Cmd.tflite", Model.PATH_TYPE.ASSET, "networks/autopilot_float.tflite", "256x96");
        Network.Device device = Network.Device.CPU; // Set your desired device here
        int numThreads = 4; // Set the number of threads you want to use

        autopilot = new Autopilot(getActivity(), tfModel, device, numThreads);
        Log.e("AUTONOMOUS INIT", "Autopilot initialization succes: " );

      }
    } catch (Exception e) {
      Log.e("AUTONOMOUS INIT", "Autopilot initialization failed: " + e.getMessage());
      e.printStackTrace();
    }

    // Start continuous autopilot updates


    if (autopilot != null) {
      Timber.i("Running autopilot on image %s", frameNum);
      final long startTime = SystemClock.elapsedRealtime();
      handleDriveCommandAutonomous(autopilot.recognizeImage(croppedBitmap, vehicle.getIndicator()));

    }

  }

  private void handleDriveCommandAutonomous(Control control) {
    vehicle.setControl(control);
    float left = vehicle.getLeftSpeed();
    float right = vehicle.getRightSpeed();
    requireActivity()
            .runOnUiThread(
                    () ->
                            binding.controllerContainer.controlInfo.setText(
                                    String.format(Locale.US, "%.0f,%.0f", left, right)));

    runInBackground(this::sendControlToSensorService);

  }


  private void stopLogging(boolean isCancel) {
    if (sensorConnection != null) requireActivity().unbindService(sensorConnection);
    requireActivity().stopService(intentSensorService);

    // Pack and upload the collected data
    runInBackground(
        () -> {
          try {
            File folder = new File(logFolder);
            if (!isCancel) {
              // Zip the log folder and then upload it
              serverCommunication.upload(zip(folder));
            }
            TimeUnit.MILLISECONDS.sleep(500);
            FileUtils.deleteQuietly(folder);
          } catch (InterruptedException e) {
            Timber.e(e, "Got interrupted.");
          }
        });
    loggingEnabled = false;
    if (autonomousControlEnabled){
      stopAutonomousDriving();
    }

  }

  private File zip(File folder) {
    String zipFileName = folder + ".zip";
    File zip = new File(zipFileName);
    ZipUtil.pack(folder, zip);
    return zip;
  }

  private void cancelLogging() {
    loggingCanceled = true;
    setLoggingActive(false);
    audioPlayer.playFromString("Log deleted!");
  }

  protected void toggleLogging() {
    loggingCanceled = false;
    setLoggingActive(!loggingEnabled);
    audioPlayer.playLogging(voice, loggingEnabled);
  }

  protected void setLoggingActive(boolean enableLogging) {
    if (enableLogging && !loggingEnabled) {
      if (!PermissionUtils.hasLoggingPermissions(requireActivity())) {
        requestPermissionLauncherLogging.launch(Constants.PERMISSIONS_LOGGING);
        loggingEnabled = false;
      } else {
        startLogging();
        loggingEnabled = true;
      }
    } else if (!enableLogging && loggingEnabled) {
      stopLogging(loggingCanceled);
      loggingEnabled = false;
    }
    BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("LOGS", loggingEnabled));

    binding.loggerSwitch.setChecked(loggingEnabled);
  }

  private boolean allGranted = true;
  protected final ActivityResultLauncher<String[]> requestPermissionLauncherLogging =
      registerForActivityResult(
          new ActivityResultContracts.RequestMultiplePermissions(),
          result -> {
            result.forEach((permission, granted) -> allGranted = allGranted && granted);
            if (allGranted) setLoggingActive(true);
            else {
              PermissionUtils.showLoggingPermissionsToast(requireActivity());
            }
          });

  @Override
  protected void processUSBData(String data) {
    long timestamp = SystemClock.elapsedRealtimeNanos();
    char header = data.charAt(0);
    String body = data.substring(1);
    int type = -1;

    switch (header) {
      case 'v':
        if (FormatUtils.isNumeric(body)) {
          type = SensorService.MSG_VOLTAGE;
        }
        break;
      case 's':
        if (FormatUtils.isNumeric(body)) {
          type = SensorService.MSG_SONAR;
        }
        break;
      case 'w':
        type = SensorService.MSG_WHEELS;
        binding.controllerContainer.speedInfo.setText(
            getString(
                R.string.speedInfo,
                String.format(
                    Locale.US,
                    "%3.0f,%3.0f",
                    vehicle.getLeftWheelRpm(),
                    vehicle.getRightWheelRpm())));
        break;
      case 'b':
        type = SensorService.MSG_BUMPER;
        break;
    }

    if (type > 0) sendVehicleDataToSensorService(timestamp, body, type);
  }

  @Override
  protected void processControllerKeyData(String commandType) {
    switch (commandType) {
      case Constants.CMD_DRIVE:
        handleDriveCommand();
        break;

      case Constants.CMD_LOGS:
        toggleLogging();
        break;

      case Constants.CMD_INDICATOR_LEFT:
        changeRewardNegative();
        break;

      case Constants.CMD_INDICATOR_RIGHT:
        changeRewardPositive();
        break;

      case Constants.CMD_INDICATOR_STOP:
        sendIndicatorToSensorService();
        break;
      case Constants.CMD_DRIVE_MODE:
        setDriveMode(Enums.switchDriveMode(vehicle.getDriveMode()));
        break;

      case Constants.CMD_DISCONNECTED:
        handleDriveCommand();
        setControlMode(Enums.ControlMode.GAMEPAD);
        break;

      case Constants.CMD_SPEED_DOWN:
        /*setSpeedMode(
            Enums.toggleSpeed(
                Enums.Direction.DOWN.getValue(),
                Enums.SpeedMode.getByID(preferencesManager.getSpeedMode())));*/
        //changeRewardPositive();
        break;
      case Constants.CMD_SPEED_UP:
        /*setSpeedMode(
            Enums.toggleSpeed(
                Enums.Direction.UP.getValue(),
                Enums.SpeedMode.getByID(preferencesManager.getSpeedMode())));*/
        //changeRewardNegative();
        break;
      case Constants.CMD_NETWORK:
        cancelLogging();
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
          if (!PermissionUtils.hasControllerPermissions(requireActivity()))
            requestPermissionLauncher.launch(Constants.PERMISSIONS_CONTROLLER);
          else connectPhoneController();
          break;
      }
      Timber.d("Updating  controlMode: %s", controlMode);
      preferencesManager.setControlMode(controlMode.getValue());
    }
  }

  protected void setDriveMode(Enums.DriveMode driveMode) {
    if (driveMode != null) {
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
      vehicle.setDriveMode(driveMode);
      preferencesManager.setDriveMode(driveMode.getValue());
    }
  }

  private void connectPhoneController() {
    phoneController.connect(requireContext());
    Enums.DriveMode oldDriveMode = currentDriveMode;
    // Currently only dual drive mode supported
    setDriveMode(Enums.DriveMode.DUAL);
    binding.controllerContainer.driveMode.setAlpha(0.5f);
    binding.controllerContainer.driveMode.setEnabled(false);
    preferencesManager.setDriveMode(oldDriveMode.getValue());
  }

  private void disconnectPhoneController() {
    phoneController.disconnect();
    setDriveMode(Enums.DriveMode.getByID(preferencesManager.getDriveMode()));
    binding.controllerContainer.driveMode.setEnabled(true);
    binding.controllerContainer.driveMode.setAlpha(1.0f);
  }

  private long frameNum = 0;

  @Override
  protected void processFrame(Bitmap bitmap, ImageProxy image) {
    ++frameNum;
    if (binding != null) {
      if (isAdded())
        requireActivity()
            .runOnUiThread(
                () ->
                    binding.frameInfo.setText(
                        String.format(Locale.US, "%d x %d", image.getWidth(), image.getHeight())));

      if (!binding.loggerSwitch.isChecked()) return;
      if (autonomousControlEnabled)
      {

        if (croppedBitmap != null) {
          processFrameForAutonomous(croppedBitmap);
          Log.e("AutonomousProcessing", "Received a frameBitmap.");

        } else {
          Log.e("AutonomousProcessing", "Received a null frameBitmap.");
        }
      }

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
      // Apply OpenCV processing to the same bitmap and save it
      Bitmap opencvProcessedBitmap = applyOpenCVProcessing(croppedBitmap);
      if (opencvProcessedBitmap != null) {
        final Canvas canvas2 = new Canvas(croppedBitmap);
        canvas2.drawBitmap(opencvProcessedBitmap, frameToCropTransform, null);
        ImageUtils.saveBitmap(
                opencvProcessedBitmap, logFolder + File.separator + "opencv_images", frameNum + "_opencv.jpeg");
      }
    }
  }
  private void processFrameForAutonomous(Bitmap frameBitmap) {
    // Ensure that the autopilot is initialized
    if (autopilot != null) {
      // Perform image recognition and get control commands
      Bitmap resizedBitmap;
      resizedBitmap = cropBitmap(frameBitmap,(int) 256, (int)96);
      Control control = autopilot.recognizeImage(frameBitmap, vehicle.getIndicator());

      // Handle the control commands (e.g., update vehicle control)
      handleDriveCommandAutonomous(control);
    }
  }
  public Bitmap cropBitmap(Bitmap originalBitmap, int cropWidth, int cropHeight) {
    int originalWidth = originalBitmap.getWidth();
    int originalHeight = originalBitmap.getHeight();

    // Calculate the coordinates for the top-left corner of the cropped region
    int left = (originalWidth - cropWidth) / 2;
    int top = (originalHeight - cropHeight) / 2;

    // Create the cropped Bitmap
    Bitmap croppedBitmap = Bitmap.createBitmap(originalBitmap, left, top, cropWidth, cropHeight);

    return croppedBitmap;
  }
  private void changeRewardNegative(){
    reward = - 30;
    sendRewardToSensorService();
  }

  private void changeRewardPositive(){
    reward =  30;
    sendRewardToSensorService();
  }

  private void changeRewardDistance(double distance){
    if (abs(distance) < 300) {
      reward =  100;
    } else if (abs(distance) > 400) {
      reward =  - 10;
    } else if (abs(distance) < 400 ){
      reward =  10;

    } else {
      reward = 2;
    }
    sendRewardToSensorService();
  }
 /* private void saveRewardToFile( String folderPath, long frameNumber) {
    File folder = new File(folderPath + File.separator + "reward");

    if (!folder.exists()) {
      folder.mkdirs();
    }

    String fileName = "reward_" + frameNumber + ".txt";
    File file = new File(folder, fileName);

    try {
      FileWriter writer = new FileWriter(file);
      writer.write("Frame Number: " + frameNumber + "\n");
      writer.write("Reward: " + reward + "\n");
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }*/

  public Point calculateCentroid(List<Point> contour) {
    double sumX = 0.0;
    double sumY = 0.0;

    // Calculate the sum of x and y coordinates of all points
    for (Point point : contour) {
      sumX += point.x;
      sumY += point.y;
    }

    // Calculate the mean (centroid) by dividing the sums by the number of points
    double centerX = sumX / contour.size();
    double centerY = sumY / contour.size();

    return new Point(centerX, centerY);
  }

  private double findClosestCentroid(List<Point> centroids, Point targetPoint) {
    double minDistance = Double.MAX_VALUE;
    Point closestCentroid = null;

    for (Point centroid : centroids) {
      double distance = calculateDistance(centroid, targetPoint);
      if (distance < minDistance) {
        minDistance = distance;
        closestCentroid = centroid;
      }
    }

    return minDistance;
  }

  private double calculateDistance(Point point1, Point point2) {
    double dx = point1.x - point2.x;

    return dx;
  }

  private Bitmap applyOpenCVProcessing(Bitmap inputImage) {
    Mat inputMat = new Mat(inputImage.getHeight(), inputImage.getWidth(), CvType.CV_8UC4);
    Utils.bitmapToMat(inputImage, inputMat);

    // Apply image processing operations (e.g., resize and convert to grayscale)
    // Size newSize = new Size(300, 300);
    // Imgproc.resize(inputMat, inputMat, newSize);
    Imgproc.cvtColor(inputMat, inputMat, Imgproc.COLOR_RGBA2GRAY);

    Mat rotatedMat = new Mat();

// Transpose the image (swap rows and columns)
    //Core.transpose(inputMat, rotatedMat);

// Flip the transposed image horizontally (180 degrees rotation)
   // Core.flip(rotatedMat, rotatedMat, 1);

    Scalar lowerWhite = new Scalar(200, 170, 170);
    Scalar higherWhite = new Scalar(254, 254, 254);

    double contrastFactor = 1.2; // Increase contrast by 50%
// Scale and convert the image data type
    Mat contrastedMat = new Mat();
    inputMat.convertTo(contrastedMat, -1, contrastFactor, -40);
// Ensure pixel values are within the valid range
    Core.normalize(contrastedMat, contrastedMat, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);

    Mat mask = new Mat();
    Core.inRange(contrastedMat, lowerWhite, higherWhite, mask);

    Mat blur = new Mat();
    Imgproc.medianBlur(mask, blur, 9);
    Mat edges = new Mat();
    Imgproc.Canny(blur, edges, 100, 150);

    Mat bottom = regionOfInterest(edges);
    Mat thresholdMat = new Mat();
    Imgproc.adaptiveThreshold(bottom, thresholdMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2);

    Mat hierarchy = new Mat(); // Not used in this case
    List<MatOfPoint> contours = new ArrayList<>();
    Imgproc.findContours(bottom, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
    List<Point> centroids = new ArrayList<>();
    for (MatOfPoint contour : contours) {
      Point centroid = calculateCentroid(contour.toList());
      centroids.add(centroid);
    }

    double distance = findClosestCentroid(centroids, new Point(250, 310));
    if (distance != Double.MAX_VALUE) {
      // Save the centroid to a TXT file
      changeRewardDistance(distance);
    }
    Bitmap processedBitmap = Bitmap.createBitmap(bottom.cols(), bottom.rows(), Bitmap.Config.ARGB_8888);
    Utils.matToBitmap(bottom, processedBitmap);

    return processedBitmap;
  }
  public static Mat regionOfInterest(Mat inputMat){

    int width = inputMat.cols();
    int height = inputMat.rows();

    // Define the ROI as the bottom half of the image
    Point[] roiPoints = new Point[4];
    roiPoints[0] = new Point(width * 0.0, height * 0.8); // Top-left corner of ROI
    roiPoints[1] = new Point(width, height * 0.8);        // Top-right corner of ROI
    roiPoints[2] = new Point(width, height);              // Bottom-right corner of ROI
    roiPoints[3] = new Point(0, height);
    MatOfPoint roiContour = new MatOfPoint(roiPoints);
    Mat mask = Mat.zeros(inputMat.size(), CvType.CV_8U);
    List<MatOfPoint> roiContours = new ArrayList<>();
    roiContours.add(roiContour);
    Imgproc.fillPoly(mask, roiContours, new Scalar(255));
    Mat resultImage = new Mat();
    Core.bitwise_and(inputMat, inputMat, resultImage, mask);



    return resultImage;
  }
  @Override
  public void onConnectionEstablished(String ipAddress) {
    requireActivity().runOnUiThread(() -> binding.ipAddress.setText(ipAddress));
  }
}
