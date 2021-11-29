package org.openbot.original;


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
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.openbot.R;
import org.openbot.customview.OverlayView;
import org.openbot.customview.OverlayView.DrawCallback;
import org.openbot.env.BorderedText;
import org.openbot.env.BotToControllerEventBus;
import org.openbot.env.ImageUtils;
import org.openbot.env.Logger;
import org.openbot.tflite.Autopilot;
import org.openbot.tflite.Detector;
import org.openbot.tflite.Model;
import org.openbot.tflite.Network.Device;
import org.openbot.tracking.MultiBoxTracker;
import org.openbot.utils.ConnectionUtils;
import org.openbot.utils.Enums.ControlMode;
import org.openbot.utils.Enums.LogMode;



import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import org.jsoup.select.Elements;
import org.openbot.env.Vehicle;

//import org.openbot.env.SharedPreferencesManager;


import java.util.ArrayList;
import java.util.Collections;


public class PlayActivity extends CameraActivity2 implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();

    // Minimum detection confidence to track a detection.
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(1280, 720); // 16:9

    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private Integer sensorOrientation;

    private Detector detector;
    private Autopilot autopilot;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingNetwork = false;
    private long frameNum = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;
    private BorderedText borderedText;

    final Bundle bundle = new Bundle();
    ArrayList x_list = new ArrayList();
    ArrayList y_list = new ArrayList();
    ArrayList angle = new ArrayList();
    ArrayList distance = new ArrayList();
    private Vehicle vehicle;


    ArrayList<Double> movingLength = new ArrayList<Double>();
    ArrayList<Double> movingDegree = new ArrayList<Double>();


    double degree;
    double gyro;

    //센서 받아오는 변수들


    //Using the Accelometer & Gyroscoper
    private SensorManager mSensorManager = null;

    //Using the Gyroscope
    private SensorEventListener mGyroLis;
    private Sensor mGgyroSensor = null;

    //Roll and Pitch
    private double pitch;
    private double roll;
    private double yaw;

    //timestamp and dt
    private double timestamp;
    private double dt;

    // for radian -> dgree
    private double RAD2DGR = 180 / Math.PI;
    private static final float NS2S = 1.0f / 1000000000.0f;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_play);
