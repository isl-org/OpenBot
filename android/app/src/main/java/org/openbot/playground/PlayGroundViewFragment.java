package org.openbot.playground;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

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
        playgroundView.setWebViewClient(new MyWebViewClient(container));
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
        playgroundView.loadUrl("https://www.openbot.itinker.io/");
        playgroundView.evaluateJavascript("", value -> System.out.println("sanjeev value = " + value));
    }

    private class MyWebViewClient extends WebViewClient {

        private WebView newWebViewPopupWindow;
        private ViewGroup viewGroup;

        public MyWebViewClient(ViewGroup viewGroup) {
            this.viewGroup = viewGroup;
        }

         String JAVASCRIPT_LOCAL_STORAGE_LOOKUP = "javascript:window.localStorage.getItem('theme');";

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            newWebViewPopupWindow = new WebView(view.getContext());
            newWebViewPopupWindow.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            newWebViewPopupWindow.setBackgroundColor(0x00000000); // Set background color to transparent
            newWebViewPopupWindow.setWebChromeClient(new WebChromeClient());
            newWebViewPopupWindow.setWebViewClient(this);

            viewGroup.addView(newWebViewPopupWindow);

            newWebViewPopupWindow.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            System.out.println("finished == " + url);
            view.evaluateJavascript(JAVASCRIPT_LOCAL_STORAGE_LOOKUP, value -> {
                System.out.println("hello "+ value);
            });
        }
    }
}
