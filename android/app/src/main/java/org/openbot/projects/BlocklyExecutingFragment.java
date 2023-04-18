package org.openbot.projects;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentBlocklyExecutingBinding;

public class BlocklyExecutingFragment extends ControlsFragment {

  private FragmentBlocklyExecutingBinding binding;
  private WebView myWebView;
  private int previousSpeedMultiplier;
  private BarCodeScannerFragment barCodeScannerFragment;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    barCodeScannerFragment = new BarCodeScannerFragment();
    // Inflate the layout for Blocks code executing Fragment.
    binding = FragmentBlocklyExecutingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // initialise web view to execute javascript block codes.
    myWebView = new WebView(getContext());
    myWebView.getSettings().setJavaScriptEnabled(true);

    // execute js code when you navigate on block codes executing screen and string js code variable
    // is not null.
    if (barCodeScannerFragment.finalCode != null) {
      runJSCommand(barCodeScannerFragment.finalCode);
    }
  }

  /**
   * get javascript code in string from googleDrive file and run execute in webView.
   *
   * @param finalCode
   */
  private void runJSCommand(String finalCode) {
    Activity activity = getActivity();
    if (activity != null) {
      activity.runOnUiThread(
          () -> {
            // set speed multiplier at maximum because openBot moving according to speed set in
            // block codes.
            // save default speed multiplier in variable to set again in initial state.
            previousSpeedMultiplier = vehicle.getSpeedMultiplier();
            vehicle.setSpeedMultiplier(255);
            myWebView.addJavascriptInterface(new BotFunctions(vehicle, audioPlayer), "Android");
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
    // set default speed multiplier when you go back from this screen.
    if (previousSpeedMultiplier != 0) {
      vehicle.setSpeedMultiplier(previousSpeedMultiplier);
    }
  }
}