//        request();

        Button sendBtn = findViewById(R.id.SendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });

    }

    private void request() {
        EditText gx_num = findViewById(R.id.gx);
        EditText gy_num = findViewById(R.id.gy);

                x_list.clear();
                y_list.clear();


                String str = gx_num.getText().toString() + "/" + gy_num.getText().toString();
                String url = "https://mysterious-sea-88696.herokuapp.com/" + str;

                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();


                //Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();


                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        Bundle bundle = msg.getData();
                        String temp = bundle.getString("hi");
                        temp.trim();
                        temp = temp.replace(" ", "");
                        temp = temp.replace("[", "");
                        temp = temp.replace("]", "");

                        String[] arr = temp.split(",");

                        for (int i = 0; i < arr.length; i++) {
                            if (i % 2 == 0) {
                                x_list.add(arr[i]);
                            } else {
                                y_list.add(arr[i]);
                            }
                        }


                        Collections.reverse(x_list);
                        Collections.reverse(y_list);


                        System.out.println("사이즈는 " + x_list.size());
                        System.out.println("사이즈는 " + y_list.size());


                        System.out.println("x임다");

                        for (int i = 0; i < x_list.size(); i++) {
                            System.out.println(x_list.get(i));
                        }

                        System.out.println("y임다");

                        for (int i = 0; i < y_list.size(); i++) {
                            System.out.println(y_list.get(i));
                        }



                        Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();

                    }
                };


                runInBackground(
                    () -> {
                        String title = "";


                        Document doc = null;
                        try {
                            doc = Jsoup.connect(url).get();
                            Elements mElementDatas = doc.select("body");
                            title = mElementDatas.text();
//                            for(Element elem : mElementDatas){
//                                title = elem.select("body").text();
//                            }
                            bundle.putString("hi", title);
                            Message msg = handler.obtainMessage();
                            msg.setData(bundle);
                            handler.sendMessage(msg);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                });

        tracking();

    }

    private void tracking() {
        angle();
        distance();
        getGyro();

        TextView trackText = findViewById(R.id.trackText);
        TextView sensorText = findViewById(R.id.sensorText);



        runInBackground(t2);

    }

    private void angle() {
        for (int i = 0; i < x_list.size() - 1; i++) {
            double current = 0;

            double gap_y = Double.parseDouble(y_list.get(i + 1).toString()) - Double.parseDouble(y_list.get(i).toString());
            double gap_x = Double.parseDouble(x_list.get(i + 1).toString()) - Double.parseDouble(x_list.get(i).toString());


            current = (Math.atan2(gap_y, gap_x) * 180) / Math.PI;
            //initialDegree.add(current);
            movingDegree.add(current);
        }

//        for (int i = 0; i < initialDegree.size(); i++) {
//            double movingAngle = initialDegree.get(i);
//            if (i == 0) {
//                movingDegree.add(movingAngle);
//            } else {
//                movingAngle -= initialDegree.get(i - 1);
//                movingDegree.add(movingAngle);
//            }
//        }


        System.out.println("앵글값");

        for (int i = 0; i < movingDegree.size(); i++) {
            System.out.println(movingDegree.get(i));
        }
    }

    private void distance() {
        for (int i = 0; i < x_list.size() - 1; i++) {

            double gap_y = Double.parseDouble(y_list.get(i + 1).toString()) - Double.parseDouble(y_list.get(i).toString());
            double gap_x = Double.parseDouble(x_list.get(i + 1).toString()) - Double.parseDouble(x_list.get(i).toString());

            double current = Math.sqrt(((gap_x) * (gap_x)) + ((gap_y) * (gap_y)));
            movingLength.add(current);
        }


        System.out.println("거리값");

        for (int i = 0; i < movingLength.size(); i++) {
            System.out.println(movingLength.get(i));
        }
    }

    private int getGyro() {

        //Using the Gyroscope & Accelometer
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Using the Accelometer
        mGgyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGyroLis = new GyroscopeListener();
        Button getBtn = findViewById(R.id.getBtn);
        mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_UI);

        return 1;

    }


    private class GyroscopeListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {

            /* 각 축의 각속도 성분을 받는다. */
            double gyroX = event.values[0];
            double gyroY = event.values[1];
            double gyroZ = event.values[2];

            /* 각속도를 적분하여 회전각을 추출하기 위해 적분 간격(dt)을 구한다.
             * dt : 센서가 현재 상태를 감지하는 시간 간격
             * NS2S : nano second -> second */
            dt = (event.timestamp - timestamp) * NS2S;
            timestamp = event.timestamp;

            /* 맨 센서 인식을 활성화 하여 처음 timestamp가 0일때는 dt값이 올바르지 않으므로 넘어간다. */
            if (dt - timestamp * NS2S != 0) {

                /* 각속도 성분을 적분 -> 회전각(pitch, roll)으로 변환.
                 * 여기까지의 pitch, roll의 단위는 '라디안'이다.
                 * SO 아래 로그 출력부분에서 멤버변수 'RAD2DGR'를 곱해주어 degree로 변환해줌.  */
                pitch = pitch + gyroY * dt;
                roll = roll + gyroX * dt;
                yaw = yaw + gyroZ * dt;

                if(roll<-360||roll>360){
                    roll=0;
                }

                degree = roll * RAD2DGR;

                String dtos = String.valueOf(degree);

                Log.e("LOG", "GYROSCOPE           [X]:" + String.format("%.4f", event.values[0])
                        + "           [Y]:" + String.format("%.4f", event.values[1])
                        + "           [Z]:" + String.format("%.4f", event.values[2])
                        + "           [Pitch]: " + String.format("%.1f", pitch * RAD2DGR)
                        + "           [Roll]: " + String.format("%.1f", roll * RAD2DGR)
                        + "           [Yaw]: " + String.format("%.1f", yaw * RAD2DGR)
                        + "           [dt]: " + String.format("%.4f", dt));

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    Thread t2 = new Thread() {


        @Override
        public void run() {
            for (int i = 0; i<movingDegree.size();i++){

                double range = (Double.parseDouble(movingDegree.get(i).toString()));

                //아두이노에 회전 명령(왼쪽이면 양수, 오른쪽 회전이면 음수)
                while(degree < range - 10 || degree > range + 10) {
                    if (range > degree) {
                        vehicle.sendControl(-130, 0);
                    }
                    else
                        vehicle.sendControl(0,-130);

                    try {
                        System.out.println("멈춤");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //직진 명령
                long t= System.currentTimeMillis();
                long end = t+(new Double(Double.parseDouble(movingLength.get(i).toString())*1000*0.16)).longValue();
                while(System.currentTimeMillis() < end) {
                    vehicle.sendControl(255, 255);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //거리 계산한것 만큼 아두이노로 start 신호 보냄.
            }
        }
    };

    @Override
    protected void processImage() {
        ++frameNum;
        final long currFrameNum = frameNum;
        // trackingOverlay.postInvalidate();

        // If network is busy and we don't need to log any image, return.
        if (computingNetwork && !loggingEnabled) {
            readyForNextImage();
            return;
        }

        final boolean SAVE_PREVIEW_BITMAP =
                logMode.equals(LogMode.ALL_IMGS) || logMode.equals(LogMode.PREVIEW_IMG);
        final boolean SAVE_CROP_BITMAP =
                logMode.equals(LogMode.ALL_IMGS) || logMode.equals(LogMode.CROP_IMG);

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
        if (loggingEnabled && SAVE_PREVIEW_BITMAP) {
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
        if (loggingEnabled && SAVE_CROP_BITMAP) {
            runInBackground(
                    () ->
                            ImageUtils.saveBitmap(
                                    croppedBitmap,
                                    logFolder + File.separator + "images",
                                    currFrameNum + "_crop.jpeg"));
            sendFrameNumberToSensorService(currFrameNum);
        }

        // Network is control of the vehicle
        if (networkEnabled) {
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
                            final List<Detector.Recognition> results =
                                    detector.recognizeImage(croppedBitmap, "person");
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
                                    if (t2.isAlive())
                                        t2.interrupt();
                                    canvas1.drawRect(location, paint);
                                    cropToFrameTransform.mapRect(location);
                                    result.setLocation(location);
                                    mappedRecognitions.add(result);
                                }
                            }

                            tracker.trackResults(mappedRecognitions, currFrameNum);
                            controllerHandler.handleDriveCommand(tracker.updateTarget());
                            trackingOverlay.postInvalidate();
                        } else if (autopilot != null) {
                            LOGGER.i("Running autopilot on image " + currFrameNum);
                            final long startTime = SystemClock.elapsedRealtime();
                            controllerHandler.handleDriveCommand(
                                    autopilot.recognizeImage(croppedBitmap, vehicle.getIndicator()));
                            lastProcessingTimeMs = SystemClock.elapsedRealtime() - startTime;
                        }

                        computingNetwork = false;

                        if (loggingEnabled) {
                            sendInferenceTimeToSensorService(currFrameNum, lastProcessingTimeMs);
                        }
                    });
        }

        runOnUiThread(
                () -> {
                    frameValueTextView.setText(
                            String.format(Locale.US, "%d x %d", previewWidth, previewHeight));
                    cropValueTextView.setText(
                            String.format(
                                    Locale.US, "%d x %d", croppedBitmap.getWidth(), croppedBitmap.getHeight()));
                    if (networkEnabled)
                        inferenceTimeTextView.setText(String.format(Locale.US, "%d ms", lastProcessingTimeMs));
                });
    }

    @Override
    protected void onPreviewSizeChosen(final Size size,final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        tracker = new MultiBoxTracker(this);

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);

        recreateNetwork(getModel(), getDevice(), getNumThreads());
        if (detector == null && autopilot == null) {
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
    protected int getLayoutId() {
        return R.layout.camera_connection_fragment_tracking;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
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

    @Override
    protected void toggleNoise() {
        noiseEnabled = !noiseEnabled;
        BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("NOISE", noiseEnabled));
        if (noiseEnabled) {
            vehicle.startNoise();
        } else vehicle.stopNoise();
        updateVehicleControl();
    }

    @Override
    protected void updateVehicleControl() {

        // Log controls
        if (loggingEnabled) {
            runInBackground(this::sendControlToSensorService);
        }

        // Update GUI
//        runOnUiThread(
//            () -> {
//                Log.i("display_ctrl", "runnable");
//                if (controlValueTextView != null)
//                    controlValueTextView.setText(
//                            String.format(
//                                    Locale.US, "%.0f,%.0f", vehicle.getLeftSpeed(), vehicle.getRightSpeed()));
//            });
    }

    @Override
    protected void setNetworkEnabled(boolean isChecked) {
        networkEnabled = isChecked;
        if (networkEnabled) {
            networkSwitchCompat.setText(R.string.on);
        } else {
            runInBackground(
                    () -> {
                        try {
                            TimeUnit.MILLISECONDS.sleep(lastProcessingTimeMs);
                            controllerHandler.handleDriveCommand(0.f, 0.f);
                            runOnUiThread(() -> inferenceTimeTextView.setText(R.string.time_ms));
                        } catch (InterruptedException e) {
                            LOGGER.e(e, "Got interrupted.");
                        }
                    });
            networkSwitchCompat.setText(R.string.off);
        }

        networkSwitchCompat.setChecked(networkEnabled);
        if (networkEnabled) {
            controlModeSpinner.setAlpha(0.5f);
            driveModeSpinner.setAlpha(0.5f);
            speedModeSpinner.setAlpha(0.5f);
        } else {
            controlModeSpinner.setAlpha(1.0f);
            driveModeSpinner.setAlpha(1.0f);
            speedModeSpinner.setAlpha(1.0f);
        }
        controlModeSpinner.setEnabled(!networkEnabled);
        driveModeSpinner.setEnabled(!networkEnabled);
        speedModeSpinner.setEnabled(!networkEnabled);
    }

    private void recreateNetwork(Model model, Device device, int numThreads) {
        if (model == null) return;
        tracker.clearTrackedObjects();
        if (detector != null) {
            LOGGER.d("Closing detector.");
            detector.close();
            detector = null;
        }
        if (autopilot != null) {
            LOGGER.d("Closing autoPilot.");
            autopilot.close();
            autopilot = null;
        }

        try {
            if (model.type == Model.TYPE.DETECTOR) {
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
                autopilot = Autopilot.create(this, model, device, numThreads);
                croppedBitmap =
                        Bitmap.createBitmap(
                                autopilot.getImageSizeX(), autopilot.getImageSizeY(), Config.ARGB_8888);
                frameToCropTransform =
                        ImageUtils.getTransformationMatrix(
                                previewWidth,
                                previewHeight,
                                croppedBitmap.getWidth(),
                                croppedBitmap.getHeight(),
                                sensorOrientation,
                                autopilot.getCropRect(),
                                autopilot.getMaintainAspect());
            }

            cropToFrameTransform = new Matrix();
            frameToCropTransform.invert(cropToFrameTransform);

        } catch (IllegalArgumentException | IOException e) {
            String msg = "Failed to create network: ";
            LOGGER.e(e, msg);
            Toast.makeText(this, msg + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        // Make sure vehicle is not controlled by network
        if (!networkEnabled) {
            // Check that the event came from a game controller
            if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
                    && event.getAction() == MotionEvent.ACTION_MOVE
                    && controlMode == ControlMode.GAMEPAD) {
                // Process the current movement sample in the batch (position -1)
                controllerHandler.handleDriveCommand(gameController.processJoystickInput(event, -1));
                return true;
            }
        }
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Check that the event came from a game controller
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD
                && controlMode == ControlMode.GAMEPAD) {
            // Only handle key once (when released)
            if (event.getAction() == KeyEvent.ACTION_UP) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_BUTTON_A: // x
                        controllerHandler.handleLogging();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_X: // square
                        controllerHandler.handleIndicatorLeft();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_Y: // triangle
                        controllerHandler.handleIndicatorStop();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_B: // circle
                        controllerHandler.handleIndicatorRight();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_START: // options
                        controllerHandler.handleNoise();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_L1:
                        controllerHandler.handleDriveMode();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_R1:
                        controllerHandler.handleNetwork();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBL:
                        controllerHandler.handleSpeedDown();
                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBR:
                        controllerHandler.handleSpeedUp();
                        break;
                    default:
                        //               makeText(this,"Key " + event.getKeyCode() + " not recognized",
                        //                       LENGTH_SHORT).show();
                        break;
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
