package org.openbot.playground;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    return binding.getRoot();
  }

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    playgroundView = (WebView) binding.playgroundWebView;
    WebSettings webSettings = playgroundView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setUserAgentString(
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
    playgroundView.setWebViewClient(new WebViewClient());
    playgroundView.loadUrl("https://www.openbot.itinker.io/");
  }
}
