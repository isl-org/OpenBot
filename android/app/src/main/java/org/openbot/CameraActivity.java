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

package org.openbot;

import android.Manifest;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Build;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.openbot.env.GameController;
import org.openbot.env.ImageUtils;
import org.openbot.env.Logger;
import org.openbot.env.UsbConnection;
import org.openbot.tflite.Network.Device;
import org.openbot.tflite.Network.Model;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

public abstract class CameraActivity extends AppCompatActivity
    implements OnImageAvailableListener,
        Camera.PreviewCallback,
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {
  private static final Logger LOGGER = new Logger();

  // Constants
  private static final int REQUEST_CAMERA_PERMISSION = 1;
  private static final int REQUEST_LOCATION_PERMISSION = 2;
  private static final int REQUEST_STORAGE_PERMISSION = 3;

  private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
  private static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
  private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

  private static Context mContext;
  private int cameraSelection = CameraCharacteristics.LENS_FACING_FRONT;
  protected int previewWidth = 0;
  protected int previewHeight = 0;
  private boolean debug = false;
  private Handler handler;
  private HandlerThread handlerThread;
  private boolean useCamera2API;
  private boolean isProcessingFrame = false;
  private byte[][] yuvBytes = new byte[3][];
  private int[] rgbBytes = null;
  private int yRowStride;
  private Runnable postInferenceCallback;
  private Runnable imageConverter;

  private LinearLayout bottomSheetLayout;
  private LinearLayout gestureLayout;
  private BottomSheetBehavior sheetBehavior;

  protected SwitchCompat connectionSwitchCompat, driveModeSwitchCompat, loggerSwitchCompat;
  protected TextView frameValueTextView,
      cropValueTextView,
      inferenceTimeTextView,
      controlValueTextView;
  protected ImageView bottomSheetArrowImageView;
  private ImageView plusImageView, minusImageView;
  protected Spinner baudRateSpinner,
      modelSpinner,
      deviceSpinner,
      driveModeSpinner,
      loggerSpinner,
      controlSpinner;
  private TextView threadsTextView;
  private Model model = Model.DETECTOR_V1_1_0_Q;
  private Device device = Device.CPU;
  private int numThreads = -1;

  protected GameController gameController;

  // **** USB **** //
  protected UsbConnection usbConnection;
  protected boolean usbConnected;
  public int[] BaudRates = {9600, 14400, 19200, 38400, 57600, 115200, 230400, 460800, 921600};
  private int baudRate = 115200;
  protected LogMode logMode = LogMode.CROP_IMG;
  protected ControlSpeed controlSpeed = ControlSpeed.NORMAL;
  protected DriveMode driveMode = DriveMode.GAME;
  protected String logFolder;
  private boolean loggingEnabled;
  private Intent intentSensorService;

  public enum LogMode {
    ALL_IMGS,
    CROP_IMG,
    PREVIEW_IMG,
    ONLY_SENSORS
  }

  public enum ControlSpeed {
    SLOW,
    NORMAL,
    FAST
  }

  public enum DriveMode {
    DUAL,
    GAME,
    JOYSTICK
  }

  public static final class ControlSignal {
    private final float left;
    private final float right;

    public ControlSignal(float left, float right) {
      this.left = Math.max(-1.f, Math.min(1.f, left));
      this.right = Math.max(-1.f, Math.min(1.f, right));
    }

    public float getLeft() {
      return left;
    }

    public float getRight() {
      return right;
    }
  }

  protected ControlSignal vehicleControl = new ControlSignal(0, 0);
  protected int speedMultiplier = 192; // 128,192,255
  protected int vehicleIndicator = 0;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    LOGGER.d("onCreate " + this);
    super.onCreate(null);
    mContext = getApplicationContext();
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    setContentView(R.layout.activity_camera);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    if (hasCameraPermission()) {
      setFragment();
    } else {
      requestCameraPermission();
    }

    connectionSwitchCompat = findViewById(R.id.connection_switch);
    threadsTextView = findViewById(R.id.threads);
    plusImageView = findViewById(R.id.plus);
    minusImageView = findViewById(R.id.minus);
    baudRateSpinner = findViewById(R.id.baud_rate_spinner);
    modelSpinner = findViewById(R.id.model_spinner);
    deviceSpinner = findViewById(R.id.device_spinner);
    driveModeSpinner = findViewById(R.id.drive_mode_spinner);
    driveModeSwitchCompat = findViewById(R.id.drive_mode_info_switch);
    bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
    gestureLayout = findViewById(R.id.gesture_layout);
    sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
    bottomSheetArrowImageView = findViewById(R.id.bottom_sheet_arrow);
    loggerSwitchCompat = findViewById(R.id.logger_switch);
    loggerSpinner = findViewById(R.id.logger_spinner);
    controlSpinner = findViewById(R.id.control_spinner);

    ViewTreeObserver vto = gestureLayout.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
              gestureLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
              gestureLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
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
                {
                  bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_down);
                }
                break;
              case BottomSheetBehavior.STATE_COLLAPSED:
                {
                  bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up);
                }
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

    frameValueTextView = findViewById(R.id.frame_info);
    cropValueTextView = findViewById(R.id.crop_info);
    inferenceTimeTextView = findViewById(R.id.inference_info);
    controlValueTextView = findViewById(R.id.control_info);

    connectionSwitchCompat.setOnCheckedChangeListener(this);
    driveModeSwitchCompat.setOnCheckedChangeListener(this);
    loggerSwitchCompat.setOnCheckedChangeListener(this);

    plusImageView.setOnClickListener(this);
    minusImageView.setOnClickListener(this);

    baudRateSpinner.setOnItemSelectedListener(this);
    modelSpinner.setOnItemSelectedListener(this);
    deviceSpinner.setOnItemSelectedListener(this);
    driveModeSpinner.setOnItemSelectedListener(this);
    loggerSpinner.setOnItemSelectedListener(this);
    controlSpinner.setOnItemSelectedListener(this);

    // Make sure spinners are initialized correctly
    baudRateSpinner.setSelection(Arrays.binarySearch(BaudRates, baudRate));
    modelSpinner.setSelection(model.ordinal());
    deviceSpinner.setSelection(device.ordinal());
    driveModeSpinner.setSelection(driveMode.ordinal());
    loggerSpinner.setSelection(logMode.ordinal());
    controlSpinner.setSelection(controlSpeed.ordinal());

    numThreads = Integer.parseInt(threadsTextView.getText().toString().trim());

    gameController = new GameController(driveMode);

    // Intent for sensor service
    intentSensorService = new Intent(this, SensorService.class);

    // Try to connect to serial device
    toggleConnection(true);
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
  }

  @Override
  public synchronized void onPause() {
    LOGGER.d("onPause " + this);

    handlerThread.quitSafely();
    try {
      handlerThread.join();
      handlerThread = null;
      handler = null;
    } catch (final InterruptedException e) {
      LOGGER.e(e, "Exception!");
    }

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

      case REQUEST_LOCATION_PERMISSION:
        // If the permission is granted, start logging,
        // otherwise, show a Toast
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setIsLoggingActive(true);
        } else {
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_LOCATION)) {
            Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_LONG).show();
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

  private void requestCameraPermission() {
    ActivityCompat.requestPermissions(
        this, new String[] {PERMISSION_CAMERA}, REQUEST_CAMERA_PERMISSION);
  }

  private void requestLocationPermission() {
    ActivityCompat.requestPermissions(
        this, new String[] {PERMISSION_LOCATION}, REQUEST_LOCATION_PERMISSION);
  }

  private void requestStoragePermission() {
    ActivityCompat.requestPermissions(
        this, new String[] {PERMISSION_STORAGE}, REQUEST_STORAGE_PERMISSION);
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

  protected void showFrameInfo(String frameInfo) {
    frameValueTextView.setText(frameInfo);
  }

  protected void showCropInfo(String cropInfo) {
    cropValueTextView.setText(cropInfo);
  }

  protected void showInference(String inferenceTime) {
    inferenceTimeTextView.setText(inferenceTime);
  }

  protected void showControl(String controlValue) {
    controlValueTextView.setText(controlValue);
  }

  protected int getBaudRate() {
    return baudRate;
  }

  private void setBaudRate(int baudRate) {
    if (this.baudRate != baudRate) {
      LOGGER.d("Updating  baudRate: " + baudRate);
      this.baudRate = baudRate;
    }
  }

  private void setLogMode(LogMode logMode) {
    if (this.logMode != logMode) {
      LOGGER.d("Updating  logMode: " + logMode);
      this.logMode = logMode;
    }
  }

  private void setControlSpeed(ControlSpeed controlSpeed) {
    if (this.controlSpeed != controlSpeed) {
      LOGGER.d("Updating  controlSpeed: " + controlSpeed);
      this.controlSpeed = controlSpeed;
      switch (controlSpeed) {
        case SLOW:
          speedMultiplier = 128;
          break;
        case NORMAL:
          speedMultiplier = 192;
          break;
        case FAST:
          speedMultiplier = 255;
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + controlSpeed);
      }
    }
  }

  protected void setDriveMode(DriveMode driveMode) {
    if (this.driveMode != driveMode) {
      LOGGER.d("Updating  driveMode: " + driveMode);
      this.driveMode = driveMode;
      gameController.setDriveMode(driveMode);
    }
  }

  protected Model getModel() {
    return model;
  }

  private void setModel(Model model) {
    if (this.model != model) {
      LOGGER.d("Updating  model: " + model);
      this.model = model;
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
      onInferenceConfigurationChanged();
    }
  }

  protected boolean getLoggingEnabled() {
    return loggingEnabled;
  }

  Messenger mSensorMessenger;

  ServiceConnection mSensorConnection =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
          mSensorMessenger = new Messenger(binder);
          Log.d("SensorServiceConnection", "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
          mSensorMessenger = null;
          Log.d("SensorServiceConnection", "disconnected");
        }
      };

  protected void sendFrameNumberToSensorService(long frameNumber) {
    if (mSensorMessenger != null) {
      Message msg = Message.obtain();
      Bundle bundle = new Bundle();
      bundle.putLong("frameNumber", frameNumber);
      bundle.putLong("timestamp", SystemClock.elapsedRealtimeNanos());
      msg.setData(bundle);
      msg.what = SensorService.MSG_FRAME;
      try {
        mSensorMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
        ;
      }
    }
  }

  protected void sendInferenceTimeToSensorService(long frameNumber, long inferenceTime) {
    if (mSensorMessenger != null) {
      Message msg = Message.obtain();
      Bundle bundle = new Bundle();
      bundle.putLong("frameNumber", frameNumber);
      bundle.putLong("inferenceTime", inferenceTime);
      msg.setData(bundle);
      msg.what = SensorService.MSG_INFERENCE;
      try {
        mSensorMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
        ;
      }
    }
  }

  protected void sendControlToSensorService(ControlSignal vehicleControl) {
    if (mSensorMessenger != null) {
      Message msg = Message.obtain();
      msg.arg1 = (int) (vehicleControl.getLeft() * speedMultiplier);
      msg.arg2 = (int) (vehicleControl.getRight() * speedMultiplier);
      msg.what = SensorService.MSG_CONTROL;
      try {
        mSensorMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
        ;
      }
    }
  }

  protected void sendIndicatorToSensorService(int vehicleIndicator) {
    if (mSensorMessenger != null) {
      Message msg = Message.obtain();
      msg.arg1 = vehicleIndicator;
      msg.what = SensorService.MSG_INDICATOR;
      try {
        mSensorMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
        ;
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
    bindService(intentSensorService, mSensorConnection, Context.BIND_AUTO_CREATE);
    // Send current vehicle state to log
    runInBackground(
        () -> {
          try {
            TimeUnit.MILLISECONDS.sleep(500);
            sendControlToSensorService(vehicleControl);
            sendIndicatorToSensorService(vehicleIndicator);
          } catch (InterruptedException e) {
            LOGGER.e(e, "Got interrupted.");
          }
        });
  }

  private void stopLogging() {
    if (mSensorConnection != null) {
      unbindService(mSensorConnection);
      stopService(intentSensorService);
    }

    // Pack and upload the collected data
    runInBackground(
        () -> {
          String logZipFile = logFolder + ".zip";
          // Zip the log folder and then delete it
          ZipUtil.pack(new File(logFolder), new File(logZipFile));
          FileUtils.deleteQuietly(new File(logFolder));
        });
  }

  protected void setIsLoggingActive(boolean loggingActive) {
    if (loggingActive && !getLoggingEnabled()) {
      if (!hasCameraPermission() && logMode != LogMode.ONLY_SENSORS) {
        requestCameraPermission();
        this.loggingEnabled = false;
      } else if (!hasLocationPermission()) {
        requestLocationPermission();
        this.loggingEnabled = false;
      } else if (!hasStoragePermission()) {
        requestStoragePermission();
        this.loggingEnabled = false;
      } else {
        startLogging();
        this.loggingEnabled = true;
      }
    } else if (!loggingActive && getLoggingEnabled()) {
      stopLogging();
      this.loggingEnabled = false;
    }

    loggerSpinner.setEnabled(!this.loggingEnabled);
    loggerSwitchCompat.setChecked(this.loggingEnabled);
    if (this.loggingEnabled) loggerSwitchCompat.setText("Logging");
    else loggerSwitchCompat.setText("Not Logging");
  }

  protected abstract void processImage();

  protected abstract void onPreviewSizeChosen(final Size size, final int rotation);

  protected abstract int getLayoutId();

  protected abstract Size getDesiredPreviewFrameSize();

  protected abstract void onInferenceConfigurationChanged();

  private void connectUsb() {
    usbConnection = new UsbConnection(this, baudRate);
    usbConnected = usbConnection.startUsbConnection();
  }

  private void disconnectUsb() {
    if (usbConnection != null) {
      sendControlToVehicle(new ControlSignal(0, 0));
      usbConnection.stopUsbConnection();
      usbConnection = null;
    }
    usbConnected = false;
  }

  protected void toggleConnection(boolean isChecked) {
    if (isChecked) {
      connectUsb();
    } else {
      disconnectUsb();
    }
    // Disable baudrate selection if connected
    baudRateSpinner.setEnabled(!usbConnected);
    connectionSwitchCompat.setChecked(usbConnected);

    if (usbConnected) {
      connectionSwitchCompat.setText(usbConnection.getProductName());
      Toast.makeText(getContext(), "Connected.", Toast.LENGTH_SHORT).show();
    } else {
      connectionSwitchCompat.setText("No Device");
      // Tried to connect but failed
      if (isChecked) {
        Toast.makeText(getContext(), "Please check the USB connection.", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getContext(), "Disconnected.", Toast.LENGTH_SHORT).show();
      }
    }
  }

  protected abstract void setDriveByNetwork(boolean isChecked);

  protected void sendControlToVehicle(ControlSignal vehicleControl) {
    if ((usbConnection != null) && usbConnection.isOpen() && !usbConnection.isBusy()) {
      String message =
          String.format(
              "c%d,%d\n",
              (int) (vehicleControl.getLeft() * speedMultiplier),
              (int) (vehicleControl.getRight() * speedMultiplier));
      usbConnection.send(message);
    }
  }

  protected void sendIndicatorToVehicle(int vehicleIndicator) {
    if (usbConnection != null && usbConnection.isOpen() && !usbConnection.isBusy()) {
      String message = String.format("i%d\n", vehicleIndicator);
      usbConnection.send(message);
    }
  }

  public static Context getContext() {
    return mContext;
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (buttonView == connectionSwitchCompat) {
      toggleConnection(isChecked);
    } else if (buttonView == driveModeSwitchCompat) {
      setDriveByNetwork(isChecked);
    } else if (buttonView == loggerSwitchCompat) {
      setIsLoggingActive(isChecked);
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
    if (parent == baudRateSpinner) {
      setBaudRate(Integer.parseInt(parent.getItemAtPosition(pos).toString()));
    } else if (parent == modelSpinner) {
      setModel(Model.valueOf(parent.getItemAtPosition(pos).toString().toUpperCase()));
    } else if (parent == deviceSpinner) {
      setDevice(Device.valueOf(parent.getItemAtPosition(pos).toString().toUpperCase()));
    } else if (parent == driveModeSpinner) {
      setDriveMode(DriveMode.valueOf(parent.getItemAtPosition(pos).toString().toUpperCase()));
    } else if (parent == loggerSpinner) {
      setLogMode(LogMode.valueOf(parent.getItemAtPosition(pos).toString().toUpperCase()));
    } else if (parent == controlSpinner) {
      setControlSpeed(ControlSpeed.valueOf(parent.getItemAtPosition(pos).toString().toUpperCase()));
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Do nothing.
  }
}
