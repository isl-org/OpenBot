package org.openbot.projects;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageProxy;

import org.openbot.common.CameraFragment;
import org.openbot.databinding.FragmentBlocklyExecutingBinding;
import org.openbot.env.ImageUtils;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.tflite.Autopilot;
import org.openbot.tflite.Detector;
import org.openbot.tflite.Model;
import org.openbot.tflite.Network;
import org.openbot.tracking.MultiBoxTracker;
import org.openbot.utils.CameraUtils;
import org.openbot.utils.Enums;
import org.openbot.utils.FileUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class BlocklyExecutingFragment extends CameraFragment {

  private FragmentBlocklyExecutingBinding binding;
  private WebView myWebView;
  private SharedPreferencesManager sharedPreferencesManager;
  private boolean isRunJSCommand = false;
  public static boolean isFollow = false;
  public static boolean isAutopilot = false;
  public static String classType = "person";
  public static String modelName = "";
  private Detector detector;
  private Autopilot autopilot;
  private Model setModel;
  private List<Model> modelList;
  private Bitmap croppedBitmap;
  private Matrix frameToCropTransform;
  private Network.Device getDevice;
  private int sensorOrientation;
  private MultiBoxTracker tracker;
  private long frameNum = 0;
  private boolean computingNetwork = false;
  private Handler handler;
  private Matrix cropToFrameTransform;
  public static float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    sharedPreferencesManager = new SharedPreferencesManager(requireContext());
    HandlerThread handlerThread = new HandlerThread("inference");
    handlerThread.start();
    handler = new Handler(handlerThread.getLooper());
    // initialise web view to execute javascript block codes.
    myWebView = new WebView(getContext());
    // enable JavaScript in the web-view.
    myWebView.getSettings().setJavaScriptEnabled(true);
    if (savedInstanceState == null) {
      isRunJSCommand = true;
    } else {
      int orientation = getResources().getConfiguration().orientation;
      if (orientation == Configuration.ORIENTATION_LANDSCAPE
          || orientation == Configuration.ORIENTATION_PORTRAIT) {
        myWebView.destroy();
        showAlertDialog();
      }
    }
    // Inflate the layout for Blocks code executing Fragment.
    binding = FragmentBlocklyExecutingBinding.inflate(inflater, container, false);
    return inflateFragment(binding, inflater, container);
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding.stopCarBtn.setOnClickListener(
        v -> {
          myWebView.destroy();
          binding.jsCommand.setText(getString(android.R.string.cancel));
          vehicle.stopBot();
          vehicle.setIndicator(0);
          isFollow = false;
          isAutopilot = false;
        });
    // if string js code variable is not null execute js code when you navigate on this fragment.
    if (BarCodeScannerFragment.finalCode != null && isRunJSCommand) {
      runJSCommand(BarCodeScannerFragment.finalCode);
    }

    modelList = FileUtils.loadConfigJSONFromAsset(requireActivity());

    switch (preferencesManager.getDevice()){
      case 0:
        getDevice = Network.Device.CPU;
        break;
      case 1:
        getDevice = Network.Device.GPU;
        break;
      case 2:
        getDevice = Network.Device.NNAPI;
        break;
    }
  }

  @Override
  protected void processFrame(Bitmap bitmap, ImageProxy imageProxy) {
    if (isFollow) startFollowObject(bitmap);
    if (isAutopilot) startAutopilot(bitmap);
  }

  private Model getModel() {
    for (Model findModel : modelList){
      if (FileUtils.nameWithoutExtension(findModel.getName()).equals(modelName)){
        setModel = findModel;
      }
    }
    return setModel;
  }

  private void startAutopilot(Bitmap bitmap){
    if (tracker == null) updateCropImageInfo();

    ++frameNum;
      // If network is busy, return.
      if (computingNetwork) {
        return;
      }

      computingNetwork = true;
      Timber.i("Putting image " + frameNum + " for detection in bg thread.");

    if (handler != null) {
      handler.post(()->{
                final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(bitmap, frameToCropTransform, null);

                if (autopilot != null) {
                  Timber.i("Running autopilot on image %s", frameNum);
                  vehicle.setControl(autopilot.recognizeImage(croppedBitmap, vehicle.getIndicator()));
                }
                computingNetwork = false;
              });}
  }
  private void startFollowObject(Bitmap bitmap){
    if (tracker == null) updateCropImageInfo();

    ++frameNum;

    if (computingNetwork) {
      return;
    }

    computingNetwork = true;
    if (handler != null) {
      handler.post(()->{

        final Canvas canvas = new Canvas(croppedBitmap);
        if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
          canvas.drawBitmap(
                  CameraUtils.flipBitmapHorizontal(bitmap), frameToCropTransform, null);
        } else {
          canvas.drawBitmap(bitmap, frameToCropTransform, null);
        }

        if (detector != null) {
          Timber.i("Running detection on image %s", frameNum);
          final List<Detector.Recognition> results =
                  detector.recognizeImage(croppedBitmap, classType);
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

          final List<Detector.Recognition> mappedRecognitions = new LinkedList<>();

          for (final Detector.Recognition result : results) {
            final RectF location = result.getLocation();
            if (location != null && result.getConfidence() >= MINIMUM_CONFIDENCE_TF_OD_API) {
              cropToFrameTransform.mapRect(location);
              result.setLocation(location);
              mappedRecognitions.add(result);
            }
          }
          tracker.trackResults(mappedRecognitions, frameNum);
          if (isFollow) vehicle.setControl(tracker.updateTarget());
          else vehicle.stopBot();
        }

        computingNetwork = false;
      });
    }
  }

  private void updateCropImageInfo() {
    frameToCropTransform = null;

    sensorOrientation = 90 - ImageUtils.getScreenOrientation(requireActivity());

    tracker = new MultiBoxTracker(requireContext());
    tracker.setDynamicSpeed(preferencesManager.getDynamicSpeed());

    Timber.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

    recreateNetwork(getModel(), getDevice, preferencesManager.getNumThreads());
    if (detector == null) {
      Timber.e("No network on preview!");
      return;
    }

    tracker.setFrameConfiguration(
            getMaxAnalyseImageSize().getWidth(),
            getMaxAnalyseImageSize().getHeight(),
            sensorOrientation);
  }

  private void recreateNetwork(Model model, Network.Device device, int numThreads) {

    if (model == null) return;
    tracker.clearTrackedObjects();
    if (detector != null) {
      Timber.d("Closing detector.");
      detector.close();
      detector = null;
    }

    try {
      Timber.d("Creating detector (model=%s, device=%s, numThreads=%d)", model, device, numThreads);
      if (isFollow) {
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

      } else if (isAutopilot) {
        autopilot = new Autopilot(requireActivity(), model, device, numThreads);
        croppedBitmap = Bitmap.createBitmap(
                autopilot.getImageSizeX(), autopilot.getImageSizeY(), Bitmap.Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        getMaxAnalyseImageSize().getWidth(),
                        getMaxAnalyseImageSize().getHeight(),
                        croppedBitmap.getWidth(),
                        croppedBitmap.getHeight(),
                        sensorOrientation,
                        autopilot.getCropRect(),
                        autopilot.getMaintainAspect());
      }

      cropToFrameTransform = new Matrix();
      frameToCropTransform.invert(cropToFrameTransform);

    } catch (IllegalArgumentException | IOException e) {
      Timber.e("Failed to create network.");
    }
  }

  private void showAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
    builder.setTitle("Rotation Detected").setMessage("Do you want to restart Blockly command?");
    builder.setPositiveButton("Yes", (dialog, id) -> restartJSCommand());
    builder.setNegativeButton("Cancel", (dialog, id) -> requireActivity().onBackPressed());
    AlertDialog dialog = builder.create();
    dialog.show();
    dialog.setCancelable(false);
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void restartJSCommand() {
    // initialise web view to execute javascript block codes.
    myWebView = new WebView(getContext());
    // enable JavaScript in the web-view.
    myWebView.getSettings().setJavaScriptEnabled(true);
    if (myWebView != null && BarCodeScannerFragment.finalCode != null) {
      runJSCommand(BarCodeScannerFragment.finalCode);
    }
  }

  /**
   * get javascript code in string from googleDrive file and run execute in webView.
   *
   */
  private void runJSCommand(String finalCode) {
    Activity activity = getActivity();
    if (activity != null) {
      activity.runOnUiThread(
          () -> {
            // set the speed multiplier to maximum value (255) because openBot moving according to
            vehicle.setSpeedMultiplier(Enums.SpeedMode.FAST.getValue());
            // add a JavaScript interface to the web-view.
            myWebView.addJavascriptInterface(
                new BotFunctions(
                    vehicle,
                    audioPlayer,
                    sharedPreferencesManager,
                    requireContext(),
                    binding,
                    requireActivity()),
                "Android");
            // execute the JavaScript code in the web-view.
            myWebView.evaluateJavascript(finalCode, null);
          });
    }
  }

  @Override
  protected void processControllerKeyData(String command) {}

  @Override
  protected void processUSBData(String data) {}

  @Override
  public void onPause() {
    super.onPause();
    myWebView.destroy();
    vehicle.setIndicator(0);
    vehicle.stopBot();
    // if previous speed multiplier value is not 0, set the speed multiplier back to its previous
    // value when you go back from this screen.
    Enums.SpeedMode speedMode = Enums.SpeedMode.getByID(preferencesManager.getSpeedMode());
    assert speedMode != null;
    preferencesManager.setSpeedMode(speedMode.getValue());
    vehicle.setSpeedMultiplier(speedMode.getValue());
  }
}
