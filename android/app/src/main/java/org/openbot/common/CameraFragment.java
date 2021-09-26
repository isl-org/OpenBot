package org.openbot.common;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.env.ImageUtils;
import org.openbot.utils.Constants;
import org.openbot.utils.Enums;
import org.openbot.utils.PermissionUtils;
import org.openbot.utils.YuvToRgbConverter;
import timber.log.Timber;

public abstract class CameraFragment extends ControlsFragment {

  private ExecutorService cameraExecutor;
  private PreviewView previewView;
  private Preview preview;
  private static int lensFacing = CameraSelector.LENS_FACING_BACK;
  private ProcessCameraProvider cameraProvider;
  private Size analyserResolution = Enums.Preview.HD.getValue();
  private YuvToRgbConverter converter;
  private Bitmap bitmapBuffer;
  private int rotationDegrees;
  private VideoCapture videoCapture;
  private boolean freshPermissionsGranted = false;

  protected View inflateFragment(int resId, LayoutInflater inflater, ViewGroup container) {
    return addCamera(inflater.inflate(resId, container, false), inflater, container);
  }

  protected View inflateFragment(
      ViewBinding viewBinding, LayoutInflater inflater, ViewGroup container) {
    return addCamera(viewBinding.getRoot(), inflater, container);
  }

  private View addCamera(View view, LayoutInflater inflater, ViewGroup container) {
    View cameraView = inflater.inflate(R.layout.fragment_camera, container, false);
    ViewGroup rootView = (ViewGroup) cameraView.getRootView();

    previewView = cameraView.findViewById(R.id.viewFinder);
    rootView.addView(view);
    videoCapture = new VideoCapture.Builder().build();

    if (!PermissionUtils.hasCameraPermission(requireActivity())) {
      requestPermissionLauncherCamera.launch(Constants.PERMISSION_CAMERA);
    } else if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_CAMERA)) {
      PermissionUtils.showCameraPermissionsPreviewToast(requireActivity());
    } else {
      setupCamera();
    }
    return cameraView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    cameraExecutor = Executors.newSingleThreadExecutor();
  }

  @SuppressLint("RestrictedApi")
  private void setupCamera() {
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
        ProcessCameraProvider.getInstance(requireContext());

    cameraProviderFuture.addListener(
        () -> {
          try {
            cameraProvider = cameraProviderFuture.get();
            bindCameraUseCases();
          } catch (ExecutionException | InterruptedException e) {
            Timber.e("Camera setup failed: %s", e.toString());
          }
        },
        ContextCompat.getMainExecutor(requireContext()));
  }

  @SuppressLint({"UnsafeExperimentalUsageError", "UnsafeOptInUsageError"})
  private void bindCameraUseCases() {
    converter = new YuvToRgbConverter(requireContext());
    bitmapBuffer = null;
    videoCapture = new VideoCapture.Builder().build();
    preview = new Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build();
    final boolean rotated = ImageUtils.getScreenOrientation(requireActivity()) % 180 == 90;
    final PreviewView.ScaleType scaleType =
        rotated ? PreviewView.ScaleType.FIT_CENTER : PreviewView.ScaleType.FIT_START;
    previewView.setScaleType(scaleType);
    preview.setSurfaceProvider(previewView.getSurfaceProvider());
    CameraSelector cameraSelector =
        new CameraSelector.Builder().requireLensFacing(lensFacing).build();
    ImageAnalysis imageAnalysis;

    if (analyserResolution == null)
      imageAnalysis =
          new ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build();
    else
      imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(analyserResolution).build();
    // insert your code here.
    imageAnalysis.setAnalyzer(
        cameraExecutor,
        image -> {
          if (bitmapBuffer == null)
            bitmapBuffer =
                Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);

          rotationDegrees = image.getImageInfo().getRotationDegrees();
          converter.yuvToRgb(image.getImage(), bitmapBuffer);
          image.close();

          processFrame(bitmapBuffer, image);
        });
    try {
      if (cameraProvider != null) {
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis, videoCapture);
      }
    } catch (Exception e) {
      Timber.e("Use case binding failed: %s", e.toString());
    }
  }

  public int getRotationDegrees() {
    return rotationDegrees;
  }

  private final ActivityResultLauncher<String> requestPermissionLauncherCamera =
      registerForActivityResult(
          new ActivityResultContracts.RequestPermission(),
          isGranted -> {
            if (isGranted) {
              setupCamera();
              freshPermissionsGranted = true;
            } else if (PermissionUtils.shouldShowRational(
                requireActivity(), Constants.PERMISSION_CAMERA)) {
              PermissionUtils.showCameraPermissionsPreviewToast(requireActivity());
            } else {

            }
          });

  @Override
  public void onDestroy() {
    super.onDestroy();
    cameraExecutor.shutdown();
  }

  @SuppressLint("RestrictedApi")
  public Size getPreviewSize() {
    return preview.getAttachedSurfaceResolution();
  }

  public Size getMaxAnalyseImageSize() {
    return new Size(bitmapBuffer.getWidth(), bitmapBuffer.getHeight());
  }

  public void toggleCamera() {
    lensFacing =
        CameraSelector.LENS_FACING_FRONT == lensFacing
            ? CameraSelector.LENS_FACING_BACK
            : CameraSelector.LENS_FACING_FRONT;
    bindCameraUseCases();
  }

  public void setAnalyserResolution(Size resolutionSize) {
    if (resolutionSize == null) analyserResolution = null;
    else {
      if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        this.analyserResolution = new Size(resolutionSize.getHeight(), resolutionSize.getWidth());
      else this.analyserResolution = resolutionSize;
    }
    bindCameraUseCases();
  }

  @SuppressLint("RestrictedApi")
  public void startVideoRecording() {
    if (freshPermissionsGranted) {
      videoCapture = new VideoCapture.Builder().build();
      bindCameraUseCases();
    }
    String outputDirectory =
        Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + getString(R.string.app_name)
            + File.separator
            + "videos";
    final File myDir = new File(outputDirectory);

    if (!myDir.exists()) {
      if (!myDir.mkdirs()) {
        Timber.i("Make dir failed");
      }
    }

    File videoFile =
        new File(
            outputDirectory,
            new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(System.currentTimeMillis())
                + ".mp4");
    VideoCapture.OutputFileOptions outputOptions =
        new VideoCapture.OutputFileOptions.Builder(videoFile).build();

    videoCapture.startRecording(
        outputOptions,
        ContextCompat.getMainExecutor(requireContext()),
        new VideoCapture.OnVideoSavedCallback() {
          @Override
          public void onVideoSaved(
              @NonNull @NotNull VideoCapture.OutputFileResults outputFileResults) {
            Uri savedUri = Uri.fromFile(videoFile);
            Timber.d("Video capture succeeded:" + savedUri);
          }

          @Override
          public void onError(
              int videoCaptureError,
              @NonNull @NotNull String message,
              @Nullable @org.jetbrains.annotations.Nullable Throwable cause) {
            Timber.e("Video capture failed: " + message);
          }
        });
  }

  @SuppressLint("RestrictedApi")
  public void stopVideoRecording() {
    videoCapture.stopRecording();
  }

  protected abstract void processFrame(Bitmap image, ImageProxy imageProxy);
}
