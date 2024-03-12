package org.openbot.logging;

import java.util.ArrayList;
import java.util.Arrays;

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
import android.util.Pair;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.CameraFragment;
import org.openbot.databinding.FragmentRlBinding;
import org.openbot.env.BotToControllerEventBus;
import org.openbot.env.ImageUtils;
import org.openbot.tflite.Model;
import org.openbot.utils.ConnectionUtils;
import org.openbot.utils.Constants;
import org.openbot.utils.Enums;
import org.openbot.utils.FormatUtils;
import org.openbot.utils.PermissionUtils;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

import kotlin.Triple;
import timber.log.Timber;
import java.util.Random;

public class RLFragment extends CameraFragment {

    private FragmentRlBinding binding;
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

    int[] actions = new int[3];
    private long rewards = 0;
    private long done = 0;

    private boolean outOfCircuit;

    private double percentage;
    private boolean reachedCheckpoint;




    private static final long LOGGING_DURATION_MILLIS = 10000; // 15 seconds
    private long loggingStartTime;
    private Handler timerHandler;

    private Handler randomActionsHandler;
    private static final long RANDOM_ACTIONS_INTERVAL = 500;




    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRlBinding.inflate(inflater, container, false);

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
            cropRect = new RectF(0.0f, 300.0f / 720.0f, 0.0f, 0.0f);
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

