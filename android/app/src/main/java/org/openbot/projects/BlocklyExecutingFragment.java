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
    binding = FragmentBlocklyExecutingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    myWebView = new WebView(getContext());
    myWebView.getSettings().setJavaScriptEnabled(true);
  }

  public void runJSCommand(String finalCode) {
    Activity activity = getActivity();
    if (activity != null) {
      activity.runOnUiThread(
          () -> {
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
  public void onResume() {
    super.onResume();
    if (barCodeScannerFragment.finalCode != null) {
      runJSCommand(barCodeScannerFragment.finalCode);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (previousSpeedMultiplier != 0) {
      vehicle.setSpeedMultiplier(previousSpeedMultiplier);
    }
  }
}
