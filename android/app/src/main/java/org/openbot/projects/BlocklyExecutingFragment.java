package org.openbot.projects;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentBlocklyExecutingBinding;
import org.openbot.env.SharedPreferencesManager;

public class BlocklyExecutingFragment extends ControlsFragment {

  private FragmentBlocklyExecutingBinding binding;
  private WebView myWebView;
  private int previousSpeedMultiplier;
  private BarCodeScannerFragment barCodeScannerFragment;
  private SharedPreferencesManager sharedPreferencesManager;
  private boolean isRunJSCommand = false;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    barCodeScannerFragment = new BarCodeScannerFragment();
    sharedPreferencesManager = new SharedPreferencesManager(requireContext());
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
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding.stopCarBtn.setOnClickListener(
        v -> {
          myWebView.destroy();
          binding.jsCommand.setText("Cancelled");
          vehicle.setControl(0, 0);
          vehicle.setIndicator(0);
        });
    // if string js code variable is not null execute js code when you navigate on this fragment.
    if (barCodeScannerFragment.finalCode != null && isRunJSCommand) {
      runJSCommand(barCodeScannerFragment.finalCode);
    }
  }

  private void showAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
    builder.setTitle("Rotation Detected").setMessage("Do you want to restart Blockly command?");
    builder.setPositiveButton("Yes", (dialog, id) -> restartJSCommand());
    builder.setNegativeButton("Cancel", (dialog, id) -> requireActivity().onBackPressed());
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void restartJSCommand() {
    // initialise web view to execute javascript block codes.
    myWebView = new WebView(getContext());
    // enable JavaScript in the web-view.
    myWebView.getSettings().setJavaScriptEnabled(true);
    if (myWebView != null && barCodeScannerFragment.finalCode != null) {
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
            // store previous speed multiplier value.
            previousSpeedMultiplier = vehicle.getSpeedMultiplier();
            // set the speed multiplier to maximum value (255) because openBot moving according to
            vehicle.setSpeedMultiplier(255);
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
    vehicle.setControl(0, 0);
    // if previous speed multiplier value is not 0, set the speed multiplier back to its previous
    // value when you go back from this screen.
    if (previousSpeedMultiplier != 0) {
      vehicle.setSpeedMultiplier(previousSpeedMultiplier);
    }
  }
}
