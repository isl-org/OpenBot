package org.openbot.objectNav;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageProxy;
import androidx.navigation.Navigation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.CameraFragment;
import org.openbot.databinding.FragmentObjectNavBinding;
import org.openbot.env.BorderedText;
import org.openbot.env.ImageUtils;
import org.openbot.tflite.Detector;
import org.openbot.tflite.Model;
import org.openbot.tflite.Network;
import org.openbot.tracking.MultiBoxTracker;
import org.openbot.utils.CameraUtils;
import org.openbot.utils.Constants;
import org.openbot.utils.Enums;
import org.openbot.utils.MovingAverage;
import org.openbot.utils.PermissionUtils;
import org.openbot.vehicle.Control;
import timber.log.Timber;

public class ObjectNavFragment extends CameraFragment {
  private FragmentObjectNavBinding binding;
  private Handler handler;
  private HandlerThread handlerThread;

  private boolean computingNetwork = false;
  public static float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;

  private static final float TEXT_SIZE_DIP = 10;

  private Detector detector;

  private boolean mirrorControl;
  private Matrix frameToCropTransform;
  private Bitmap croppedBitmap;
  private int sensorOrientation;
  private Bitmap cropCopyBitmap;
  private Matrix cropToFrameTransform;

  private MultiBoxTracker tracker;

  private Model model;
  private Network.Device device = Network.Device.CPU;
  private int numThreads = -1;
  private String classType = "person";

  private long lastProcessingTimeMs = -1;
  private long frameNum = 0;

  private final boolean isBenchmarkMode = false;
  private long processedFrames = 0;
  private final int movingAvgSize = 100;
  private MovingAverage movingAvgProcessingTimeMs = new MovingAverage(movingAvgSize);

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentObjectNavBinding.inflate(inflater, container, false);

    return inflateFragment(binding, inflater, container);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding.confidenceValue.setText((int) (MINIMUM_CONFIDENCE_TF_OD_API * 100) + "%");

    binding.plusConfidence.setOnClickListener(
        v -> {
          String trimConfValue = binding.confidenceValue.getText().toString().trim();
          int confValue = Integer.parseInt(trimConfValue.substring(0, trimConfValue.length() - 1));
          if (confValue >= 95) return;
          confValue += 5;
          binding.confidenceValue.setText(confValue + "%");
          MINIMUM_CONFIDENCE_TF_OD_API = confValue / 100f;
        });
    binding.minusConfidence.setOnClickListener(
        v -> {
          String trimConfValue = binding.confidenceValue.getText().toString().trim();
          int confValue = Integer.parseInt(trimConfValue.substring(0, trimConfValue.length() - 1));
          if (confValue <= 5) return;
          confValue -= 5;
          binding.confidenceValue.setText(confValue + "%");
          MINIMUM_CONFIDENCE_TF_OD_API = confValue / 100f;
        });

    binding.controllerContainer.speedInfo.setText(getString(R.string.speedInfo, "---,---"));

    if (vehicle.getConnectionType().equals("USB")) {
      binding.usbToggle.setVisibility(View.VISIBLE);
      binding.bleToggle.setVisibility(View.GONE);
    } else if (vehicle.getConnectionType().equals("Bluetooth")) {
      binding.bleToggle.setVisibility(View.VISIBLE);
      binding.usbToggle.setVisibility(View.GONE);
    }

    classType = preferencesManager.getObjectType();
    binding.classType.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            classType = parent.getItemAtPosition(position).toString();
            preferencesManager.setObjectType(classType);
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
    binding.deviceSpinner.setSelection(preferencesManager.getDevice());
    setNumThreads(preferencesManager.getNumThreads());
    binding.threads.setText(String.valueOf(getNumThreads()));

    binding.cameraToggle.setOnClickListener(v -> toggleCamera());

    binding.mirrorControl.setOnClickListener(v -> mirrorControl());

    List<String> models =
        getModelNames(f -> f.type.equals(Model.TYPE.DETECTOR) && f.pathType != Model.PATH_TYPE.URL);
    initModelSpinner(binding.modelSpinner, models, preferencesManager.getObjectNavModel());