        timerHandler = new Handler();
        randomActionsHandler = new Handler();
        super.onResume();
    }

    @Override
    public synchronized void onPause() {
        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
            handler = null;
            randomActionsHandler.removeCallbacksAndMessages(null); // Remove any pending callbacks
            randomActionsHandler = null;
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

    protected void sendIndicatorToSensorService() {
        if (sensorMessenger != null) {
            try {
                sensorMessenger.send(LogDataUtils.generateIndicatorMessage(vehicle.getIndicator()));
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


    protected void sendVehicleDataToSensorService(long timestamp, String data, int type) {
        if (sensorMessenger != null) {
            try {
                sensorMessenger.send(LogDataUtils.generateVehicleDataMessage(timestamp, data, type));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    protected void sendInfoToSensorService() { //This is to send the information for the action and reward

        long[] info = {actions[0], actions[1], actions[2], rewards, done};
        String string_info = Arrays.toString(info);
        if (sensorMessenger != null) {
            try {
                sensorMessenger.send(LogDataUtils.generateRLMessage(string_info));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    private void startLogging() {
        done = 0;
        rewards = 0;
        outOfCircuit = false;
        reachedCheckpoint = false;
        startRandomActions();
        logFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                        .getAbsolutePath()
                        + File.separator
                        + getString(R.string.app_name)
                        + File.separator
                        + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        intentSensorService.putExtra("logFolder", logFolder + File.separator + "sensor_data");

        // Record the start time
        loggingStartTime = System.currentTimeMillis();
        timerHandler.post(timerRunnable);
        requireActivity().startService(intentSensorService);
        requireActivity().bindService(intentSensorService, sensorConnection, Context.BIND_AUTO_CREATE);
        runInBackground(
                () -> {
                    try {
                        // Send current vehicle state to log
                        TimeUnit.MILLISECONDS.sleep(500);
                        RandomActions();
                        sendControlToSensorService();

                        sendInfoToSensorService();

                        sendIndicatorToSensorService();

                        //timerHandler.postDelayed(timerRunnable, 500);


                    } catch (InterruptedException e) {
                        Timber.e(e, "Got interrupted.");
                    }
                });

    }

    private void stopLogging(boolean isCancel) {

        timerHandler.removeCallbacks(timerRunnable);

        done = 1;
        if (outOfCircuit){
            rewards = -1;
        }
        percentage = 0;
        vehicle.setControl(0f,0f);
        sendInfoToSensorService();
        stopRandomActions();

        if (sensorConnection != null) requireActivity().unbindService(sensorConnection);
        requireActivity().stopService(intentSensorService);

        if (isCancel) {
            Log.d("Logging", "Logging canceled");
        } else {
            Log.d("Logging", "Logging stopped after 20 seconds");
        }

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
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - loggingStartTime;
            Log.d("Timer", "Elapsed Time: " + elapsedTime + ", Percentage: " + percentage);
            if (elapsedTime >= LOGGING_DURATION_MILLIS || percentage > 25) {
                elapsedTime = 0;
                // Stop logging when the duration is reached
                if(elapsedTime >= LOGGING_DURATION_MILLIS) {
                    rewards=2;
                    outOfCircuit = false;
                }
                if(percentage > 20)
                {
                    Log.d("PERCENT", "Percentage: " + percentage);
                    outOfCircuit = true;
                }

                stopLogging(false);

                setLoggingActive(false);


            } else{
                // Continue checking elapsed time periodically
                if (elapsedTime >= 5000 && !reachedCheckpoint) // give reward if it runs for more than 5 seconds without stop
                {
                    reachedCheckpoint = true;
                    rewards = 1;
                }
                timerHandler.postDelayed(this, 500);
            }
        }
    };
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

            case Constants.CMD_INDICATOR_RIGHT:

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
        runInBackground(this::sendInfoToSensorService);
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
                        croppedBitmap, logFolder + File.separator + "images_true", frameNum + "_crop.jpeg");
            }

            if(croppedBitmap != null){
                Triple<Bitmap, Bitmap, Mat> images = applyOpenCVProcessing(croppedBitmap);
                Bitmap edgesBitmap = images.component1();
                Bitmap bottomBitmap = images.component2();
                Mat bottom = images.component3();
                binding.percentage.setText(String.valueOf(percentage));
                if (edgesBitmap != null) {
                    final Canvas canvas2 = new Canvas(croppedBitmap);
                    canvas2.drawBitmap(edgesBitmap, frameToCropTransform, null);
                    ImageUtils.saveBitmap(
                            edgesBitmap, logFolder + File.separator + "edges_images", frameNum + "_edges.jpeg");

                }

                if (bottomBitmap != null) {
                    final Canvas canvas2 = new Canvas(croppedBitmap);
                    canvas2.drawBitmap(bottomBitmap, frameToCropTransform, null);
                    ImageUtils.saveBitmap(
                            bottomBitmap, logFolder + File.separator + "images", frameNum + "_crop.jpeg");

                }
            }
        }
    }

    private Triple<Bitmap, Bitmap, Mat> applyOpenCVProcessing(Bitmap inputImage) {

        Mat inputMat = new Mat(inputImage.getHeight(), inputImage.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(inputImage, inputMat);

        Mat bottom = regionOfInterest(inputMat);

        Imgproc.cvtColor(bottom, bottom, Imgproc.COLOR_RGBA2GRAY); // Turn the image Gray

        Imgproc.threshold(bottom, bottom, 128, 255, Imgproc.THRESH_BINARY); // Using threshold to turn it black and white


        Mat edges = new Mat();
        Imgproc.Canny(bottom, edges, 100, 150);

        percentage = calculateWhitePixelPercentage(bottom);



        Bitmap edgesBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, edgesBitmap);
        Bitmap bottomBitmap = Bitmap.createBitmap(bottom.cols(), bottom.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(bottom, bottomBitmap);


        return new Triple<>(edgesBitmap, bottomBitmap, bottom);
    }

    //This function crops the image to only keep the region of interest
    public static Mat regionOfInterest(Mat inputMat){

        int width = inputMat.cols();
        int height = inputMat.rows();

        // Define the ROI as the bottom 30% of the image
        Point[] roiPoints = new Point[4];
        roiPoints[0] = new Point(width * 0.0, height * 0.7); // Top-left corner of ROI
        roiPoints[1] = new Point(width, height * 0.7);        // Top-right corner of ROI
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

    public double calculateWhitePixelPercentage(Mat binaryMat) {
        // Convert the binary bitmap to a Mat object


        // Verify pixel values after thresholding
        byte[] pixels = new byte[binaryMat.rows() * binaryMat.cols()];
        binaryMat.get(0, 0, pixels);


        // Count the number of white pixels
        int whiteCount = 0;
        for (byte pixel : pixels) {
            if (pixel == -1) { // white pixels are represented by -1
                whiteCount++;
            }
        }

        // Calculate the percentage of white pixels
        double totalPixels = binaryMat.rows() * binaryMat.cols();
        double whitePercentage = (whiteCount / totalPixels) * 100.0; // Ensure floating-point division

        return whitePercentage;
    }

    @Override
    public void onConnectionEstablished(String ipAddress) {
        requireActivity().runOnUiThread(() -> binding.ipAddress.setText(ipAddress));
    }

    private void RandomActions(){
        Random random = new Random();
        int randomIndex = random.nextInt(actions.length);
        Arrays.fill(actions, 0);

        // Set the randomly chosen index to 1
        actions[randomIndex] = 1;
        MoveAction();

    }

    private void MoveAction()
    {
        if(loggingEnabled) {
            if (actions[0] == 1) {
                vehicle.setControl(0.45f, 0.45f);
                handleDriveCommand();
            }
            if (actions[1] == 1)  {
                vehicle.setControl(0.5f, -0.1f);
                handleDriveCommand();
            }
            if (actions[2] == 1)  {
                vehicle.setControl(-0.1f, 0.5f);
                handleDriveCommand();
            }
        }
    }

    private void startRandomActions() {
        randomActionsHandler.postDelayed(randomActionsRunnable, RANDOM_ACTIONS_INTERVAL);
    }

    private void stopRandomActions() {
        randomActionsHandler.removeCallbacks(randomActionsRunnable);
    }

    private Runnable randomActionsRunnable = new Runnable() {
        @Override
        public void run() {
            RandomActions(); // Generate random actions
            randomActionsHandler.postDelayed(this, RANDOM_ACTIONS_INTERVAL); // Schedule next random actions generation
        }
    };
}

