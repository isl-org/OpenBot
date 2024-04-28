package org.openbot.logging;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import android.widget.EditText;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.CameraFragment;
import org.openbot.databinding.FragmentRlBinding;
import org.openbot.databinding.PolicyGradientBinding;
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

public class PolicyGradientFragment extends CameraFragment {

    private PolicyGradientBinding binding;
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

    int action;
    private double rewards = 0;
    private double totalRewards;
    private long done = 0;

    private boolean outOfCircuit;

    private double percentage;
    private boolean reachedCheckpoint;

    private static final long LOGGING_DURATION_MILLIS = 10000; // 15 seconds
    private long loggingStartTime;
    private Handler timerHandler;

    private Handler randomActionsHandler;
    private static final long RANDOM_ACTIONS_INTERVAL = 200;

    private MyModel myModel;
    private double learningRate = 1e-3; // learning rate used in RMS prop
    private double gamma = 0.99; // discount factor for reward
    private double decayRate = 0.99; // decay factor for RMSProp leaky sum of grad^2
    private boolean resume = false;
    private Map<String, double[][]> gradBuffer = new HashMap<>();
    private Map<String, double[][]> rmsPropCache = new HashMap<>();


    private ArrayList<double[][]> xs = new ArrayList<>();
    private ArrayList<double[][]> hs = new ArrayList<>();
    private ArrayList<double[]> dLogPs = new ArrayList<>();
    private ArrayList<Double> drs = new ArrayList<>();
    private ArrayList<double[][]> epx = new ArrayList<>();
    private ArrayList<double[][]> eph = new ArrayList<>();
    private ArrayList<double[]> epdLogP = new ArrayList<>();
    private ArrayList<Double> epr = new ArrayList<>();
    private double[] discounted_epr;
    private ArrayList<Double> rewardsArray = new ArrayList<>();
    private double mean_rewards = 0;
    private Handler updateHandler = new Handler();
    private final int UPDATE_INTERVAL = 1000;
    private double[][] x;
    private boolean is_rand;


    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = PolicyGradientBinding.inflate(inflater, container, false);

        if (!OpenCVLoader.initDebug()) {
            // OpenCV initialization failed, handle the error
            Log.e("OPENCV TEST", "OpenCV initialization failed");
        } else {
            // OpenCV initialized successfully, proceed with your code
            Log.d("OPENCV TEST", "OpenCV initialized successfully");
        }
        myModel = new MyModel();


