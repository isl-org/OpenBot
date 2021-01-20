/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Modified by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot.robot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;
import org.openbot.R;
import org.openbot.common.Constants;
import org.openbot.common.Enums;
import org.openbot.common.Enums.ControlMode;
import org.openbot.common.Enums.DriveMode;
import org.openbot.common.Enums.LogMode;
import org.openbot.common.Enums.SpeedMode;
import org.openbot.env.AudioPlayer;
import org.openbot.env.BotToControllerEventBus;
import org.openbot.env.ControllerToBotEventBus;
import org.openbot.env.GameController;
import org.openbot.env.ImageUtils;
import org.openbot.env.Logger;
import org.openbot.env.PhoneController;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.env.Vehicle;
import org.openbot.tflite.Model;
import org.openbot.tflite.Network.Device;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

public abstract class CameraActivity extends AppCompatActivity
    implements OnImageAvailableListener,
        Camera.PreviewCallback,
        CompoundButton.OnCheckedChangeListener,
        ServerService.ServerListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {
  private static final Logger LOGGER = new Logger();

  // Constants
  private static final int REQUEST_CAMERA_PERMISSION = 1;
  private static final int REQUEST_LOCATION_PERMISSION_LOGGING = 2;
  private static final int REQUEST_LOCATION_PERMISSION_CONTROLLER = 3;
  private static final int REQUEST_STORAGE_PERMISSION = 4;
  private static final int REQUEST_BLUETOOTH_PERMISSION = 5;

  private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
  private static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
  private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
  private static final String PERMISSION_BLUETOOTH = Manifest.permission.BLUETOOTH;

  private static Context context;
  private int cameraSelection = CameraCharacteristics.LENS_FACING_BACK;
  protected int previewWidth = 0;
  protected int previewHeight = 0;
  private final boolean debug = false;
  private Handler handler;
  private HandlerThread handlerThread;
  private boolean useCamera2API;
  private boolean isProcessingFrame = false;
  private final byte[][] yuvBytes = new byte[3][];
  private int[] rgbBytes = null;
  private int yRowStride;
  private Runnable postInferenceCallback;
  private Runnable imageConverter;

  private LinearLayout bottomSheetLayout;
  private LinearLayout gestureLayout;
  private BottomSheetBehavior sheetBehavior;

  protected SwitchCompat connectionSwitchCompat,
      networkSwitchCompat,
      logSwitchCompat,
      cameraSwitchCompat;
  protected TextView frameValueTextView,
      cropValueTextView,
      inferenceTimeTextView,
      controlValueTextView;
  protected ImageView bottomSheetArrowImageView;
  private ImageView plusImageView, minusImageView;
  protected Spinner baudRateSpinner,
      modelSpinner,
      deviceSpinner,
      controlModeSpinner,
      driveModeSpinner,
      logSpinner,
      speedModeSpinner;
  private TextView threadsTextView, voltageTextView, speedTextView, sonarTextView;
  private ArrayAdapter<CharSequence> modelAdapter;
  private Model model = Model.DETECTOR_V1_1_0_Q;
  private Device device = Device.CPU;
  private int numThreads = -1;

  public int[] BaudRates = {9600, 14400, 19200, 38400, 57600, 115200, 230400, 460800, 921600};
  private int baudRate = 115200;
  private LocalBroadcastManager localBroadcastManager;
  private BroadcastReceiver localBroadcastReceiver;

  protected LogMode logMode = LogMode.CROP_IMG;
  protected ControlMode controlMode = ControlMode.GAMEPAD;
  protected SpeedMode speedMode = SpeedMode.NORMAL;
  protected DriveMode driveMode = DriveMode.GAME;
  protected String logFolder;
  protected boolean loggingEnabled;
  protected boolean networkEnabled = false;
  protected boolean noiseEnabled = false;

  private Intent intentSensorService;
  private ServerService serverService;
  private SharedPreferencesManager preferencesManager;
  protected final GameController gameController = new GameController(driveMode);
  private final PhoneController phoneController = new PhoneController();
  protected final ControllerHandler controllerHandler = new ControllerHandler();
  private final AudioPlayer audioPlayer = new AudioPlayer(this);
  private final String voice = "matthew";

  protected Vehicle vehicle;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    LOGGER.d("onCreate " + this);
    super.onCreate(null);
    context = getApplicationContext();
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    vehicle = new Vehicle(this, baudRate);

    setContentView(R.layout.activity_camera);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    if (hasCameraPermission()) {
      setFragment();
    } else {
      requestCameraPermission();
    }

    preferencesManager = new SharedPreferencesManager(this);

    connectionSwitchCompat = findViewById(R.id.connection_switch);
    threadsTextView = findViewById(R.id.threads);
    plusImageView = findViewById(R.id.plus);
    minusImageView = findViewById(R.id.minus);
    networkSwitchCompat = findViewById(R.id.network_switch);
    bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
    gestureLayout = findViewById(R.id.gesture_layout);
    sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
    bottomSheetArrowImageView = findViewById(R.id.bottom_sheet_arrow);
    logSwitchCompat = findViewById(R.id.logger_switch);
    cameraSwitchCompat = findViewById(R.id.camera_toggle_switch);

    baudRateSpinner = findViewById(R.id.baud_rate_spinner);
    ArrayAdapter<CharSequence> baudRateAdapter =
        ArrayAdapter.createFromResource(this, R.array.baud_rates, R.layout.spinner_item);
    baudRateAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
    baudRateSpinner.setAdapter(baudRateAdapter);

    modelSpinner = findViewById(R.id.model_spinner);
    List<CharSequence> models = Arrays.asList(context.getResources().getTextArray(R.array.models));
    modelAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, new ArrayList<>(models));
    modelAdapter.addAll(getModelFiles());
    modelAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
    modelSpinner.setAdapter(modelAdapter);

    deviceSpinner = findViewById(R.id.device_spinner);
    ArrayAdapter<CharSequence> deviceAdapter =
        ArrayAdapter.createFromResource(this, R.array.devices, R.layout.spinner_item);
    deviceAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
    deviceSpinner.setAdapter(deviceAdapter);

    logSpinner = findViewById(R.id.log_spinner);
    ArrayAdapter<CharSequence> logAdapter =
        ArrayAdapter.createFromResource(this, R.array.log_settings, R.layout.spinner_item);
    logAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
    logSpinner.setAdapter(logAdapter);

    controlModeSpinner = findViewById(R.id.control_mode_spinner);
    ArrayAdapter<CharSequence> controlModeAdapter =
        ArrayAdapter.createFromResource(this, R.array.control_modes, R.layout.spinner_item);
    controlModeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
    controlModeSpinner.setAdapter(controlModeAdapter);

    driveModeSpinner = findViewById(R.id.drive_mode_spinner);
    ArrayAdapter<CharSequence> driveModeAdapter =
        ArrayAdapter.createFromResource(this, R.array.drive_modes, R.layout.spinner_item);
    driveModeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
    driveModeSpinner.setAdapter(driveModeAdapter);

    speedModeSpinner = findViewById(R.id.speed_mode_spinner);
    ArrayAdapter<CharSequence> controlAdapter =
        ArrayAdapter.createFromResource(this, R.array.speed_modes, R.layout.spinner_item);
    controlAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
    speedModeSpinner.setAdapter(controlAdapter);

    ViewTreeObserver vto = gestureLayout.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            gestureLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            // int width = bottomSheetLayout.getMeasuredWidth();
            int height = gestureLayout.getMeasuredHeight();

            sheetBehavior.setPeekHeight(height);
          }
        });
    sheetBehavior.setHideable(false);

    sheetBehavior.setBottomSheetCallback(
        new BottomSheetBehavior.BottomSheetCallback() {
          @Override
          public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
              case BottomSheetBehavior.STATE_HIDDEN:
                break;
              case BottomSheetBehavior.STATE_EXPANDED:
                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_down);
                preferencesManager.setSheetExpanded(true);
                break;
              case BottomSheetBehavior.STATE_COLLAPSED:
                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up);
                preferencesManager.setSheetExpanded(false);
                break;
              case BottomSheetBehavior.STATE_DRAGGING:
                break;
              case BottomSheetBehavior.STATE_SETTLING:
                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up);
                break;
            }
          }

          @Override
          public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });

    voltageTextView = findViewById(R.id.voltage_info);
    speedTextView = findViewById(R.id.speed_info);
    sonarTextView = findViewById(R.id.sonar_info);

    frameValueTextView = findViewById(R.id.frame_info);
    cropValueTextView = findViewById(R.id.crop_info);
    inferenceTimeTextView = findViewById(R.id.inference_info);
    controlValueTextView = findViewById(R.id.control_info);

    connectionSwitchCompat.setOnCheckedChangeListener(this);
    networkSwitchCompat.setOnCheckedChangeListener(this);
    logSwitchCompat.setOnCheckedChangeListener(this);
    cameraSwitchCompat.setOnCheckedChangeListener(this);

    plusImageView.setOnClickListener(this);
    minusImageView.setOnClickListener(this);

    baudRateSpinner.setOnItemSelectedListener(this);
    modelSpinner.setOnItemSelectedListener(this);
    deviceSpinner.setOnItemSelectedListener(this);
    logSpinner.setOnItemSelectedListener(this);
    controlModeSpinner.setOnItemSelectedListener(this);
    driveModeSpinner.setOnItemSelectedListener(this);
    speedModeSpinner.setOnItemSelectedListener(this);

    // Intent for sensor service
    intentSensorService = new Intent(this, SensorService.class);

    setInitialValues();

    // Try to connect to serial device
    toggleConnection(true);

    localBroadcastReceiver =
        new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action != null) {

              switch (action) {
                case Constants.USB_ACTION_CONNECTION_ESTABLISHED:
                  break;

                case Constants.USB_ACTION_CONNECTION_CLOSED:
                  break;

                case Constants.USB_ACTION_DATA_RECEIVED:
                  long timestamp = SystemClock.elapsedRealtimeNanos();
                  String data = intent.getStringExtra("data");
                  // Data has the following form: voltage, lWheel, rWheel, obstacle
                  sendVehicleDataToSensorService(timestamp, data);
                  String[] itemList = data.split(",");
                  vehicle.setBatteryVoltage(Float.parseFloat(itemList[0]));
                  vehicle.setLeftWheelTicks(Float.parseFloat(itemList[1]));
                  vehicle.setRightWheelTicks(Float.parseFloat(itemList[2]));
                  vehicle.setSonarReading(Float.parseFloat(itemList[3]));
                  runOnUiThread(
                      () -> {
                        voltageTextView.setText(
                            String.format(Locale.US, "%2.1f V", vehicle.getBatteryVoltage()));
                        speedTextView.setText(
                            String.format(
                                Locale.US,
                                "%3.0f,%3.0f rpm",
                                vehicle.getLeftWheelRPM(),
                                vehicle.getRightWheelRPM()));
                        sonarTextView.setText(
                            String.format(Locale.US, "%3.0f cm", vehicle.getSonarReading()));
                      });
                  break;
              }
            }
          }
        };
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction(Constants.USB_ACTION_CONNECTION_ESTABLISHED);
    localIntentFilter.addAction(Constants.USB_ACTION_CONNECTION_CLOSED);
    localIntentFilter.addAction(Constants.USB_ACTION_DATA_RECEIVED);
    localBroadcastManager = LocalBroadcastManager.getInstance(this);
    localBroadcastManager.registerReceiver(localBroadcastReceiver, localIntentFilter);
  }

  @SuppressLint("SetTextI18n")
  private void setInitialValues() {
    cameraSwitchCompat.setChecked(preferencesManager.getCameraSwitch());

    baudRateSpinner.setSelection(Arrays.binarySearch(BaudRates, preferencesManager.getBaudrate()));
    modelSpinner.setSelection(Math.max(0, modelAdapter.getPosition(preferencesManager.getModel())));
    deviceSpinner.setSelection(preferencesManager.getDevice());
    logSpinner.setSelection(preferencesManager.getLogMode());
    controlModeSpinner.setSelection(preferencesManager.getControlMode());
    driveModeSpinner.setSelection(preferencesManager.getDriveMode());
    speedModeSpinner.setSelection(preferencesManager.getSpeedMode());

    setNumThreads(preferencesManager.getNumThreads());
    threadsTextView.setText(Integer.toString(numThreads));

    if (preferencesManager.getSheetExpanded()) {
      sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
  }

  protected int[] getRgbBytes() {
    imageConverter.run();
    return rgbBytes;
  }

  protected int getLuminanceStride() {
    return yRowStride;
  }

  protected byte[] getLuminance() {
    return yuvBytes[0];
  }

  /** Callback for android.hardware.Camera API */
  @Override
  public void onPreviewFrame(final byte[] bytes, final Camera camera) {
    if (isProcessingFrame) {
      LOGGER.w("Dropping frame!");
      return;
    }

    try {
      // Initialize the storage bitmaps once when the resolution is known.
      if (rgbBytes == null) {
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        previewHeight = previewSize.height;
        previewWidth = previewSize.width;
        rgbBytes = new int[previewWidth * previewHeight];
        onPreviewSizeChosen(new Size(previewSize.width, previewSize.height), 90);
      }
    } catch (final Exception e) {
      LOGGER.e(e, "Exception!");
      return;
    }

    isProcessingFrame = true;
    yuvBytes[0] = bytes;
    yRowStride = previewWidth;

    imageConverter =
        new Runnable() {
          @Override
          public void run() {
            ImageUtils.convertYUV420SPToARGB8888(bytes, previewWidth, previewHeight, rgbBytes);
          }
        };

    postInferenceCallback =
        new Runnable() {
          @Override
          public void run() {
            camera.addCallbackBuffer(bytes);
            isProcessingFrame = false;
          }
        };
    processImage();
  }

  /** Callback for Camera2 API */
  @Override
  public void onImageAvailable(final ImageReader reader) {
    // We need wait until we have some size from onPreviewSizeChosen
    if (previewWidth == 0 || previewHeight == 0) {
      return;
    }
    if (rgbBytes == null) {
      rgbBytes = new int[previewWidth * previewHeight];
    }
    try {
      final Image image = reader.acquireLatestImage();

      if (image == null) {
        return;
      }

      if (isProcessingFrame) {
        image.close();
        return;
      }
      isProcessingFrame = true;
      Trace.beginSection("imageAvailable");
      final Plane[] planes = image.getPlanes();
      fillBytes(planes, yuvBytes);

      yRowStride = planes[0].getRowStride();
      final int uvRowStride = planes[1].getRowStride();
      final int uvPixelStride = planes[1].getPixelStride();

      imageConverter =
          new Runnable() {
            @Override
            public void run() {
              ImageUtils.convertYUV420ToARGB8888(
                  yuvBytes[0],
                  yuvBytes[1],
                  yuvBytes[2],
                  previewWidth,
                  previewHeight,
                  yRowStride,
                  uvRowStride,
                  uvPixelStride,
                  rgbBytes);
            }
          };

      postInferenceCallback =
          new Runnable() {
            @Override
            public void run() {
              image.close();
              isProcessingFrame = false;
            }
          };

      processImage();
    } catch (final Exception e) {
      LOGGER.e(e, "Exception!");
      Trace.endSection();
      return;
    }
    Trace.endSection();
  }

  @Override
  public synchronized void onStart() {
    LOGGER.d("onStart " + this);
    super.onStart();
  }

  @Override
  public synchronized void onResume() {
    LOGGER.d("onResume " + this);
    super.onResume();

    handlerThread = new HandlerThread("inference");
    handlerThread.start();
    handler = new Handler(handlerThread.getLooper());
    serverService = new ServerService(this, this);
    serverService.start();
  }

  @Override
  public void onAddModel(String model) {
    if (modelAdapter != null && modelAdapter.getPosition(model) == -1) {
      modelAdapter.add(model);
    } else {
      if (model.equals(modelSpinner.getSelectedItem())) {
        setModel(new Model(model));
      }
    }
    Toast.makeText(context, "Model added: " + model, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onRemoveModel(String model) {
    if (modelAdapter != null && modelAdapter.getPosition(model) != -1) {
      modelAdapter.remove(model);
    }
    Toast.makeText(context, "Model removed: " + model, Toast.LENGTH_SHORT).show();
  }

  @Override
  public synchronized void onPause() {
    LOGGER.d("onPause " + this);

    handlerThread.quitSafely();
    try {
      handlerThread.join();
      handlerThread = null;
      handler = null;
      serverService.stop();
    } catch (final InterruptedException e) {
      LOGGER.e(e, "Exception!");
    }

    phoneController.disconnect();
    super.onPause();
  }

  @Override
  public synchronized void onStop() {
    LOGGER.d("onStop " + this);
    super.onStop();
  }

  @Override
  public synchronized void onDestroy() {
    toggleConnection(false);
    if (localBroadcastManager != null) {
      localBroadcastManager.unregisterReceiver(localBroadcastReceiver);
      localBroadcastManager = null;
    }
    if (localBroadcastReceiver != null) localBroadcastReceiver = null;
    LOGGER.d("onDestroy " + this);
    super.onDestroy();
  }

  protected synchronized void runInBackground(final Runnable r) {
    if (handler != null) {
      handler.post(r);
    }
  }

  @Override
  public void onRequestPermissionsResult(
      final int requestCode, final String[] permissions, final int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CAMERA_PERMISSION:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setFragment();
        } else {
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_CAMERA)) {
            Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_LONG).show();
          }
          // requestCameraPermission();
        }
        break;

      case REQUEST_LOCATION_PERMISSION_LOGGING:
        // If the permission is granted, start logging,
        // otherwise, show a Toast
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setIsLoggingActive(true);
        } else {
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_LOCATION)) {
            Toast.makeText(this, R.string.location_permission_denied_logging, Toast.LENGTH_LONG)
                .show();
          }
        }
        break;

      case REQUEST_LOCATION_PERMISSION_CONTROLLER:
        // If the permission is granted, start advertising to controller,
        // otherwise, show a Toast
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          if (!phoneController.isConnected()) {
            phoneController.connect(this);
          }
        } else {
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_LOCATION)) {
            Toast.makeText(this, R.string.location_permission_denied_controller, Toast.LENGTH_LONG)
                .show();
          }
        }
        break;

      case REQUEST_STORAGE_PERMISSION:
        // If the permission is granted, start logging,
        // otherwise, show a Toast
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setIsLoggingActive(true);
        } else {
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_STORAGE)) {
            Toast.makeText(this, R.string.storage_permission_denied, Toast.LENGTH_LONG).show();
          }
        }
        break;
    }
  }

  private String[] getModelFiles() {
    return this.getFilesDir().list((dir1, name) -> name.endsWith(".tflite"));
  }

  private boolean hasCameraPermission() {
    return ContextCompat.checkSelfPermission(this, PERMISSION_CAMERA)
        == PackageManager.PERMISSION_GRANTED;
  }

  private boolean hasLocationPermission() {
    return ContextCompat.checkSelfPermission(this, PERMISSION_LOCATION)
        == PackageManager.PERMISSION_GRANTED;
  }

  private boolean hasStoragePermission() {
    return ContextCompat.checkSelfPermission(this, PERMISSION_STORAGE)
        == PackageManager.PERMISSION_GRANTED;
  }

  private boolean hasBluetoothPermission() {
    return ContextCompat.checkSelfPermission(this, PERMISSION_BLUETOOTH)
        == PackageManager.PERMISSION_GRANTED;
  }

  private void requestCameraPermission() {
    ActivityCompat.requestPermissions(
        this, new String[] {PERMISSION_CAMERA}, REQUEST_CAMERA_PERMISSION);
  }

  private void requestLocationPermissionLogging() {
    ActivityCompat.requestPermissions(
        this, new String[] {PERMISSION_LOCATION}, REQUEST_LOCATION_PERMISSION_LOGGING);
  }

  private void requestLocationPermissionController() {
    ActivityCompat.requestPermissions(
        this, new String[] {PERMISSION_LOCATION}, REQUEST_LOCATION_PERMISSION_CONTROLLER);
  }

  private void requestStoragePermission() {
    ActivityCompat.requestPermissions(
        this, new String[] {PERMISSION_STORAGE}, REQUEST_STORAGE_PERMISSION);
  }

  private void requestBluetoothPermission() {
    ActivityCompat.requestPermissions(
        this, new String[] {PERMISSION_BLUETOOTH}, REQUEST_BLUETOOTH_PERMISSION);
  }

  // Returns true if the device supports the required hardware level, or better.
  private boolean isHardwareLevelSupported(
      CameraCharacteristics characteristics, int requiredLevel) {
    int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
    if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
      return requiredLevel == deviceLevel;
    }
    // deviceLevel is not LEGACY, can use numerical sort
    return requiredLevel <= deviceLevel;
  }

  private String chooseCamera(int facingSelection) {
    final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    try {
      for (final String cameraId : manager.getCameraIdList()) {
        final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        LOGGER.i(
            "CAMERA ID: "
                + cameraId
                + " FACING: "
                + characteristics.get(CameraCharacteristics.LENS_FACING));
        // We don't use a front facing camera in this sample.
        final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
        if (facing != null && facing != facingSelection) {
          continue;
        }

        final StreamConfigurationMap map =
            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        if (map == null) {
          continue;
        }

        // Fallback to camera1 API for internal cameras that don't have full support.
        // This should help with legacy situations where using the camera2 API causes
        // distorted or otherwise broken previews.
        useCamera2API =
            (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
                || isHardwareLevelSupported(
                    characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL)
                || isHardwareLevelSupported(
                    characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED);
        LOGGER.i("Camera API lv2?: %s", useCamera2API);
        return cameraId;
      }
    } catch (CameraAccessException e) {
      LOGGER.e(e, "Not allowed to access camera");
    }

    return null;
  }

  protected void setFragment() {
    cameraSelection = getCameraUserSelection();
    String cameraId = chooseCamera(cameraSelection);
    Fragment fragment;
    if (useCamera2API) {
      CameraConnectionFragment camera2Fragment =
          CameraConnectionFragment.newInstance(
              new CameraConnectionFragment.ConnectionCallback() {
                @Override
                public void onPreviewSizeChosen(final Size size, final int rotation) {
                  previewHeight = size.getHeight();
                  previewWidth = size.getWidth();
                  CameraActivity.this.onPreviewSizeChosen(size, rotation);
                }
              },
              this,
              getLayoutId(),
              getDesiredPreviewFrameSize());

      camera2Fragment.setCamera(cameraId);
      fragment = camera2Fragment;
    } else {
      fragment =
          new LegacyCameraConnectionFragment(
              this, getLayoutId(), getDesiredPreviewFrameSize(), cameraSelection);
    }

    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
  }

  protected void fillBytes(final Plane[] planes, final byte[][] yuvBytes) {
    // Because of the variable row stride it's not possible to know in
    // advance the actual necessary dimensions of the yuv planes.
    for (int i = 0; i < planes.length; ++i) {
      final ByteBuffer buffer = planes[i].getBuffer();
      if (yuvBytes[i] == null) {
        LOGGER.d("Initializing buffer %d at size %d", i, buffer.capacity());
        yuvBytes[i] = new byte[buffer.capacity()];
      }
      buffer.get(yuvBytes[i]);
    }
  }

  public boolean isDebug() {
    return debug;
  }

  protected void readyForNextImage() {
    if (postInferenceCallback != null) {
      postInferenceCallback.run();
    }
  }

  protected int getScreenOrientation() {
    switch (getWindowManager().getDefaultDisplay().getRotation()) {
      case Surface.ROTATION_270:
        return 270;
      case Surface.ROTATION_180:
        return 180;
      case Surface.ROTATION_90:
        return 90;
      default:
        return 0;
    }
  }

  protected int getCameraUserSelection() {
    // during initialisation there is no cameraToggle so we assume default
    if (this.cameraSwitchCompat == null) {
      this.cameraSelection = CameraCharacteristics.LENS_FACING_BACK;
    } else {
      this.cameraSelection = this.cameraSwitchCompat.isChecked() ? 0 : 1;
    }
    return this.cameraSelection;
  }

  protected void setCameraSwitchText() {
    if (this.cameraSelection == CameraCharacteristics.LENS_FACING_BACK) {
      cameraSwitchCompat.setText(R.string.camera_facing_back);
    } else {
      cameraSwitchCompat.setText(R.string.camera_facing_front);
    }
  }

  protected int getBaudRate() {
    return baudRate;
  }

  private void setBaudRate(int baudRate) {
    if (this.baudRate != baudRate) {
      LOGGER.d("Updating  baudRate: " + baudRate);
      this.baudRate = baudRate;
      preferencesManager.setBaudrate(baudRate);
    }
  }

  private void setLogMode(LogMode logMode) {
    if (this.logMode != logMode) {
      LOGGER.d("Updating  logMode: " + logMode);
      this.logMode = logMode;
      preferencesManager.setLogMode(logMode.ordinal());
    }
  }

  private void setSpeedMode(SpeedMode speedMode) {
    if (this.speedMode != speedMode) {
      LOGGER.d("Updating  speedMode: " + speedMode);
      this.speedMode = speedMode;
      preferencesManager.setSpeedMode(speedMode.ordinal());
      vehicle.setSpeedMultiplier(speedMode.getValue());
    }
  }

  private void setControlMode(ControlMode controlMode) {
    if (this.controlMode != controlMode) {
      LOGGER.d("Updating  controlMode: " + controlMode);
      this.controlMode = controlMode;
      switch (controlMode) {
        case GAMEPAD:
          disconnectPhoneController();
          break;
        case PHONE:
          handleControllerEvents();
          if (!hasLocationPermission()) requestLocationPermissionController();
          else connectPhoneController();
          break;
        case WEBRTC:
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + controlMode);
      }
      preferencesManager.setControlMode(controlMode.ordinal());
      controlModeSpinner.setSelection(controlMode.ordinal());
    }
  }

  private void connectPhoneController() {
    if (!phoneController.isConnected()) {
      phoneController.connect(this);
    }
    DriveMode oldDriveMode = driveMode;
    // Currently only dual drive mode supported
    setDriveMode(DriveMode.DUAL);
    driveModeSpinner.setAlpha(0.5f);
    preferencesManager.setDriveMode(oldDriveMode.ordinal());
  }

  private void disconnectPhoneController() {
    if (phoneController.isConnected()) {
      phoneController.disconnect();
    }
    setDriveMode(DriveMode.values()[preferencesManager.getDriveMode()]);
    driveModeSpinner.setEnabled(true);
    driveModeSpinner.setAlpha(1.0f);
  }

  protected void setDriveMode(DriveMode driveMode) {
    if (this.driveMode != driveMode) {
      LOGGER.d("Updating  driveMode: " + driveMode);
      this.driveMode = driveMode;
      preferencesManager.setDriveMode(driveMode.ordinal());
      gameController.setDriveMode(driveMode);
      driveModeSpinner.setSelection(driveMode.ordinal());
      BotToControllerEventBus.emitEvent(createStatus("DRIVE_MODE", driveMode.toString()));
    }
  }

  protected Model getModel() {
    return model;
  }

  private void setModel(Model model) {
    if (this.model != model) {
      LOGGER.d("Updating  model: " + model);
      this.model = model;
      preferencesManager.setModel(model.toString());
      onInferenceConfigurationChanged();
    }
  }

  protected Device getDevice() {
    return device;
  }

  private void setDevice(Device device) {
    if (this.device != device) {
      LOGGER.d("Updating  device: " + device);
      this.device = device;
      final boolean threadsEnabled = device == Device.CPU;
      plusImageView.setEnabled(threadsEnabled);
      minusImageView.setEnabled(threadsEnabled);
      threadsTextView.setText(threadsEnabled ? String.valueOf(numThreads) : "N/A");
      if (threadsEnabled) threadsTextView.setTextColor(Color.BLACK);
      else threadsTextView.setTextColor(Color.GRAY);
      preferencesManager.setDevice(device.ordinal());
      onInferenceConfigurationChanged();
    }
  }

  protected int getNumThreads() {
    return numThreads;
  }

  private void setNumThreads(int numThreads) {
    if (this.numThreads != numThreads) {
      LOGGER.d("Updating  numThreads: " + numThreads);
      this.numThreads = numThreads;
      preferencesManager.setNumThreads(numThreads);
      onInferenceConfigurationChanged();
    }
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
      msg.arg1 = (int) (vehicle.getControl().getLeft());
      msg.arg2 = (int) (vehicle.getControl().getRight());
      msg.what = SensorService.MSG_CONTROL;
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

  private void startLogging() {
    logFolder =
        Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + getString(R.string.app_name)
            + File.separator
            + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    intentSensorService.putExtra("logFolder", logFolder + File.separator + "sensor_data");
    startService(intentSensorService);
    bindService(intentSensorService, sensorConnection, Context.BIND_AUTO_CREATE);
    // Send current vehicle state to log
    runInBackground(
        () -> {
          try {
            TimeUnit.MILLISECONDS.sleep(500);
            sendControlToSensorService();
            sendIndicatorToSensorService();
          } catch (InterruptedException e) {
            LOGGER.e(e, "Got interrupted.");
          }
        });
  }

  private void stopLogging() {
    if (sensorConnection != null) unbindService(sensorConnection);
    stopService(intentSensorService);

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
            LOGGER.e(e, "Got interrupted.");
          }
        });
  }

  protected void setIsLoggingActive(boolean loggingActive) {
    if (loggingActive && !loggingEnabled) {
      if (!hasCameraPermission() && logMode != LogMode.ONLY_SENSORS) {
        requestCameraPermission();
        loggingEnabled = false;
      } else if (!hasLocationPermission()) {
        requestLocationPermissionLogging();
        loggingEnabled = false;
      } else if (!hasStoragePermission()) {
        requestStoragePermission();
        loggingEnabled = false;
      } else {
        startLogging();
        loggingEnabled = true;
      }
    } else if (!loggingActive && loggingEnabled) {
      stopLogging();
      loggingEnabled = false;
    }
    BotToControllerEventBus.emitEvent(createStatus("LOGS", loggingEnabled));

    logSpinner.setEnabled(!loggingEnabled);
    if (loggingEnabled) logSpinner.setAlpha(0.5f);
    else logSpinner.setAlpha(1.0f);
    logSwitchCompat.setChecked(loggingEnabled);
    if (loggingEnabled) logSwitchCompat.setText(R.string.logging);
    else logSwitchCompat.setText(R.string.not_logging);
  }

  protected abstract void processImage();

  protected abstract void onPreviewSizeChosen(final Size size, final int rotation);

  protected abstract int getLayoutId();

  protected abstract Size getDesiredPreviewFrameSize();

  protected abstract void onInferenceConfigurationChanged();

  protected abstract void toggleNoise();

  protected abstract void sendVehicleControl();

  protected abstract void setNetworkEnabled(boolean isChecked);

  protected void toggleCamera(boolean isChecked) {
    LOGGER.d("Camera Toggled to " + isChecked);
    this.cameraSelection = getCameraUserSelection();
    this.setCameraSwitchText();
    this.setFragment();
    preferencesManager.setCameraSwitch(isChecked);
  }

  protected void toggleConnection(boolean isChecked) {
    if (isChecked) {
      vehicle.connectUsb();
    } else {
      vehicle.disconnectUsb();
    }
    // Disable baudrate selection if connected
    baudRateSpinner.setEnabled(!vehicle.isUsbConnected());
    if (vehicle.isUsbConnected()) baudRateSpinner.setAlpha(0.5f);
    else baudRateSpinner.setAlpha(1.0f);
    connectionSwitchCompat.setChecked(vehicle.isUsbConnected());

    if (vehicle.isUsbConnected()) {
      connectionSwitchCompat.setText(vehicle.getUsbConnection().getProductName());
      Toast.makeText(getContext(), "Connected.", Toast.LENGTH_SHORT).show();
    } else {
      connectionSwitchCompat.setText(R.string.no_device);
      // Tried to connect but failed
      if (isChecked) {
        Toast.makeText(getContext(), "Please check the USB connection.", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getContext(), "Disconnected.", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public static Context getContext() {
    return context;
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (buttonView == connectionSwitchCompat) {
      toggleConnection(isChecked);
    } else if (buttonView == networkSwitchCompat) {
      setNetworkEnabled(isChecked);
    } else if (buttonView == logSwitchCompat) {
      setIsLoggingActive(isChecked);
    } else if (buttonView == cameraSwitchCompat) {
      toggleCamera(isChecked);
    }
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.plus) {
      String threads = threadsTextView.getText().toString().trim();
      int numThreads = Integer.parseInt(threads);
      if (numThreads >= 9) return;
      setNumThreads(++numThreads);
      threadsTextView.setText(String.valueOf(numThreads));
    } else if (v.getId() == R.id.minus) {
      String threads = threadsTextView.getText().toString().trim();
      int numThreads = Integer.parseInt(threads);
      if (numThreads == 1) return;
      setNumThreads(--numThreads);
      threadsTextView.setText(String.valueOf(numThreads));
    }
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    String selected = parent.getItemAtPosition(pos).toString();
    if (parent == baudRateSpinner) {
      setBaudRate(Integer.parseInt(selected));
    } else if (parent == modelSpinner) {
      try {
        setModel(Model.fromId(selected.toUpperCase()));
      } catch (IllegalArgumentException e) {
        setModel(new Model(selected));
      }
    } else if (parent == deviceSpinner) {
      setDevice(Device.valueOf(selected.toUpperCase()));
    } else if (parent == logSpinner) {
      setLogMode(LogMode.valueOf(selected.toUpperCase()));
    } else if (parent == controlModeSpinner) {
      setControlMode(ControlMode.valueOf(selected.toUpperCase()));
    } else if (parent == driveModeSpinner) {
      setDriveMode(driveMode);
    } else if (parent == speedModeSpinner) {
      setSpeedMode(SpeedMode.valueOf(selected.toUpperCase()));
    }
  }

  /*
     Classes to handle events from a Controller.
     This can be the entry point to other external controllers
     See how PhoneController emits events.

     Expected JSON format:
     {command: "LOGS"}
        or
     {driveCmd: {l:0.2, r:-0.34}}
  */

  private void handleControllerEvents() {
    // Prevent multiple subscriptions. This happens if we select "Phone control multiple times.
    if (ControllerToBotEventBus.getProcessor().hasObservers()) {
      return;
    }

    ControllerToBotEventBus.getProcessor()
        .subscribe(
            event -> {
              JSONObject commandJsn = event;
              String commandType = "";
              if (commandJsn.has("command")) {
                commandType = commandJsn.getString("command");
              } else if (commandJsn.has("driveCmd")) {
                commandType = "DRIVE_CMD";
              } else {
                Log.d(null, "Got invalid command from controller: " + commandJsn.toString());
                return;
              }

              switch (commandType) {
                case "DRIVE_CMD":
                  JSONObject driveValue = commandJsn.getJSONObject("driveCmd");
                  controllerHandler.handleDriveCommand(
                      Float.valueOf(driveValue.getString("l")),
                      Float.valueOf(driveValue.getString("r")));
                  break;

                case "LOGS":
                  controllerHandler.handleLogging();
                  break;

                case "NOISE":
                  controllerHandler.handleNoise();
                  break;

                case "INDICATOR_LEFT":
                  controllerHandler.handleIndicatorLeft();
                  break;

                case "INDICATOR_RIGHT":
                  controllerHandler.handleIndicatorRight();
                  break;

                case "INDICATOR_STOP":
                  controllerHandler.handleIndicatorStop();
                  break;

                case "NETWORK":
                  controllerHandler.handleNetwork();
                  break;

                case "DRIVE_MODE":
                  controllerHandler.handleDriveMode();
                  break;

                  // We re connected to the controller, send back status info
                case "CONNECTED":
                  // PhoneController class will receive this event and resent it to the controller.
                  // Other controllers can subscribe to this event as well.
                  // That is why we are not calling phoneController.send() here directly.
                  BotToControllerEventBus.emitEvent(getStatus());
                  break;
                case "DISCONNECTED":
                  controllerHandler.handleDriveCommand(0.f, 0.f);
                  setControlMode(ControlMode.GAMEPAD);
                  break;
              }
            });
  }

  private JSONObject getStatus() {
    JSONObject status = new JSONObject();
    try {
      JSONObject statusValue = new JSONObject();

      statusValue.put("LOGS", this.loggingEnabled);
      statusValue.put("NOISE", this.noiseEnabled);
      statusValue.put("NETWORK", this.networkEnabled);
      statusValue.put("DRIVE_MODE", this.driveMode);

      // Possibly can only send the value of the indicator here, but this makes it clearer.
      // Also, the controller need not have to know implementation details.
      statusValue.put("INDICATOR_LEFT", vehicle.getIndicator() == -1);
      statusValue.put("INDICATOR_RIGHT", vehicle.getIndicator() == 1);
      statusValue.put("INDICATOR_STOP", vehicle.getIndicator() == 0);

      status.put("status", statusValue);

    } catch (JSONException e) {
      e.printStackTrace();
    }
    return status;
  }

  protected JSONObject createStatus(String name, Boolean value) {
    return createStatus(name, value ? "true" : "false");
  }

  protected JSONObject createStatus(String name, String value) {
    try {
      return new JSONObject().put("status", new JSONObject().put(name, value));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return new JSONObject();
  }

  private void sendIndicatorStatus(Integer status) {
    BotToControllerEventBus.emitEvent(createStatus("INDICATOR_LEFT", status == -1));
    BotToControllerEventBus.emitEvent(createStatus("INDICATOR_RIGHT", status == 1));
    BotToControllerEventBus.emitEvent(createStatus("INDICATOR_STOP", status == 0));
  }

  // Controller event handler
  protected class ControllerHandler {

    protected void handleDriveCommand(Vehicle.Control control) {
      vehicle.setControl(control);
      sendVehicleControl();
    }

    protected void handleDriveCommand(Float l, Float r) {
      vehicle.setControl(l, r);
      sendVehicleControl();
    }

    protected void handleLogging() {
      setIsLoggingActive(!loggingEnabled);
      audioPlayer.playLogging(voice, loggingEnabled);
    }

    protected void handleNoise() {
      toggleNoise();
      audioPlayer.playNoise(voice, noiseEnabled);
    }

    protected void handleIndicatorLeft() {
      vehicle.setIndicator(Enums.VehicleIndicator.LEFT.getValue());
      if (loggingEnabled) {
        sendIndicatorToSensorService();
      }
      sendIndicatorStatus(vehicle.getIndicator());
    }

    protected void handleIndicatorRight() {
      vehicle.setIndicator(Enums.VehicleIndicator.RIGHT.getValue());
      if (loggingEnabled) {
        sendIndicatorToSensorService();
      }
      sendIndicatorStatus(vehicle.getIndicator());
    }

    protected void handleIndicatorStop() {
      vehicle.setIndicator(Enums.VehicleIndicator.STOP.getValue());
      if (loggingEnabled) {
        sendIndicatorToSensorService();
      }
      sendIndicatorStatus(vehicle.getIndicator());
    }

    protected void handleDriveMode() {
      if (networkEnabled) return;
      switch (driveMode) {
        case DUAL:
          setDriveMode(DriveMode.GAME);
          break;
        case GAME:
          setDriveMode(DriveMode.JOYSTICK);
          break;
        case JOYSTICK:
          setDriveMode(DriveMode.DUAL);
          break;
      }
      audioPlayer.playDriveMode(voice, driveMode);
      driveModeSpinner.setSelection(driveMode.ordinal());
    }

    protected void handleNetwork() {
      setNetworkEnabled(!networkEnabled);
      if (networkEnabled) audioPlayer.play(voice, "network_enabled.mp3");
      else {
        // when network disabled play the active drive mode used for controller
        audioPlayer.playDriveMode(voice, driveMode);
      }
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Do nothing.
  }
}