    setAnalyserResolution(Enums.Preview.HD.getValue());
    binding.deviceSpinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            setDevice(Network.Device.valueOf(selected.toUpperCase()));
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

    binding.plus.setOnClickListener(
        v -> {
          String threads = binding.threads.getText().toString().trim();
          int numThreads = Integer.parseInt(threads);
          if (numThreads >= 9) return;
          setNumThreads(++numThreads);
          binding.threads.setText(String.valueOf(numThreads));
        });
    binding.minus.setOnClickListener(
        v -> {
          String threads = binding.threads.getText().toString().trim();
          int numThreads = Integer.parseInt(threads);
          if (numThreads == 1) return;
          setNumThreads(--numThreads);
          binding.threads.setText(String.valueOf(numThreads));
        });
    BottomSheetBehavior.from(binding.aiBottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

    mViewModel
        .getUsbStatus()
        .observe(getViewLifecycleOwner(), status -> binding.usbToggle.setChecked(status));

    binding.usbToggle.setChecked(vehicle.isUsbConnected());
    binding.bleToggle.setChecked(vehicle.bleConnected());

    binding.usbToggle.setOnClickListener(
        v -> {
          binding.usbToggle.setChecked(vehicle.isUsbConnected());
          Navigation.findNavController(requireView()).navigate(R.id.open_usb_fragment);
        });
    binding.bleToggle.setOnClickListener(
        v -> {
          binding.bleToggle.setChecked(vehicle.bleConnected());
          Navigation.findNavController(requireView()).navigate(R.id.open_bluetooth_fragment);
        });

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

    binding.autoSwitch.setOnClickListener(v -> setNetworkEnabled(binding.autoSwitch.isChecked()));
    binding.dynamicSpeed.setChecked(preferencesManager.getDynamicSpeed());
    binding.dynamicSpeed.setOnClickListener(
        v -> {
          preferencesManager.setDynamicSpeed(binding.dynamicSpeed.isChecked());
          tracker.setDynamicSpeed(preferencesManager.getDynamicSpeed());
        });
  }

  private void mirrorControl() {
    mirrorControl = !mirrorControl;
  }

  private void updateCropImageInfo() {
    //    Timber.i("%s x %s",getPreviewSize().getWidth(), getPreviewSize().getHeight());
    //    Timber.i("%s x %s",getMaxAnalyseImageSize().getWidth(),
    //     getMaxAnalyseImageSize().getHeight());
    frameToCropTransform = null;

    sensorOrientation = 90 - ImageUtils.getScreenOrientation(requireActivity());

    final float textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    BorderedText borderedText = new BorderedText(textSizePx);
    borderedText.setTypeface(Typeface.MONOSPACE);

    tracker = new MultiBoxTracker(requireContext());
    tracker.setDynamicSpeed(preferencesManager.getDynamicSpeed());

    Timber.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

    recreateNetwork(getModel(), getDevice(), getNumThreads());
    if (detector == null) {
      Timber.e("No network on preview!");
      return;
    }

    binding.trackingOverlay.addCallback(
        canvas -> {
          tracker.draw(canvas);
          //          tracker.drawDebug(canvas);
        });
    tracker.setFrameConfiguration(
        getMaxAnalyseImageSize().getWidth(),
        getMaxAnalyseImageSize().getHeight(),
        sensorOrientation);
  }

  protected void onInferenceConfigurationChanged() {
    computingNetwork = false;
    if (croppedBitmap == null) {
      // Defer creation until we're getting camera frames.
      return;
    }
    final Network.Device device = getDevice();
    final Model model = getModel();
    final int numThreads = getNumThreads();
    runInBackground(() -> recreateNetwork(model, device, numThreads));
  }

