/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

// Modified by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.openbot.customview.OverlayView;
import org.openbot.customview.OverlayView.DrawCallback;
import org.openbot.env.AudioPlayer;
import org.openbot.env.BorderedText;
import org.openbot.env.ImageUtils;
import org.openbot.env.Logger;
import org.openbot.tflite.Autopilot;
import org.openbot.tflite.Detector;
import org.openbot.tflite.Network.Device;
import org.openbot.tflite.Network.Model;
import org.openbot.tracking.MultiBoxTracker;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class NetworkActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();

    // Minimum detection confidence to track a detection.
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(1280, 720); // 16:9

    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private Integer sensorOrientation;

    private Detector detector;
    private Autopilot autoPilot;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingNetwork = false;
    private boolean driveByNetwork = false;
    private boolean noiseEnabled = false;
    private long frameNum = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;
    private BorderedText borderedText;

    private final AudioPlayer audioPlayer;
    private final String voice;

    public NetworkActivity() {
        audioPlayer = new AudioPlayer(this);
        voice = "matthew";
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        tracker = new MultiBoxTracker(this);

        // int cropSize = TF_OD_API_INPUT_SIZE;

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);

        recreateNetwork(getModel(), getDevice(), getNumThreads());
        if (detector == null && autoPilot == null) {
            LOGGER.e("No network on preview!");
            return;
        }

        trackingOverlay = findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                new DrawCallback() {
                    @Override
                    public void drawCallback(final Canvas canvas) {
                        tracker.draw(canvas);
                        if (isDebug()) {
                            tracker.drawDebug(canvas);
                        }
                    }
                });
        tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
    }

    @Override
    protected void processImage() {
        ++frameNum;
        final long currFrameNum = frameNum;
        // trackingOverlay.postInvalidate();

        // If network is busy and we don't need to log any image, return.
        if (computingNetwork && !getLoggingEnabled()) {
            readyForNextImage();
            return;
        }

        final boolean SAVE_PREVIEW_BITMAP =
                logMode.equals(LogMode.ALL_IMGS) || logMode.equals(LogMode.PREVIEW_IMG);
        final boolean SAVE_CROP_BITMAP =
                logMode.equals(LogMode.ALL_IMGS) || logMode.equals(LogMode.CROP_IMG);

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
        if (getLoggingEnabled() && SAVE_PREVIEW_BITMAP) {
            runInBackground(
                    () ->
                            ImageUtils.saveBitmap(
                                    rgbFrameBitmap,
                                    logFolder + File.separator + "images",
                                    currFrameNum + "_preview.jpeg"));
            if (!SAVE_CROP_BITMAP) sendFrameNumberToSensorService(currFrameNum);
        }

        readyForNextImage();
        // If network is busy and we don't need to log the crop, return.
        if (computingNetwork && !SAVE_CROP_BITMAP) {
            return;
        }

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
        // For examining the actual TF input.
        if (getLoggingEnabled() && SAVE_CROP_BITMAP) {
            runInBackground(
                    () ->
                            ImageUtils.saveBitmap(
                                    croppedBitmap,
                                    logFolder + File.separator + "images",
                                    currFrameNum + "_crop.jpeg"));
            sendFrameNumberToSensorService(currFrameNum);
        }

        // Network is control of the vehicle
        if (driveByNetwork) {
            // If network is busy, return.
            if (computingNetwork) {
                return;
            }

            computingNetwork = true;
            LOGGER.i("Putting image " + currFrameNum + " for detection in bg thread.");

            runInBackground(
                    () -> {
                        if (detector != null) {
                            LOGGER.i("Running detection on image " + currFrameNum);
                            final long startTime = SystemClock.elapsedRealtime();
                            final List<Detector.Recognition> results = detector.recognizeImage(croppedBitmap);
                            lastProcessingTimeMs = SystemClock.elapsedRealtime() - startTime;

                            if (!results.isEmpty())
                                LOGGER.i(
                                        "Object: "
                                                + results.get(0).getLocation().centerX()
                                                + ", "
                                                + results.get(0).getLocation().centerY()
                                                + ", "
                                                + results.get(0).getLocation().height()
                                                + ", "
                                                + results.get(0).getLocation().width());

                            cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                            final Canvas canvas1 = new Canvas(cropCopyBitmap);
                            final Paint paint = new Paint();
                            paint.setColor(Color.RED);
                            paint.setStyle(Style.STROKE);
                            paint.setStrokeWidth(2.0f);

                            float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;

                            final List<Detector.Recognition> mappedRecognitions =
                                    new LinkedList<Detector.Recognition>();

                            for (final Detector.Recognition result : results) {
                                final RectF location = result.getLocation();
                                if (location != null && result.getConfidence() >= minimumConfidence) {
                                    canvas1.drawRect(location, paint);
                                    cropToFrameTransform.mapRect(location);
                                    result.setLocation(location);
                                    mappedRecognitions.add(result);
                                }
                            }

                            tracker.trackResults(mappedRecognitions, currFrameNum);
                            vehicle.setControl(tracker.updateTarget());
                            trackingOverlay.postInvalidate();
                        } else if (autoPilot != null) {
                            LOGGER.i("Running autopilot on image " + currFrameNum);
                            final long startTime = SystemClock.elapsedRealtime();
                            vehicle.setControl(autoPilot.recognizeImage(croppedBitmap, vehicle.getIndicator()));
                            lastProcessingTimeMs = SystemClock.elapsedRealtime() - startTime;
                        }

                        computingNetwork = false;

                        if (getLoggingEnabled()) {
                            sendInferenceTimeToSensorService(currFrameNum, lastProcessingTimeMs);
                        }

                        updateVehicleState();
                    });
        }

        runOnUiThread(
                () -> {
                    frameValueTextView.setText(
                            String.format(Locale.US, "%d x %d", previewWidth, previewHeight));
                    cropValueTextView.setText(
                            String.format(
                                    Locale.US, "%d x %d", croppedBitmap.getWidth(), croppedBitmap.getHeight()));
                    if (driveByNetwork)
                        inferenceTimeTextView.setText(String.format(Locale.US, "%d ms", lastProcessingTimeMs));
                });
    }

    void updateVehicleState() {

        float left;
        float right;

        // Log controls
        if (getLoggingEnabled()) {
            runInBackground(this::sendControlToSensorService);
        }

        if (getNoiseEnabled()) {
            left = vehicle.getNoisyControl().getLeft();
            right = vehicle.getNoisyControl().getRight();
            sendNoisyControlToVehicle();
        } else {
            left = vehicle.getControl().getLeft();
            right = vehicle.getControl().getRight();
            sendControlToVehicle();
        }

        // Update GUI
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.i("display_ctrl", "runnable");
                        if (controlValueTextView != null)
                            controlValueTextView.setText(String.format(Locale.US, "%.0f,%.0f", left, right));
                    }
                });
    }

    private Timer noiseTimer;

    private class NoiseTask extends TimerTask {
        @Override
        public void run() {
            vehicle.applyNoise();
            updateVehicleState();
        }
    }

    private boolean getNoiseEnabled() {
        return noiseEnabled;
    }

    private void toggleNoise() {
        noiseEnabled = !noiseEnabled;
        if (noiseEnabled) {
            noiseTimer = new Timer();
            NoiseTask noiseTask = new NoiseTask();
            noiseTimer.schedule(noiseTask, 0, 50); // no delay 50ms intervals
        } else noiseTimer.cancel();
        updateVehicleState();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.camera_connection_fragment_tracking;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    @Override
    protected void setDriveByNetwork(final boolean isChecked) {
        driveByNetwork = isChecked;
        if (driveByNetwork) {
            networkSwitchCompat.setText(R.string.on);
        } else {
            runInBackground(
                    () -> {
                        try {
                            TimeUnit.MILLISECONDS.sleep(lastProcessingTimeMs);
                            vehicle.setControl(0, 0);
                            updateVehicleState();
                            runOnUiThread(
                                    () -> {
                                        inferenceTimeTextView.setText(R.string.time_ms);
                                    });
                        } catch (InterruptedException e) {
                            LOGGER.e(e, "Got interrupted.");
                        }
                    });
            networkSwitchCompat.setText(R.string.off);
        }

        networkSwitchCompat.setChecked(driveByNetwork);
        if (driveByNetwork) {
            controlModeSpinner.setAlpha(0.5f);
            driveModeSpinner.setAlpha(0.5f);
            speedModeSpinner.setAlpha(0.5f);
        } else {
            controlModeSpinner.setAlpha(1.0f);
            driveModeSpinner.setAlpha(1.0f);
            speedModeSpinner.setAlpha(1.0f);
        }
        controlModeSpinner.setEnabled(!driveByNetwork);
        driveModeSpinner.setEnabled(!driveByNetwork);
        speedModeSpinner.setEnabled(!driveByNetwork);
    }

    @Override
    protected void onInferenceConfigurationChanged() {
        computingNetwork = false;
        if (croppedBitmap == null) {
            // Defer creation until we're getting camera frames.
            return;
        }
        final Device device = getDevice();
        final Model model = getModel();
        final int numThreads = getNumThreads();
        runInBackground(() -> recreateNetwork(model, device, numThreads));
    }

    private void recreateNetwork(Model model, Device device, int numThreads) {
        tracker.clearTrackedObjects();
        if (detector != null) {
            LOGGER.d("Closing detector.");
            detector.close();
            detector = null;
        }
        if (autoPilot != null) {
            LOGGER.d("Closing autoPilot.");
            autoPilot.close();
            autoPilot = null;
        }

        try {
            if (model == Model.DETECTOR_V1_1_0_Q || model == Model.DETECTOR_V3_S_Q) {
                LOGGER.d(
                        "Creating detector (model=%s, device=%s, numThreads=%d)", model, device, numThreads);
                detector = Detector.create(this, model, device, numThreads);
                croppedBitmap =
                        Bitmap.createBitmap(
                                detector.getImageSizeX(), detector.getImageSizeY(), Config.ARGB_8888);
                frameToCropTransform =
                        ImageUtils.getTransformationMatrix(
                                previewWidth,
                                previewHeight,
                                croppedBitmap.getWidth(),
                                croppedBitmap.getHeight(),
                                sensorOrientation,
                                detector.getCropRect(),
                                detector.getMaintainAspect());
            } else {
                LOGGER.d(
                        "Creating autopilot (model=%s, device=%s, numThreads=%d)", model, device, numThreads);
                autoPilot = Autopilot.create(this, model, device, numThreads);
                croppedBitmap =
                        Bitmap.createBitmap(
                                autoPilot.getImageSizeX(), autoPilot.getImageSizeY(), Config.ARGB_8888);
                frameToCropTransform =
                        ImageUtils.getTransformationMatrix(
                                previewWidth,
                                previewHeight,
                                croppedBitmap.getWidth(),
                                croppedBitmap.getHeight(),
                                sensorOrientation,
                                autoPilot.getCropRect(),
                                autoPilot.getMaintainAspect());
            }

            cropToFrameTransform = new Matrix();
            frameToCropTransform.invert(cropToFrameTransform);

        } catch (IOException e) {
            LOGGER.e(e, "Failed to create detector.");
        }
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        // Make sure vehicle is not controlled by network
        if (!driveByNetwork) {
            // Check that the event came from a game controller
            if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
                    && event.getAction() == MotionEvent.ACTION_MOVE) {
                if (controlMode == ControlMode.GAMEPAD) {
                    // Process the current movement sample in the batch (position -1)
                    vehicle.setControl(gameController.processJoystickInput(event, -1));
                    updateVehicleState();
                    return true;
                }
            }
        }
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Check that the event came from a game controller
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD
                && event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BUTTON_A:
                    setIsLoggingActive(!getLoggingEnabled());
                    if (getLoggingEnabled()) audioPlayer.play(voice, "logging_started.mp3");
                    else audioPlayer.play(voice, "logging_stopped.mp3");
                    return true;
                case KeyEvent.KEYCODE_BUTTON_B:
                    vehicle.setIndicator(1);
                    if (getLoggingEnabled()) {
                        sendIndicatorToSensorService();
                    }
                    sendIndicatorToVehicle();
                    return true;
                case KeyEvent.KEYCODE_BUTTON_Y:
                    vehicle.setIndicator(0);
                    if (getLoggingEnabled()) {
                        sendIndicatorToSensorService();
                    }
                    sendIndicatorToVehicle();
                    return true;
                case KeyEvent.KEYCODE_BUTTON_X:
                    vehicle.setIndicator(-1);
                    if (getLoggingEnabled()) {
                        sendIndicatorToSensorService();
                    }
                    sendIndicatorToVehicle();
                    return true;
                case KeyEvent.KEYCODE_BUTTON_START:
                    toggleNoise();
                    if (getNoiseEnabled()) audioPlayer.play(voice, "noise_enabled.mp3");
                    else audioPlayer.play(voice, "noise_disabled.mp3");
                    return true;
                case KeyEvent.KEYCODE_BUTTON_L1:
                    if (driveByNetwork) return true;
                    switch (driveMode) {
                        case DUAL:
                            setDriveMode(DriveMode.GAME);
                            audioPlayer.play(voice, "video_game_control.mp3");
                            break;
                        case GAME:
                            setDriveMode(DriveMode.JOYSTICK);
                            audioPlayer.play(voice, "joystick_control.mp3");
                            break;
                        case JOYSTICK:
                            setDriveMode(DriveMode.DUAL);
                            audioPlayer.play(voice, "dual_drive_control.mp3");
                            break;
                    }
                    driveModeSpinner.setSelection(driveMode.ordinal());
                    return true;
                case KeyEvent.KEYCODE_BUTTON_R1:
                    setDriveByNetwork(!driveByNetwork);
                    if (driveByNetwork) audioPlayer.play(voice, "network_enabled.mp3");
                    else {
                        switch (driveMode) {
                            case DUAL:
                                audioPlayer.play(voice, "dual_drive_control.mp3");
                                break;
                            case GAME:
                                audioPlayer.play(voice, "video_game_control.mp3");
                                break;
                            case JOYSTICK:
                                audioPlayer.play(voice, "joystick_control.mp3");
                                break;
                        }
                    }
                    return true;
                default:
                    // Toast.makeText(this,"Key " + event.getKeyCode() + " not recognized",
                    // Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    // Classes to handle events from a Controller
    private void handleControllerEvents() {
        ControllerHandler handler = new ControllerHandler();

        ControllerEventProcessor.getProcessor().subscribe(event -> {
            ControllerEventProcessor.ControllerEvent command = event;

            switch (command.type) {
                case DRIVE_LEFT_RIGHT_VALUES:
                    ControllerEventProcessor.ControllerEvent<ControllerEventProcessor.DriveValue> driveCommand = event;
                    ControllerEventProcessor.DriveValue v = driveCommand.payload;
                    handler.handleDriveCommand (v.getLeftValue(), v.getRightValue());
                    break;

                case LOGS:
                    handler.handleLogs();
                    break;

                case NOISE:
                    handler.handleNoise();
                    break;

                case INDICATOR_LEFT:
                    handler.handleIndicatorLeft();
                    break;

                case INDICATOR_RIGHT:
                    handler.handleIndicatorRight();
                    break;

                case INDICATOR_STOP:
                    handler.handleIndictorStop();
                    break;

                case DRIVE_MODE:
                    handler.handleDriveMode();
                    break;
            }
        });
    }

    // Controller event handler
    private class ControllerHandler {
        private void handleDriveCommand(Float l, Float r) {
            vehicleControl = new ControlSignal(l, r);
            sendControlToVehicle(vehicleControl);
        }

        private void handleLogs() {
            setIsLoggingActive(!getLoggingEnabled());
            if (getLoggingEnabled()) audioPlayer.play(voice, "logging_started.mp3");
            else audioPlayer.play(voice, "logging_stopped.mp3");
        }

        private void handleNoise() {
            toggleNoise();
            if (getNoiseEnabled()) audioPlayer.play(voice, "noise_enabled.mp3");
            else audioPlayer.play(voice, "noise_disabled.mp3");
        }

        private void handleIndicatorLeft() {
            vehicleIndicator = 1;
            if (getLoggingEnabled()) {
                sendIndicatorToSensorService(vehicleIndicator);
            }
            sendIndicatorToVehicle(vehicleIndicator);
        }

        private void handleIndicatorRight() {
            vehicleIndicator = 0;
            if (getLoggingEnabled()) {
                sendIndicatorToSensorService(vehicleIndicator);
            }
            sendIndicatorToVehicle(vehicleIndicator);
        }

        private void handleIndictorStop() {
            vehicleIndicator = -1;
            if (getLoggingEnabled()) {
                sendIndicatorToSensorService(vehicleIndicator);
            }
            sendIndicatorToVehicle(vehicleIndicator);
        }

        private void handleDriveMode() {
            if (driveByNetwork) return;
            switch (driveMode) {
                case DUAL:
                case SMARTPHONE:
                    setDriveMode(DriveMode.GAME);
                    audioPlayer.play(voice, "video_game_control.mp3");
                    break;
                case GAME:
                    setDriveMode(DriveMode.JOYSTICK);
                    audioPlayer.play(voice, "joystick_control.mp3");
                    break;
                case JOYSTICK:
                    setDriveMode(DriveMode.DUAL);
                    audioPlayer.play(voice, "dual_drive_control.mp3");
                    break;
            }
            driveModeSpinner.setSelection(driveMode.ordinal());
        }
    }
}
