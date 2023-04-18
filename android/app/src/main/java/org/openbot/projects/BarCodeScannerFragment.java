package org.openbot.projects;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageProxy;
import androidx.navigation.Navigation;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.io.IOException;
import org.openbot.R;
import org.openbot.common.CameraFragment;
import org.openbot.databinding.FragmentBarCodeScannerBinding;
import org.openbot.utils.BotFunctionUtils;

public class BarCodeScannerFragment extends CameraFragment {

  private FragmentBarCodeScannerBinding binding;
  private BarcodeDetector barcodeDetector;
  private BottomSheetBehavior successBottomSheetBehavior;
  private BottomSheetBehavior failedBottomSheetBehavior;
  private boolean barCodeAccess = true;
  public static String finalCode;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for Barcode Scanner View fragment.
    binding = FragmentBarCodeScannerBinding.inflate(inflater, container, false);

    // initialise 2 bottom-sheet one is for correct QR code another one is for wrong QR code.
    successBottomSheetBehavior = BottomSheetBehavior.from(binding.qrBottomSheetSuccess);
    failedBottomSheetBehavior = BottomSheetBehavior.from(binding.qrBottomSheetError);
    successBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    failedBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    return inflateFragment(binding, inflater, container);
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ImageView crossImageView = requireView().findViewById(R.id.btnCross);
    crossImageView.setOnClickListener(
        v -> Navigation.findNavController(requireView()).popBackStack());
    binding.qrCancelBtn.setOnClickListener(v -> onQRCancelButton());
    binding.qrStartBtn.setOnClickListener(v -> onQRStartButton());
    binding.reScanBtn.setOnClickListener(v -> onQRReScanButton());
    successBottomSheetBehavior.addBottomSheetCallback(successBottomSheetCallback);
    failedBottomSheetBehavior.addBottomSheetCallback(failedBottomSheetCallback);
    initialiseBarcodeDetector();
  }

  @Override
  protected void processControllerKeyData(String command) {}

  @Override
  protected void processUSBData(String data) {}

  @Override
  protected void processFrame(Bitmap image, ImageProxy imageProxy) {
    startBarcodeDetection(image);
  }

  private void initialiseBarcodeDetector() {
    barcodeDetector =
        new BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build();
  }

  private void startBarcodeDetection(Bitmap image) {
    Frame frame = null;
    if (image != null) {
      frame = new Frame.Builder().setBitmap(image).build();
    }
    SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);
    if (barcodes.size() != 0 && barCodeAccess) {
      barCodeAccess = false;
      String barCodeValue = barcodes.valueAt(0).displayValue;
      getFileID(barCodeValue);
    }
  }

  private BottomSheetBehavior.BottomSheetCallback successBottomSheetCallback =
      new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
          // Handle state changes here of bottomSheet.
          if (newState == BottomSheetBehavior.STATE_EXPANDED
              || newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            binding.overlayView.setVisibility(View.VISIBLE);
          } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            binding.overlayView.setVisibility(View.GONE);
            barCodeAccess = true;
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
            binding.overlayView.setVisibility(View.VISIBLE);
          } else {
            binding.overlayView.setVisibility(View.GONE);
            barCodeAccess = true;
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