  private void recreateNetwork(Model model, Network.Device device, int numThreads) {
    resetFpsUi();
    if (model == null) return;
    tracker.clearTrackedObjects();
    if (detector != null) {
      Timber.d("Closing detector.");
      detector.close();
      detector = null;
    }

    try {
      Timber.d("Creating detector (model=%s, device=%s, numThreads=%d)", model, device, numThreads);
      detector = Detector.create(requireActivity(), model, device, numThreads);

      assert detector != null;
      croppedBitmap =
          Bitmap.createBitmap(
              detector.getImageSizeX(), detector.getImageSizeY(), Bitmap.Config.ARGB_8888);
      frameToCropTransform =
          ImageUtils.getTransformationMatrix(
              getMaxAnalyseImageSize().getWidth(),
              getMaxAnalyseImageSize().getHeight(),
              croppedBitmap.getWidth(),
              croppedBitmap.getHeight(),
              sensorOrientation,
              detector.getCropRect(),
              detector.getMaintainAspect());

      cropToFrameTransform = new Matrix();
      frameToCropTransform.invert(cropToFrameTransform);

      requireActivity()
          .runOnUiThread(
              () -> {
                ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        detector.getLabels());
                binding.classType.setAdapter(adapter);
                binding.classType.setSelection(
                    detector.getLabels().indexOf(preferencesManager.getObjectType()));
                binding.inputResolution.setText(
                    String.format(
                        Locale.getDefault(),
                        "%dx%d",
                        detector.getImageSizeX(),
                        detector.getImageSizeY()));
              });

    } catch (IllegalArgumentException | IOException e) {
      String msg = "Failed to create network.";
      Timber.e(e, msg);
      requireActivity()
          .runOnUiThread(
              () ->
                  Toast.makeText(
                          requireContext().getApplicationContext(),
                          e.getMessage(),
                          Toast.LENGTH_LONG)
                      .show());
    }
  }

  @Override
  public synchronized void onResume() {
    croppedBitmap = null;
    tracker = null;
    handlerThread = new HandlerThread("inference");
    handlerThread.start();
    handler = new Handler(handlerThread.getLooper());
    binding.bleToggle.setChecked(vehicle.bleConnected());
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

  @Override
  protected void processUSBData(String data) {
    binding.controllerContainer.speedInfo.setText(
        getString(
            R.string.speedInfo,
            String.format(
                Locale.US, "%3.0f,%3.0f", vehicle.getLeftWheelRpm(), vehicle.getRightWheelRpm())));
  }

  @Override
  protected void processControllerKeyData(String commandType) {
    switch (commandType) {
      case Constants.CMD_DRIVE:
        binding.controllerContainer.controlInfo.setText(
            String.format(Locale.US, "%.0f,%.0f", vehicle.getLeftSpeed(), vehicle.getRightSpeed()));
        break;

      case Constants.CMD_NETWORK:
        setNetworkEnabledWithAudio(!binding.autoSwitch.isChecked());
        break;
    }
  }

  private void setNetworkEnabledWithAudio(boolean b) {
    setNetworkEnabled(b);

    if (b) audioPlayer.play(voice, "network_enabled.mp3");
    else audioPlayer.playDriveMode(voice, vehicle.getDriveMode());
  }

  private void setNetworkEnabled(boolean b) {
    binding.autoSwitch.setChecked(b);

    binding.controllerContainer.controlMode.setEnabled(!b);
    binding.controllerContainer.driveMode.setEnabled(!b);
    binding.controllerContainer.speedMode.setEnabled(!b);

    binding.controllerContainer.controlMode.setAlpha(b ? 0.5f : 1f);
    binding.controllerContainer.driveMode.setAlpha(b ? 0.5f : 1f);
    binding.controllerContainer.speedMode.setAlpha(b ? 0.5f : 1f);

    resetFpsUi();
    if (!b) handler.postDelayed(() -> vehicle.setControl(0, 0), Math.max(lastProcessingTimeMs, 50));
  }

  @Override
  protected void processFrame(Bitmap bitmap, ImageProxy image) {
    if (tracker == null) updateCropImageInfo();

    ++frameNum;
    if (binding != null && binding.autoSwitch.isChecked()) {
      // If network is busy, return.
      if (computingNetwork) {
        return;
      }

      computingNetwork = true;
      Timber.i("Putting image " + frameNum + " for detection in bg thread.");

      runInBackground(
          () -> {
            final Canvas canvas = new Canvas(croppedBitmap);
            if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
              canvas.drawBitmap(
                  CameraUtils.flipBitmapHorizontal(bitmap), frameToCropTransform, null);
            } else {
              canvas.drawBitmap(bitmap, frameToCropTransform, null);
            }

            if (detector != null) {
              Timber.i("Running detection on image %s", frameNum);
              final long startTime = SystemClock.elapsedRealtime();
              final List<Detector.Recognition> results =
                  detector.recognizeImage(croppedBitmap, classType);
              lastProcessingTimeMs = SystemClock.elapsedRealtime() - startTime;

              if (!results.isEmpty())
                Timber.i(
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
              paint.setStyle(Paint.Style.STROKE);
              paint.setStrokeWidth(2.0f);

              final List<Detector.Recognition> mappedRecognitions = new LinkedList<>();

              for (final Detector.Recognition result : results) {
                final RectF location = result.getLocation();
                if (location != null && result.getConfidence() >= MINIMUM_CONFIDENCE_TF_OD_API) {
                  canvas1.drawRect(location, paint);
                  cropToFrameTransform.mapRect(location);
                  result.setLocation(location);
                  mappedRecognitions.add(result);
                }
              }

              tracker.trackResults(mappedRecognitions, frameNum);
              Control target = tracker.updateTarget();
              if (mirrorControl) {
                handleDriveCommand(target.mirror());
              } else {
                handleDriveCommand(target);
              }
              binding.trackingOverlay.postInvalidate();
            }

            computingNetwork = false;
          });
      if (lastProcessingTimeMs > 0) {
        if (isBenchmarkMode) {
          double avgProcessingTimeMs = movingAvgProcessingTimeMs.next(lastProcessingTimeMs);
          processedFrames += 1;
          if (processedFrames >= movingAvgSize) updateFpsUi(avgProcessingTimeMs);
        } else updateFpsUi(lastProcessingTimeMs);
      }
    }
  }

  private void updateFpsUi(double processingTimeMs) {
    requireActivity()
        .runOnUiThread(
            () ->
                binding.inferenceInfo.setText(
                    String.format(Locale.US, "%.1f fps", 1000.f / processingTimeMs)));
  }

  private void resetFpsUi() {
    processedFrames = 0;
    movingAvgProcessingTimeMs = new MovingAverage(movingAvgSize);
    requireActivity().runOnUiThread(() -> binding.inferenceInfo.setText(R.string.time_fps));
  }

  protected void handleDriveCommand(Control control) {
    vehicle.setControl(control);
    float left = vehicle.getLeftSpeed();
    float right = vehicle.getRightSpeed();
    requireActivity()
        .runOnUiThread(
            () ->
                binding.controllerContainer.controlInfo.setText(
                    String.format(Locale.US, "%.0f,%.0f", left, right)));
  }

  protected Model getModel() {
    return model;
  }

  @Override
  protected void setModel(Model model) {
    if (this.model != model) {
      Timber.d("Updating  model: %s", model);
      this.model = model;
      preferencesManager.setObjectNavModel(model.name);
      onInferenceConfigurationChanged();
    }
  }

  protected Network.Device getDevice() {
    return device;
  }

  private void setDevice(Network.Device device) {
    if (this.device != device) {
      Timber.d("Updating  device: %s", device);
      this.device = device;
      final boolean threadsEnabled = device == Network.Device.CPU;
      binding.plus.setEnabled(threadsEnabled);
      binding.minus.setEnabled(threadsEnabled);
      binding.threads.setText(threadsEnabled ? String.valueOf(numThreads) : "N/A");
      if (threadsEnabled) binding.threads.setTextColor(Color.BLACK);
      else binding.threads.setTextColor(Color.GRAY);
      preferencesManager.setDevice(device.ordinal());
      onInferenceConfigurationChanged();
    }
  }

  protected int getNumThreads() {
    return numThreads;
  }

  private void setNumThreads(int numThreads) {
    if (this.numThreads != numThreads) {
      Timber.d("Updating  numThreads: %s", numThreads);
      this.numThreads = numThreads;
      preferencesManager.setNumThreads(numThreads);
      onInferenceConfigurationChanged();
    }
  }

  private String[] getModelFiles() {
    return requireActivity().getFilesDir().list((dir1, name) -> name.endsWith(".tflite"));
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
}
