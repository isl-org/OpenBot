package org.openbot.projects;

import android.Manifest;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.io.IOException;
import org.openbot.R;
import org.openbot.databinding.FragmentBarCodeScannerBinding;
import org.openbot.utils.BotFunctionUtils;

public class BarCodeScannerFragment extends Fragment {

  private SurfaceView surfaceView;
  private View overlayView;
  private BarcodeDetector barcodeDetector;
  private CameraSource cameraSource;
  private BottomSheetBehavior successBottomSheetBehavior;
  private BottomSheetBehavior failedBottomSheetBehavior;
  private boolean barCodeAccess = true;
  public static String finalCode;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for Barcode Scanner View fragment.
    FragmentBarCodeScannerBinding binding =
        FragmentBarCodeScannerBinding.inflate(inflater, container, false);

    // initialise 2 bottom-sheet one is for correct QR code another one is for wrong QR code.
    ConstraintLayout successQrBottomSheet =
        binding.getRoot().findViewById(R.id.qr_bottom_sheet_success);
    ConstraintLayout errorQrBottomSheet =
        binding.getRoot().findViewById(R.id.qr_bottom_sheet_error);
    successBottomSheetBehavior = BottomSheetBehavior.from(successQrBottomSheet);
    failedBottomSheetBehavior = BottomSheetBehavior.from(errorQrBottomSheet);
    successBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    failedBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    surfaceView = requireView().findViewById(R.id.surface_view);
    ImageView crossImageView = requireView().findViewById(R.id.btnCross);
    LinearLayout qrCancelBtn = requireView().findViewById(R.id.qr_cancel_btn);
    LinearLayout qrStartBtn = requireView().findViewById(R.id.qr_start_btn);
    LinearLayout qrReScanBtn = requireView().findViewById(R.id.re_scan_btn);
    overlayView = requireView().findViewById(R.id.overlay_view);
      startScan();
    crossImageView.setOnClickListener(
        v -> Navigation.findNavController(requireView()).popBackStack());
    qrCancelBtn.setOnClickListener(v -> onQRCancelButton());
    qrStartBtn.setOnClickListener(v -> onQRStartButton());
    qrReScanBtn.setOnClickListener(v -> onQRReScanButton());
    successBottomSheetBehavior.addBottomSheetCallback(successBottomSheetCallback);
    failedBottomSheetBehavior.addBottomSheetCallback(failedBottomSheetCallback);
  }

  private void startScan() {
    barcodeDetector =
        new BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build();

    cameraSource =
        new CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(2340, 1080)
            .setAutoFocusEnabled(true)
            .build();

    surfaceView
        .getHolder()
        .addCallback(
            new SurfaceHolder.Callback() {
              @Override
              public void surfaceCreated(SurfaceHolder holder) {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
              }

              @Override
              public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

              @Override
              public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
              }
            });

    barcodeDetector.setProcessor(
        new Detector.Processor<Barcode>() {
          @Override
          public void release() {}

          @Override
          public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
            final SparseArray<Barcode> barcodes = detections.getDetectedItems();
            if (barcodes.size() != 0 && barCodeAccess) {
              barCodeAccess = false;
              String barCodeValue = barcodes.valueAt(0).displayValue;
              getFileID(barCodeValue);
            }
          }
        });
  }

  ActivityResultLauncher<String> requestPermissionLauncher =
      registerForActivityResult(
          new ActivityResultContracts.RequestPermission(),
          new ActivityResultCallback<>() {
            @Override
            public void onActivityResult(Boolean result) {
              if (result) {
                try {
                  cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              }
            }
          });

  private BottomSheetBehavior.BottomSheetCallback successBottomSheetCallback =
      new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
          // Handle state changes here
          if (newState == BottomSheetBehavior.STATE_EXPANDED
              || newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            overlayView.setVisibility(View.VISIBLE);
          } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            overlayView.setVisibility(View.GONE);
          }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
          // Handle sliding events here
        }
      };

  private BottomSheetBehavior.BottomSheetCallback failedBottomSheetCallback =
      new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
          // Handle state changes here
          if (newState == BottomSheetBehavior.STATE_EXPANDED
              || newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            overlayView.setVisibility(View.VISIBLE);
          } else {
            overlayView.setVisibility(View.GONE);
          }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
          // Handle sliding events here
        }
      };

  private void onQRCancelButton() {
    successBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    barCodeAccess = true;
  }

  private void onQRReScanButton() {
    failedBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    barCodeAccess = true;
  }

  private void onQRStartButton() {
    Navigation.findNavController(requireView())
        .navigate(R.id.action_barCodeScannerFragment_to_blocklyExecutingFragment);
  }

  private void getFileID(String urlLink) {
    if (urlLink.contains("/file/d/")) {
      // Extract the ID from the URL string
      String id = urlLink.substring(urlLink.indexOf("/file/d/") + 8, urlLink.indexOf("/edit"));
      try {
        readFileFromDrive(id);
      } catch (IOException e) {
        handleBottomSheet(false);
        throw new RuntimeException(e);
      }
    } else {
      handleBottomSheet(false);
    }
  }

  private void readFileFromDrive(String fileId) throws IOException {
    new ReadFileTask(
            fileId,
            new ReadFileCallback() {
              @Override
              public void onFileReadSuccess(String fileContents) {
                String code = fileContents;
                for (String fun : BotFunctionUtils.botFunctionArray) {
                  if (code.contains(fun)) {
                    code = code.replace(fun, "Android." + fun);
                  }
                }
                finalCode = code;
                handleBottomSheet(true);
              }

              @Override
              public void onFileReadFailed(IOException e) {
                handleBottomSheet(false);
              }
            })
        .execute();
  }

  private void handleBottomSheet(boolean scanSuccess) {
    if (scanSuccess) {
      successBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    } else {
      failedBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
  }
}
