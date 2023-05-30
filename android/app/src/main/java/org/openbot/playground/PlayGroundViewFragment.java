package org.openbot.playground;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.openbot.databinding.FragmentPlaygroundBinding;

public class PlayGroundViewFragment extends Fragment {
  private FragmentPlaygroundBinding binding;
  private WebView playgroundView;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentPlaygroundBinding.inflate(inflater, container, false);
    playgroundView = (WebView) binding.playgroundWebView;
    playgroundView.setWebViewClient(new WebViewClient());
    playgroundView.setWebChromeClient(new WebChromeClient(){
      @Override
      public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d("WebViewConsole", consoleMessage.message() + ", "
                + consoleMessage.sourceId());
        return true;
      }
    });
    return binding.getRoot();
  }

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    WebSettings webSettings = playgroundView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setUserAgentString(
        "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36");
    playgroundView.loadUrl("https://www.openbot.itinker.io");
  }
}