        return inflateFragment(binding, inflater, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int H = 200; // Number of hidden units
        int D = 128*30; // Input dimension
        myModel.initializeWeights(H, D);

        for (Map.Entry<String, double[][]> entry : myModel.getModel().entrySet()) {
            String key = entry.getKey();
            double[][] value = entry.getValue();
            gradBuffer.put(key, new double[value.length][value[0].length]);
            rmsPropCache.put(key, new double[value.length][value[0].length]);
        }

        binding.resumeSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    resume = isChecked;
                });

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
            cropRect = new RectF(0.0f, 240.0f/700.0f, 0.0f, 0.0f);
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

   /* protected void sendRewardsArrayToSensorService() {
        double[] rArray = convertDoubleArrayListToArray(rewardsArray);
        String string_reward = Arrays.toString(rArray);
        if (sensorMessenger != null) {
            try {
                Log.e("TEST", "Sending reward message...");
                sensorMessenger.send(LogDataUtils.generateRewardsArrayMessage(string_reward));
            } catch (RemoteException e) {
                Log.e("TEST", "Failed to send reward message.");
                e.printStackTrace();
            }
        } else {
            Log.e("TEST", "sensorMessenger is null.");
        }
    }*/


    private void startLogging() {
        if (resume==true){
            myModel.loadModel("/storage/emulated/0/Documents/OpenBot/" + File.separator + "models_ppo" + "model");
            rewardsArray = loadRewardsArray();
        }
        startUpdateModel();
        done = 0;
        rewards = 0;
        outOfCircuit = false;
        reachedCheckpoint = false;
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
                        sendControlToSensorService();


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
            if(epr.size() != 0) {
                int lastIndex = epr.size() - 1; // Get the index of the last element
                double newReward = -5; // Example new reward value
                epr.set(lastIndex, newReward);
                rewards = -5;
                totalRewards += rewards;
            }
        }
        percentage = 0;
        vehicle.setControl(0f,0f);

        resetArrays();

        discounted_epr = discountRewards(epr);
        discounted_epr = standardizeRewards(discounted_epr);
        double[][] epdLogP_array = convertDoubleArrayListToArray(epdLogP);
        double[][] epdLogPDouble = modulateGradient(epdLogP_array, discounted_epr);

        double[][] eph_Array = convertArrayListToArray(eph);
        int numRows = eph_Array.length; // Number of rows
        int numCols = eph_Array[0].length;
        Log.d("SIZE OF rows: ", "epx after conversion: " + numRows);
        Log.d("SIZE OF columns: ", "epx after conversion: " + numCols);
        double[][] epx_Array = convertArrayListToArray(epx);
        numRows = epx_Array.length; // Number of rows
        numCols = epx_Array[0].length;
        Log.d("SIZE OF rows: ", "epx after conversion: " + numRows);
        Log.d("SIZE OF columns: ", "epx after conversion: " + numCols);



        stopUpdateModel();
        Map<String, double[][]> grad = myModel.policyBackward(eph_Array, epx_Array, epdLogPDouble);

        accumulateGradients(grad, gradBuffer);

        Map<String, double[][]> updatedModel = myModel.updateModel(gradBuffer, rmsPropCache, decayRate, learningRate);
        myModel.setModel(updatedModel);
        myModel.saveModel("/storage/emulated/0/Documents/OpenBot/" + File.separator + "models_ppo" + "model");
        rewardsArray.add(totalRewards);
        saveRewardsArray(rewardsArray);
        binding.rewards.setText(String.valueOf(totalRewards));





        Log.d("DONE", "UPDATE DONE");
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
            double elapsedTime = System.currentTimeMillis() - loggingStartTime;
            Log.d("Timer", "Elapsed Time: " + elapsedTime + ", Percentage: " + percentage);
            if (elapsedTime >= LOGGING_DURATION_MILLIS || percentage > 75) {
                elapsedTime = 0;
                // Stop logging when the duration is reached
                if(elapsedTime >= LOGGING_DURATION_MILLIS) {
                    rewards=2;
                    totalRewards += rewards;
                    outOfCircuit = false;
                }
                if(percentage > 75)
                {
                    Log.d("PERCENT", "Percentage: " + percentage);
                    outOfCircuit = true;
                }

                stopLogging(false);

                setLoggingActive(false);


            } else{
                rewards = (elapsedTime * 1e-3) - rewards;
                timerHandler.postDelayed(this, 200);
                totalRewards += rewards;
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
                outOfCircuit = false;
                loggingEnabled = true;
            }
        } else if (!enableLogging && loggingEnabled) {
            outOfCircuit = true;
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

            sendFrameNumberToSensorService(frameNum);
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


            if (croppedBitmap != null) {
                Bitmap bottomBitmap = OpenCVProcessing(croppedBitmap);
                binding.percentage.setText(String.valueOf(percentage));
                binding.rewards.setText(String.valueOf(rewards));

                int originalWidth = bottomBitmap.getWidth();
                int originalHeight = bottomBitmap.getHeight();

                // Resize the original image to half its original size
                int newWidth = originalWidth / 2;
                int newHeight = originalHeight / 2;

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bottomBitmap, newWidth, newHeight, true);

                // Crop the resized bitmap to keep only the bottom 35 pixel lines
                int cropHeight = Math.min(30, newHeight);
                int startY = Math.max(0, newHeight - cropHeight); // Start from the bottom 35 pixel lines
                Bitmap croppedBottomBitmap = Bitmap.createBitmap(resizedBitmap, 0, startY, newWidth, cropHeight);

                int finalHeight = croppedBottomBitmap.getHeight();
                int finalWidth = croppedBottomBitmap.getWidth();

                Log.d("Final Height: ", "Height: " + finalHeight);
                Log.d("Final Width: ", "Width: " + finalWidth);

                Bitmap finalBitmap = Bitmap.createBitmap(newWidth,cropHeight,Bitmap.Config.ARGB_8888);
                final Canvas canvas3 = new Canvas(finalBitmap);
                canvas3.drawBitmap(croppedBottomBitmap,0,0,null);
                x = convertBitmapToDoubleArray(finalBitmap);





                ImageUtils.saveBitmap(
                        finalBitmap, logFolder + File.separator + "images", frameNum + "_crop.jpeg");



            }
        }
    }



    private Bitmap OpenCVProcessing(Bitmap inputImage) {
        Mat inputMat = new Mat(inputImage.getHeight(), inputImage.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(inputImage, inputMat);

        // Mat bottom = regionOfInterest(inputMat);
        Mat bottom = inputMat;

        Imgproc.cvtColor(bottom, bottom, Imgproc.COLOR_RGBA2GRAY); // Turn the image Gray

        Imgproc.threshold(bottom, bottom, 128, 255, Imgproc.THRESH_BINARY); // Using threshold to turn it black and white


        Mat edges = new Mat();
        Imgproc.Canny(bottom, edges, 100, 150);

        percentage = calculateWhitePixelPercentage(bottom);



        Bitmap edgesBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, edgesBitmap);
        Bitmap bottomBitmap = Bitmap.createBitmap(bottom.cols(), bottom.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(bottom, bottomBitmap);



        return bottomBitmap;
    }

    public double[] fakeLabel() {
        double[] label = {0 , 0, 0};
        if (action == 3) {
            label[2] = 1;
            return label; // Label 1 for action 1
        } else if (action == 2) {
            label[1] = 1;
            return label;
        } else {
            label[0] = 1;
            return label; // Label 0 for actions 2 and 3
        }
    }
    private double[][] convertBitmapToDoubleArray(Bitmap bitmap) {
        // Implement logic to convert Bitmap to double[][] array
        // Example code to demonstrate conversion (replace with your logic):
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        double[][] result = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Assuming grayscale image, convert pixel value to double
                result[i][j] = bitmap.getPixel(j, i) & 0xFF; // Extracting grayscale value
            }
        }
        return result;
    }
    public int chooseAction(double[] probability) {
        Random random = new Random();
        double randValue = random.nextDouble(); // Generate a random value between 0 and 1
        EditText epsilonInput = binding.epsilonInput;
        String epsilonText = epsilonInput.getText().toString();
        double epsilon = Double.parseDouble(epsilonText);

        if (randValue < epsilon)
        {
            int randomNumber = random.nextInt(3) + 1;
            is_rand = true;
            return randomNumber;
        }
        int maxIndex = 0;
        double maxValue = probability[0];
        for (int i = 1; i < probability.length; i++) {
            if (probability[i] > maxValue) {
                maxValue = probability[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }
    /*private Triple<Bitmap, Bitmap, Mat> applyOpenCVProcessing(Bitmap inputImage) {

        Mat inputMat = new Mat(inputImage.getHeight(), inputImage.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(inputImage, inputMat);

        // Mat bottom = regionOfInterest(inputMat);
        Mat bottom = inputMat;

        Imgproc.cvtColor(bottom, bottom, Imgproc.COLOR_RGBA2GRAY); // Turn the image Gray

        Imgproc.threshold(bottom, bottom, 128, 255, Imgproc.THRESH_BINARY); // Using threshold to turn it black and white


        Mat edges = new Mat();
        Imgproc.Canny(bottom, edges, 100, 150);

        percentage = calculateWhitePixelPercentage(bottom);



        Bitmap edgesBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, edgesBitmap);
        Bitmap bottomBitmap = Bitmap.createBitmap(bottom.cols(), bottom.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(bottom, bottomBitmap);

        int originalWidth = bottomBitmap.getWidth();
        int originalHeight = bottomBitmap.getHeight();

        // Resize the original image to half its original size
        int newWidth = originalWidth / 2;
        int newHeight = originalHeight / 2;

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bottomBitmap, newWidth, newHeight, true);

        // Crop the resized bitmap to keep only the bottom 35 pixel lines
        int cropHeight = Math.min(30, newHeight);
        int startY = Math.max(0, newHeight - cropHeight); // Start from the bottom 35 pixel lines
        Bitmap croppedBottomBitmap = Bitmap.createBitmap(resizedBitmap, 0, startY, newWidth, cropHeight);

        int finalHeight = croppedBottomBitmap.getHeight();
        int finalWidth = croppedBottomBitmap.getWidth();

        Log.d("Final Height: ", "Height: " + finalHeight);
        Log.d("Final Width: ", "Width: " + finalWidth);


        return new Triple<>(edgesBitmap, bottomBitmap, bottom);
    }*/

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

    private void MoveAction()
    {
        if(loggingEnabled) {
            if (action == 3) {
                vehicle.setControl(0.45f, 0.45f);
                handleDriveCommand();
            }
            if (action == 2)  {
                vehicle.setControl(0.5f, -0.1f);
                handleDriveCommand();
            }
            if (action == 1)  {
                vehicle.setControl(-0.1f, 0.5f);
                handleDriveCommand();
            }
        }
    }

    public void resetArrays() {
        // Convert temporary arrays to arrays
        double[][] tempXs = convertArrayListToArray(xs);
        double[][] tempHs = convertArrayListToArray(hs);
        double[][] tempDLogPs = convertDoubleArrayListToArray(dLogPs);
        double[] tempDrs = convertDoubleListToArray(drs);

        // Accumulate arrays across episodes
        if (!xs.isEmpty()) {
            addAndFlatten(tempXs, tempHs, dLogPs, drs);

        }

        // Clear temporary ArrayLists
        xs.clear();
        hs.clear();
        dLogPs.clear();
        drs.clear();
    }

    // Method to convert ArrayList<double[][]> to double[][]
    private double[][] convertArrayListToArray(ArrayList<double[][]> arrayList) {
        int size = arrayList.size();
        int rows = arrayList.get(0).length;
        int cols = arrayList.get(0)[0].length;
        double[][] array = new double[size * rows][cols];
        int index = 0;
        for (double[][] matrix : arrayList) {
            for (double[] row : matrix) {
                array[index] = row.clone(); // Copy the row into the array
                index++;
            }
        }
        return array;
    }

    // Method to convert ArrayList<Double> to double[]
    private double[] convertDoubleListToArray(ArrayList<Double> arrayList) {
        int size = arrayList.size();
        double[] array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    private double[][] convertDoubleArrayListToArray(ArrayList<double[]> arrayList) {
        int size = arrayList.size();
        double[][] array = new double[size][];
        for (int i = 0; i < size; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    private void addAndFlatten(double[][] tempXs, double[][] tempHs, ArrayList<double[]> dLogPs, ArrayList<Double> drs) {
        epx.add(tempXs);
        if (epx.size() == 2) {
            flattenArrayList(epx);
        }
        eph.add(tempHs);
        if (eph.size() == 2) {
            flattenArrayList(eph);
        }
        epdLogP.addAll(dLogPs);
        if (epdLogP.size() == 2) {
            flattenDoubleArrayList(epdLogP);
        }
        epr.addAll(drs);
        if (epr.size() == 2) {
            flattenDoubleList(epr);
        }
    }

    // Function to flatten ArrayList<double[][]>
    private void flattenArrayList(ArrayList<double[][]> arrayList) {
        double[][] flattenedArray = new double[arrayList.get(0).length + arrayList.get(1).length][arrayList.get(0)[0].length];
        int index = 0;
        for (double[][] array : arrayList) {
            for (double[] row : array) {
                flattenedArray[index++] = row;
            }
        }
        // Clear arrayList and add flattened array
        arrayList.clear();
        arrayList.add(flattenedArray);
    }

    // Function to flatten ArrayList<Double>
    private void flattenDoubleList(ArrayList<Double> arrayList) {
        double[] flattenedArray = new double[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            flattenedArray[i] = arrayList.get(i);
        }
        // Clear arrayList and add flattened array
        arrayList.clear();
        for (double val : flattenedArray) {
            arrayList.add(val);
        }
    }

    private void flattenDoubleArrayList(ArrayList<double[]> arrayList) {
        int totalSize = 0;
        for (double[] arr : arrayList) {
            totalSize += arr.length;
        }
        double[] flattenedArray = new double[totalSize];
        int index = 0;
        for (double[] arr : arrayList) {
            for (double val : arr) {
                flattenedArray[index++] = val;
            }
        }
        // Clear arrayList and add flattened array
        arrayList.clear();
        arrayList.add(flattenedArray);
    }

    private static double[][] flattenImage(double[][] array) {
        int numRows = array.length;
        int numCols = array[0].length;
        double[][] newArray = new double[numRows * numCols][1];
        int index = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                newArray[index++][0] = array[i][j];
            }
        }
        return newArray;
    }


    public double[] discountRewards(ArrayList<Double> rewards) {
        int n = rewards.size();
        double[] discountedRewards = new double[n];
        double runningTotal = 0;

        // Calculate discounted rewards
        for (int t = n - 1; t >= 0; t--) {
            runningTotal = runningTotal * gamma + rewards.get(t);
            discountedRewards[t] = runningTotal;
        }

        return discountedRewards;
    }


    public double[] standardizeRewards(double[] discounted_rewards) {
        // Calculate mean and standard deviation
        double mean = calculateMean(discounted_rewards);
        double stdDev = calculateStandardDeviation(discounted_rewards, mean);

        // Standardize rewards
        for (int i = 0; i < discounted_rewards.length; i++) {
            discounted_rewards[i] = (discounted_rewards[i] - mean) / stdDev;
        }
        return discounted_rewards;
    }

    private double calculateMean(double[] array) {
        double sum = 0;
        for (double value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    private double calculateStandardDeviation(double[] array, double mean) {
        double sumOfSquaredDifferences = 0;
        for (double value : array) {
            double diff = value - mean;
            sumOfSquaredDifferences += diff * diff;
        }
        return Math.sqrt(sumOfSquaredDifferences / array.length);
    }

    public double[][] modulateGradient(double[][] epdLog, double[] discountedEpr) {
        if (epdLog.length != discountedEpr.length) {
            throw new IllegalArgumentException("Arrays must have the same length.");
        }

        // Perform element-wise multiplication
        for (int i = 0; i < epdLog.length; i++) {
            for(int j=0; j < 3; j++){
                epdLog[i][j] *= discountedEpr[i];
            }
        }
        return epdLog;
    }
    public void accumulateGradients(Map<String, double[][]> grad, Map<String, double[][]> gradBuffer) {
        for (String key : grad.keySet()) {
            double[][] gradArray = grad.get(key);
            double[][] gradBufferArray = gradBuffer.get(key);

            Log.d("SIZE OF", "Dimensions of " + key + " array:");
            Log.d("SIZE OF", "gradArray: " + gradArray.length + " x " + gradArray[0].length);
            Log.d("SIZE OF", "gradBufferArray: " + gradBufferArray.length + " x " + gradBufferArray[0].length);

            if (gradArray.length != gradBufferArray.length || gradArray[0].length != gradBufferArray[0].length) {
                throw new IllegalArgumentException("Dimensions of gradient arrays must match.");
            }

            // Accumulate gradients over batch
            for (int i = 0; i < gradArray.length; i++) {
                for (int j = 0; j < gradArray[0].length; j++) {
                    gradBufferArray[i][j] += gradArray[i][j];
                }
            }
        }
    }

    private void saveRewardsArray(ArrayList<Double> rewardsArray) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(logFolder + File.separator + "rewardsArray.ser"))) {
            out.writeObject(rewardsArray);
            System.out.println("Rewards array saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving rewards array: " + e.getMessage());
        }
    }

    private ArrayList<Double> loadRewardsArray() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(logFolder + File.separator + "rewardsArray.ser"))) {
            return (ArrayList<Double>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading rewards array: " + e.getMessage());
            return new ArrayList<>(); // Return an empty ArrayList if loading fails
        }
    }

    private void startUpdateModel() {
        randomActionsHandler.postDelayed(randomActionsRunnable, RANDOM_ACTIONS_INTERVAL);
    }

    private void stopUpdateModel() {
        randomActionsHandler.removeCallbacks(randomActionsRunnable);
    }

    private Runnable randomActionsRunnable = new Runnable() {
        @Override
        public void run() {
            stepToUpdateModel(); // Generate random actions
            randomActionsHandler.postDelayed(this, RANDOM_ACTIONS_INTERVAL); // Schedule next random actions generation
        }
    };

    private void stepToUpdateModel()
    {
        if(x!=null) {
            double[][] flatImage = flattenImage(x);
            Object[] result = myModel.policyForward(flatImage);
            double[] aProb = (double[]) result[0];
            double[][] h = (double[][]) result[1];
            Log.d("SIZE OF h: ", "size: " + h.length);
            action = chooseAction(aProb);
            MoveAction();
            xs.add(flatImage);
            Log.d("SIZE OF xs: ", "size: " + xs.size());
            hs.add(h);
            Log.d("SIZE OF hs: ", "size: " + hs.size());

            double[] y = fakeLabel();
            if (is_rand){
                dLogPs.add(y);
            } else {
                dLogPs.add(subtractArrays(y, aProb));
            }

            Log.d("SIZE OF dLogPs: ", "size: " + dLogPs.size());
            drs.add(rewards);
            Log.d("SIZE OF drs: ", "size: " + drs.size());
        }

    }

    public double[] subtractArrays(double[] array1, double[] array2) {
        if (array1 == null || array2 == null || array1.length != array2.length) {
            throw new IllegalArgumentException("Arrays are null or have different lengths");
        }

        int length = array1.length;
        double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            result[i] = array1[i] - array2[i];
        }

        return result;
    }

}


