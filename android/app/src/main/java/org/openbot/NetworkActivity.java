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
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.util.TypedValue;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.openbot.customview.OverlayView;
import org.openbot.customview.OverlayView.DrawCallback;
import org.openbot.env.AudioPlayer;
import org.openbot.env.BorderedText;
import org.openbot.env.ImageUtils;
import org.openbot.env.Logger;
import org.openbot.env.PoseNetUtils;
import org.openbot.tflite.Autopilot;
import org.openbot.tflite.Detector;
import org.openbot.tflite.Network.Device;
import org.openbot.tflite.Network.Model;
import org.openbot.tracking.KeyPointsTracker;
import org.openbot.tracking.MultiBoxTracker;
import org.tensorflow.lite.examples.posenet.lib.BodyPart;
import org.tensorflow.lite.examples.posenet.lib.KeyPoint;
import org.tensorflow.lite.examples.posenet.lib.Person;
import org.tensorflow.lite.examples.posenet.lib.Posenet;
import org.tensorflow.lite.examples.posenet.lib.Position;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.sqrt;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class NetworkActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();

    // Posenet fields and constants
    List<Pair<BodyPart, BodyPart>> bodyJoints = Arrays.asList(
            new Pair(BodyPart.LEFT_WRIST, BodyPart.LEFT_ELBOW),
            new Pair(BodyPart.LEFT_ELBOW, BodyPart.LEFT_SHOULDER),
            new Pair(BodyPart.LEFT_SHOULDER, BodyPart.RIGHT_SHOULDER),
            new Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW),
            new Pair(BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST),
            new Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_HIP),
            new Pair(BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP),
            new Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_SHOULDER),
            new Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE),
            new Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
            new Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE),
            new Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
    );

    List<Integer> list = Arrays.asList(1, 2, 3);


    private static final int MODEL_WIDTH = 257;
    private static final int MODEL_HEIGHT = 257;
    private float minConfidencePose = 0.50f;
    public Posenet posenet;
    private float circleRadius = 8.0f;
    private SurfaceHolder surfaceHolder;
    public Paint paint = new Paint();

    // Minimum detection confidence to track a detection.
    private static final float minConfidenceOD = 0.55f;
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
    private boolean PosenetOn = false;
    private long frameNum = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;
    private KeyPointsTracker keyPointsTracker;
    private BorderedText borderedText;

    private AudioPlayer audioPlayer;
    private String voice;
    private Canvas mCanvas;
    private float scaledRatio;
    private long lastFrame = 0;
    private long numFrameCheckingFall = 10;
    private int lastCenter;
    private int t2;
    private int t1 = 0;


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

        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
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
                    new Runnable() {
                        @Override
                        public void run() {
                            if (detector != null) {
                                LOGGER.i("Running detection on image " + currFrameNum);
                                long startTime = SystemClock.uptimeMillis();
                                final List<Detector.Recognition> results = detector.recognizeImage(croppedBitmap);
                                lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

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
                                final Canvas canvas = new Canvas(cropCopyBitmap);
                                final Paint paint = new Paint();
                                paint.setColor(Color.RED);
                                paint.setStyle(Style.STROKE);
                                paint.setStrokeWidth(2.0f);

                                float minimumConfidence = minConfidenceOD;


                                final List<Detector.Recognition> mappedRecognitions =
                                        new LinkedList<Detector.Recognition>();
                                for (final Detector.Recognition result : results) {
                                    final RectF location = result.getLocation();
                                    if (location != null && result.getConfidence() >= minimumConfidence) {
                                        canvas.drawRect(location, paint);
                                        cropToFrameTransform.mapRect(location);
                                        result.setLocation(location);
                                        mappedRecognitions.add(result);
                                    }
                                }


//                                Bitmap imageBitmap = Bitmap.createBitmap(getRgbBytes(),
//                                        getDesiredPreviewFrameSize().getWidth(),
//                                        getDesiredPreviewFrameSize().getHeight(),
//                                        Config.ARGB_8888);

//                                startTime = SystemClock.uptimeMillis();
//
//                                Matrix rotateMatrix = new Matrix();
//                                rotateMatrix.postRotate(90.0f);
//
//                                Bitmap rotatedBitmap = Bitmap.createBitmap(
//                                        rgbFrameBitmap, 0, 0,
//                                        getDesiredPreviewFrameSize().getWidth(),
//                                        getDesiredPreviewFrameSize().getHeight(),
//                                        rotateMatrix, true);
//
//
//                                // Crop bitmap.
//                                Bitmap croppedBitmapPoseNet = cropBitmap(rotatedBitmap);
//
//                                // Compute the scaledRatio only at the first inference
//                                if (scaledRatio  ==  0.0f) {
//                                    scaledRatio = croppedBitmapPoseNet.getHeight() / MODEL_HEIGHT;
//
//                                    Log.d( "scaledRatio", String.format("scaledRatio is %f", scaledRatio));
//                                }
//                                // Created scaled version of bitmap for model input.
//                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(croppedBitmapPoseNet, MODEL_WIDTH, MODEL_HEIGHT, true);
//
//                                // Perform inference.
//                                Person person = posenet.estimateSinglePose(scaledBitmap);
//
//                                // Scale Back the point so it will fit with canvas
//                                scaleBack(person);
//
//                                lastProcessingTimeMs += SystemClock.uptimeMillis() - startTime;

                                if( !results.isEmpty() ) {
//                                    if (frameNum == 0) {
//                                        setUpConditionChecking(person);
//                                    }
                                    if (frameNum - lastFrame >= numFrameCheckingFall){
                                        Log.d("Checking Fall events", "Conditions are checking...");
                                        fallDetection( //person,
                                                results);
                                        lastFrame = frameNum;
                                    } else if (frameNum - lastFrame < numFrameCheckingFall) {
                                        t2 += lastProcessingTimeMs;
                                    }
                                }



                                tracker.trackResults(mappedRecognitions, currFrameNum);
//                                tracker.trackKeypoint(person, currFrameNum);
                                vehicleControl = tracker.updateTarget();
                                trackingOverlay.postInvalidate();


                            } else if (autoPilot != null) {
                                LOGGER.i("Running autopilot on image " + currFrameNum);
                                final long startTime = SystemClock.uptimeMillis();
                                vehicleControl = autoPilot.recognizeImage(croppedBitmap, vehicleIndicator);
                                lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
                            }

                            // In case control was removed from network during inference
                            if (!driveByNetwork) {
                                vehicleControl = new ControlSignal(0, 0);
                            }

                            if (getLoggingEnabled()) {
                                sendInferenceTimeToSensorService(currFrameNum, lastProcessingTimeMs);
                            }

                            computingNetwork = false;

                            updateVehicleState();

//                            if (PosenetOn) {
//                                final long startTime = SystemClock.uptimeMillis();
//                                // Crop bitmap.
//                                croppedBitmap = cropBitmap(croppedBitmap);
////                                surfaceHolder = getSurfaceHolder();
////                                Canvas canvas = surfaceHolder.lockCanvas();
////                                if (canvas == null) {
////                                    Log.d("#Surface Holder", "Surface holder is null");
////                                    return;
////                                }
//
//                                // Created scaled version of bitmap for model input.
//                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, MODEL_WIDTH, MODEL_HEIGHT, true);
//
//                                // Perform inference.
//                                Person person = posenet.estimateSinglePose(scaledBitmap);
//                                Log.d("#Surface Holder", "Surface holder is not null");
//                                draw(canvas, person, scaledBitmap);
//                                lastProcessingTimeMs += SystemClock.uptimeMillis() - startTime;
//                            }

                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            // showFrameInfo(previewWidth + "x" + previewHeight);
                                            // showCropInfo(croppedBitmap.getWidth() + "x" + croppedBitmap.getHeight());
                                            showInference(lastProcessingTimeMs + "ms");
                                        }
                                    });
                        }
                    });
        }

    }

    private void setUpConditionChecking(Person person) {
        KeyPoint left_hip = person.getKeyPoints().get(11);
        KeyPoint right_hip = person.getKeyPoints().get(12);

        lastCenter = (left_hip.getPosition().getY() + right_hip.getPosition().getY()) / 2;
    }

    private void fallDetection( List<Detector.Recognition> recognitions) {

//        /// First Condition
//        KeyPoint left_hip = person.getKeyPoints().get(11);
//        KeyPoint right_hip = person.getKeyPoints().get(12);
//
//        // if we can't get left-hip score or right-hip scores is less than threshold, we won't check it
//        if (left_hip.getScore() < minConfidencePose || right_hip.getScore() < minConfidencePose) {
//            return;
//        }
//
//
//        float meterToPixel = (float) 600 / 1.2f;
//        float centerY = (left_hip.getPosition().getY() + right_hip.getPosition().getY()) / 2;
//        float velocity = (centerY - lastCenter) / (t2 - t1);
//        t2 = t1 = 0;
//
//        Log.d( "Velocity", String.format("Velocity: %f", velocity) );
//        Toast.makeText(this, String.format("Velocity: %f", velocity), Toast.LENGTH_SHORT);
//
//        // If velocity is smaller than 0.09m/s, we wont check second condition
//        if (velocity < 0.09f * meterToPixel) {
//            return;
//        }
//        Toast.makeText(this, String.format("Real Velocity: %f", velocity / meterToPixel), Toast.LENGTH_SHORT);

//        // Second condition
//        KeyPoint left_ankle = person.getKeyPoints().get(15);
//        KeyPoint right_ankle = person.getKeyPoints().get(16);
////        KeyPoint nose = person.getKeyPoints().get(0);
//        float centerX = (left_hip.getPosition().getX() + right_hip.getPosition().getX()) / 2;
//
//        if (left_ankle.getScore() < minConfidencePose || left_ankle.getScore() < minConfidencePose ) {
//            return;
//        }
//        int s_barX = (left_ankle.getPosition().getX() + right_ankle.getPosition().getX()) / 2;
//        int s_barY = (left_ankle.getPosition().getY() + right_ankle.getPosition().getY()) / 2;
//
//        float angle = (float) atan((s_barY - centerY)/ (s_barX - centerX));
//        if (angle > 45){
//            return;
//        }


        for (Detector.Recognition person : recognitions){
            if (person.getConfidence() < minConfidenceOD){
                return;
            }

            // Third condition
            float widthToHeightRatio = abs(person.getLocation().height() / person.getLocation().width());
            Log.d("Width to height ratio", String.format("widthToHeightRation %f", widthToHeightRatio));
            if (widthToHeightRatio < 1){
                return;
            }
            Log.d("Alert", " fall event detected");
            Toast.makeText(this, String.format("Alert: fall event detected, Width to Height ratio: %f", widthToHeightRatio), Toast.LENGTH_SHORT).show();
        }
    }

    // The bitmap canvas draw and the bitmap posenet infers on are not the same.
    // So if we just proceed draw it the y coordinate  of all keypoint are wrong.
    // We realise we just need to scale and offset so it will draw correctly
    private void scaleBack(Person person) {
        if (getScreenOrientation() == 0 || getScreenOrientation() == 180){
            for (KeyPoint keyPoint : person.getKeyPoints()) {
                int y = keyPoint.getPosition().getY();
                keyPoint.getPosition().setY((int) (y / scaledRatio )) ;
            }
        }

        if (getScreenOrientation() == 90 || getScreenOrientation() == 270){
            for (KeyPoint keyPoint : person.getKeyPoints()) {
                int x = keyPoint.getPosition().getX();
                keyPoint.getPosition().setX((int) (x / scaledRatio));
            }
        }

    }

    private Bitmap cropBitmap(Bitmap croppedBitmap) {
//        Matrix matrix = new Matrix();
//        matrix.preRotate(180f);
//        matrix.preScale(-1f, 1f);
//        Bitmap rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(), matrix, false);
        float bitmapRatio = (float) croppedBitmap.getHeight() / croppedBitmap.getWidth();
        float modelInputRatio = (float) MODEL_HEIGHT / MODEL_WIDTH;

        // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
        double maxDifference = 1e-5;

        // Checks if the bitmap has similar aspect ratio as the required model input.
        if(abs(modelInputRatio - bitmapRatio) < maxDifference)
            return croppedBitmap;
        else if (modelInputRatio < bitmapRatio) {
            float cropHeight = (float) croppedBitmap.getHeight() - (croppedBitmap.getWidth() / modelInputRatio);
            croppedBitmap = Bitmap.createBitmap(
                    croppedBitmap,
                    0,
                    (int) (cropHeight / 2),
                    croppedBitmap.getWidth(),
                    (int) (croppedBitmap.getHeight() - cropHeight));
        }
        else{
            float cropWidth = (float) croppedBitmap.getWidth() - (croppedBitmap.getHeight() * modelInputRatio);
            croppedBitmap = Bitmap.createBitmap(
                    croppedBitmap,
                    (int) (cropWidth / 2),
                    0,
                    (int) (croppedBitmap.getWidth() - cropWidth),
                    croppedBitmap.getHeight());
        }
        return croppedBitmap;

    }

    void updateVehicleState() {
        float noise = 0;
        int noiseDirection = 0;
        long noiseDuration = 5000;
        long noiseStartTime = 0;
        long noiseTimeout = 10000;
        // Update GUI
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.i("display_ctrl", "runnable");
                        showControl(
                                String.format(
                                        Locale.US, "%.2f,%.2f", vehicleControl.getLeft(), vehicleControl.getRight()));
                    }
                });

        // Log controls
        if (getLoggingEnabled()) {
            runInBackground(() -> sendControlToSensorService(vehicleControl));
        }

        if (noiseEnabled) {
            long currentTime = SystemClock.uptimeMillis();
            if (currentTime > noiseStartTime + noiseDuration + noiseTimeout) {
                noiseStartTime = currentTime;
                noiseDuration = generateRandomInt(1, 2) * 1000;
                noise = 0;
                Random r = new Random();
                noiseDirection = r.nextBoolean() ? 1 : -1;
            }
            if (currentTime < noiseStartTime + noiseDuration) {
                if (currentTime < noiseStartTime + noiseDuration / 2) {
                    noise += (float) generateRandomInt(1, 8) / 255;
                } else {
                    noise -= (float) generateRandomInt(1, 8) / 255;
                }
                noise = Math.max(0, Math.min(noise, 0.5f));
                LOGGER.d("Injecting Noise: " + noise);
                if (noiseDirection < 0)
                    sendControlToVehicle(
                            new ControlSignal(vehicleControl.getLeft() - noise, vehicleControl.getRight()));
                else
                    sendControlToVehicle(
                            new ControlSignal(vehicleControl.getLeft(), vehicleControl.getRight() - noise));
            } else {
                sendControlToVehicle(vehicleControl);
            }
        } else {
            sendControlToVehicle(vehicleControl);
        }
    }

    public static int generateRandomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private boolean getNoiseEnabled() {
        return noiseEnabled;
    }

    private void toggleNoise() {
        noiseEnabled = !noiseEnabled;
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
            driveModeSwitchCompat.setText("Network");
        } else {
            vehicleControl = new ControlSignal(0, 0);
            updateVehicleState();
            driveModeSwitchCompat.setText("Controller");
        }
        driveModeSwitchCompat.setChecked(driveByNetwork);
    }

    @Override
    protected void PosenetOn(boolean isChecked) {
        PosenetOn = isChecked;
        if (PosenetOn) {
            posenetSwitchCompat.setText("On");
        } else {
            posenetSwitchCompat.setText("Off");
        }
        posenetSwitchCompat.setChecked(PosenetOn);
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
        runInBackground(() -> posenet = new Posenet(getContext(), getDevice_posenet(), getNumThreadsPosenet()));
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

                // Process the current movement sample in the batch (position -1)
                vehicleControl = gameController.processJoystickInput(event, -1);
                updateVehicleState();
                return true;
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
                    vehicleIndicator = 1;
                    if (getLoggingEnabled()) {
                        sendIndicatorToSensorService(vehicleIndicator);
                    }
                    sendIndicatorToVehicle(vehicleIndicator);
                    return true;
                case KeyEvent.KEYCODE_BUTTON_Y:
                    vehicleIndicator = 0;
                    if (getLoggingEnabled()) {
                        sendIndicatorToSensorService(vehicleIndicator);
                    }
                    sendIndicatorToVehicle(vehicleIndicator);
                    return true;
                case KeyEvent.KEYCODE_BUTTON_X:
                    vehicleIndicator = -1;
                    if (getLoggingEnabled()) {
                        sendIndicatorToSensorService(vehicleIndicator);
                    }
                    sendIndicatorToVehicle(vehicleIndicator);
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

    @Override
    public synchronized void onStart() {
        super.onStart();
        Log.d("#Posenet Initialization", "Instantiating PoseNet");
        final long startTime = SystemClock.uptimeMillis();
        posenet = new Posenet(getContext(), getDevice_posenet(), getNumThreadsPosenet());
        final long time = SystemClock.uptimeMillis() - startTime;
        Log.d("#Posenet Initialization", String.format("Instantiated PoseNet: takes %d ms", time));
    }

    @Override
    public synchronized void onStop() {
        super.onStop();
        posenet.close();
        Log.d("#Posenet Destruction", "Destructing PoseNet");
    }
}
