package org.openbot.logging;
/*
The original code is from the LoggerFragment, but some news implementations were made here to include a reward, and the possibility for a model to run while gathering data.
The author of the modifications: Lilou Gras, 2023

 */

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
import org.openbot.databinding.FragmentLoggerRlBinding;
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

public class LoggerFragmentRL extends CameraFragment {

    private FragmentLoggerRlBinding binding;
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


    private long reward = 4;


    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoggerRlBinding.inflate(inflater, container, false);

        // This is used to initialized OpenCV
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

        // The button was added to toggle autonomous driving
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

    // This function stops Autonomous Driving when the data loading stops. It also forces the robot to halt.
    private void stopAutonomousDriving() {

        // Close the autopilot if it was initialized
        vehicle.setControl((float)0, (float) 0);
        if (autopilot != null) {
            Log.e("AUTONOMOUS STOPPING", "AUTONOMOUS STOP SUCCESS !");
            autopilot.close();
            autopilot = null;
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

    // This is used to send the computed reward to SensorService that will then upload it to the Python server once the data logging is complete
    protected void sendRewardToSensorService() {
        if (sensorMessenger != null) {
            try {
                Log.e("TEST", "Sending reward message...");
                sensorMessenger.send(LogDataUtils.generateRewardMessage(reward));
                reward = 0;
            } catch (RemoteException e) {
                Log.e("TEST", "Failed to send reward message.");
                e.printStackTrace();
            }
        } else {
            Log.e("TEST", "sensorMessenger is null.");
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
        intentSensorService.putExtra("rewardFolder", logFolder + File.separator + "reward_data");
        requireActivity().startService(intentSensorService);
        requireActivity().bindService(intentSensorService, sensorConnection, Context.BIND_AUTO_CREATE);
        runInBackground(
                () -> {
                    try {
                        // Send current vehicle state to log
                        TimeUnit.MILLISECONDS.sleep(500);
                        sendControlToSensorService();
                        sendIndicatorToSensorService();
                        sendRewardToSensorService();



                    } catch (InterruptedException e) {
                        Timber.e(e, "Got interrupted.");
                    }
                });
        if(autonomousControlEnabled){
            startAutonomousDriving();
        }
    }

    // This function is used to start Autonomous driving. It selects the model named "reinforcement_learning" and runs it. If it is not found it returns an error in the logs
    private void startAutonomousDriving() {
        try {
            if (autopilot == null) {
                String filePath = requireActivity().getFilesDir() + File.separator + "reinforcement_learning.tflite";
                File file = new File(filePath);

// Check if the file exists
                if (file.exists()) {
                    // File exists, create the Model object
                    tfModel = new Model(
                            masterList.size() + 1,
                            Model.CLASS.AUTOPILOT,
                            Model.TYPE.CMDNAV,
                            "reinforcement_learning.tflite",
                            Model.PATH_TYPE.FILE,
                            filePath,
                            "256x96");

                    Network.Device device = Network.Device.CPU; // Set your desired device here
                    int numThreads = 4; // Set the number of threads you want to use

                    autopilot = new Autopilot(getActivity(), tfModel, device, numThreads);
                    Log.e("AUTONOMOUS INIT", "Autopilot initialization success");
                    Log.e("AUTONOMOUS INIT", filePath);
                } else {
                    // File does not exist, handle accordingly (e.g., show an error message)
                    Log.e("AUTONOMOUS INIT", "Error: File does not exist - " + filePath);
                }

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

    // This function is to allow the model to control the robot
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
        runInBackground(this::sendRewardToSensorService);

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

                // here are where the rewards are given by the supervisor
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
        runInBackground(this::sendRewardToSensorService);
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
            // This is to send the frame to the correct function in case the autonomous control is enables
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
    // This is to send the image to the model so it can make decisions
    private void processFrameForAutonomous(Bitmap frameBitmap) {
        // Ensure that the autopilot is initialized
        if (autopilot != null) {
            // Perform image recognition and get control commands
            Control control = autopilot.recognizeImage(frameBitmap, vehicle.getIndicator());

            // Handle the control commands (e.g., update vehicle control)
            handleDriveCommandAutonomous(control);
        }
    }
    // These functions set the reward value
    private void changeRewardNegative(){
        reward = - 15;
    }

    private void changeRewardPositive(){
        reward =  15;
    }

    private void changeRewardDistance(double distance){
        if (abs(distance) < 100) {
            reward =  10;
        } else if (abs(distance) > 300) {
            reward =  - 1;
        } else if (abs(distance) < 300 ){
            reward =  1;

        } else if (distance != Double.MAX_VALUE){
            reward = -7;
        }
    }

    // This function is used to calculate the centroid
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

    // There are often numerous centroid, so this one finds the one closest to the goal
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

    // This function computes the distance between two points
    private double calculateDistance(Point point1, Point point2) {
        double dx = point1.x - point2.x;

        return dx;
    }

    // This is where the processing of the image is done for the reward function
    private Bitmap applyOpenCVProcessing(Bitmap inputImage) {
        Mat inputMat = new Mat(inputImage.getHeight(), inputImage.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(inputImage, inputMat);

        Imgproc.cvtColor(inputMat, inputMat, Imgproc.COLOR_RGBA2GRAY);

        Mat rotatedMat = new Mat();


        Scalar lowerWhite = new Scalar(200, 170, 170);
        Scalar higherWhite = new Scalar(254, 254, 254);

        double contrastFactor = 1.2;
        Mat contrastedMat = new Mat();
        inputMat.convertTo(contrastedMat, -1, contrastFactor, -40);
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

        changeRewardDistance(distance);

        Bitmap processedBitmap = Bitmap.createBitmap(bottom.cols(), bottom.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(bottom, processedBitmap);

        return processedBitmap;
    }
    //This function crops the image to only keep the region of interest
    public static Mat regionOfInterest(Mat inputMat){

        int width = inputMat.cols();
        int height = inputMat.rows();

        // Define the ROI as the bottom 20% of the image
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
