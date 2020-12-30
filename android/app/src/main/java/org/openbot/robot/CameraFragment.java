package org.openbot.robot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openbot.R;
import org.openbot.env.Logger;

public abstract class CameraFragment extends Fragment {

  private ExecutorService cameraExecutor;
  private final int PERMISSIONS_REQUEST_CODE = 10;
  private String[] PERMISSIONS_REQUIRED = new String[] {Manifest.permission.CAMERA};
  private static final Logger LOGGER = new Logger();
  private PreviewView previewView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

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

    if (allPermissionsGranted()) startCamera();
    else requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);

    return cameraView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    cameraExecutor = Executors.newSingleThreadExecutor();
  }

  private void startCamera() {
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
        ProcessCameraProvider.getInstance(requireContext());

    cameraProviderFuture.addListener(
        () -> {
          try {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
            Preview preview = new Preview.Builder().build();

            CameraSelector cameraSelector =
                new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();

            preview.setSurfaceProvider(previewView.getSurfaceProvider());

            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();

            // insert your code here.
            imageAnalysis.setAnalyzer(cameraExecutor, (ImageAnalysis.Analyzer) this::processFrame);

            try {
              cameraProvider.unbindAll();

              cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
            } catch (Exception e) {
              LOGGER.e("Use case binding failed: %s", e);
            }
          } catch (ExecutionException | InterruptedException e) {
            LOGGER.e(e.toString());
          }
        },
        ContextCompat.getMainExecutor(requireContext()));
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSIONS_REQUEST_CODE) {
      if (allPermissionsGranted()) {
        startCamera();
      }
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    cameraExecutor.shutdown();
  }

  private boolean allPermissionsGranted() {
    boolean permissionsGranted = false;
    for (String permission : PERMISSIONS_REQUIRED)
      permissionsGranted =
          ContextCompat.checkSelfPermission(requireContext(), permission)
              == PackageManager.PERMISSION_GRANTED;

    return permissionsGranted;
  }

  protected abstract void processFrame(ImageProxy image);
}
