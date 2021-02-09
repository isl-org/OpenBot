package org.openbot.robot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openbot.R;
import org.openbot.common.Enums;
import org.openbot.common.YuvToRgbConverter;
import org.openbot.env.Logger;
import org.openbot.main.ControlsFragment;

public abstract class CameraFragment extends ControlsFragment {

  private ExecutorService cameraExecutor;
  private final int PERMISSIONS_REQUEST_CODE = 10;
  private final String[] PERMISSIONS_REQUIRED = new String[] {Manifest.permission.CAMERA};
  private static final Logger LOGGER = new Logger();
  private PreviewView previewView;
  private Preview preview;
  private int lensFacing = CameraSelector.LENS_FACING_BACK;
  private ProcessCameraProvider cameraProvider;
  private Size analyserResolution = Enums.Preview.FULL_HD.getValue();
  private YuvToRgbConverter converter;
  private Bitmap bitmapBuffer;

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

    if (allPermissionsGranted()) setupCamera();
    else requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);

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
            LOGGER.e(e.toString());
          }
        },
        ContextCompat.getMainExecutor(requireContext()));
  }

  @SuppressLint("UnsafeExperimentalUsageError")
  private void bindCameraUseCases() {
    converter = new YuvToRgbConverter(requireContext());
    bitmapBuffer = null;
    preview = new Preview.Builder().build();
    preview.setSurfaceProvider(previewView.getSurfaceProvider());

    CameraSelector cameraSelector =
        new CameraSelector.Builder().requireLensFacing(lensFacing).build();

    ImageAnalysis imageAnalysis =
        new ImageAnalysis.Builder().setTargetResolution(analyserResolution).build();

    // insert your code here.
    imageAnalysis.setAnalyzer(
        cameraExecutor,
        image -> {
          if (bitmapBuffer == null)
            bitmapBuffer =
                Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);

          converter.yuvToRgb(image.getImage(), bitmapBuffer);
          image.close();
          processFrame(bitmapBuffer, image);
        });

    try {
      cameraProvider.unbindAll();

      cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    } catch (Exception e) {
      LOGGER.e("Use case binding failed: %s", e);
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSIONS_REQUEST_CODE) {
      if (allPermissionsGranted()) {
        setupCamera();
      }
    }
  }

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
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
      this.analyserResolution = new Size(resolutionSize.getHeight(), resolutionSize.getWidth());
    else this.analyserResolution = resolutionSize;
    bindCameraUseCases();
  }

  private boolean allPermissionsGranted() {
    boolean permissionsGranted = false;
    for (String permission : PERMISSIONS_REQUIRED)
      permissionsGranted =
          ContextCompat.checkSelfPermission(requireContext(), permission)
              == PackageManager.PERMISSION_GRANTED;

    return permissionsGranted;
  }

  protected abstract void processFrame(Bitmap image, ImageProxy imageProxy);
}
