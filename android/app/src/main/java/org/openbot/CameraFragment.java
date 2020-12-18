package org.openbot;

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
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;

import org.openbot.env.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class CameraFragment extends Fragment {


	private ExecutorService cameraExecutor;
	private final int PERMISSIONS_REQUEST_CODE = 10;
	private String[] PERMISSIONS_REQUIRED = new String[]{Manifest.permission.CAMERA};
	private static final Logger LOGGER = new Logger();
	private PreviewView previewView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected View inflateFragment(int resId, LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(resId, container, false);
		return addCamera(view, inflater, container);
	}

	protected View addCamera(View view, LayoutInflater inflater, ViewGroup container)
	{
		if (view.getRootView() instanceof ViewGroup) {
			ViewGroup rootView = (ViewGroup) view.getRootView();
			previewView = (PreviewView) inflater.inflate(R.layout.fragment_camera, container, false);
			rootView.addView(previewView, 0);

			if (allPermissionsGranted())
				startCamera();
			else
				requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);
		}
		return view;
	}



	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		cameraExecutor = Executors.newSingleThreadExecutor();
	}

	private void startCamera() {
		ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

		cameraProviderFuture.addListener(() -> {
			try {
				ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
				Preview preview = new Preview.Builder()
						.build();

				CameraSelector cameraSelector = new CameraSelector.Builder()
						.requireLensFacing(CameraSelector.LENS_FACING_BACK)
						.build();

				preview.setSurfaceProvider(previewView.getSurfaceProvider());

				ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();

				imageAnalysis.setAnalyzer(cameraExecutor, (ImageAnalysis.Analyzer) image -> {
					int rotationDegrees = image.getImageInfo().getRotationDegrees();
					// insert your code here.
				});

				try {
					cameraProvider.unbindAll();

					cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
				} catch (Exception e) {
					LOGGER.e("Use case binding failed: %s", e);
				}
			} catch (ExecutionException | InterruptedException e) {
				LOGGER.e(e.toString());
			}
		}, ContextCompat.getMainExecutor(requireContext()));
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
			permissionsGranted = ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED;

		return permissionsGranted;
	}

}

