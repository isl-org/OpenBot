package org.openbot.convertJStoJAVA;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.openbot.R;
import org.openbot.databinding.FragmentConvertJsToJavaBinding;

public class convertJStoJAVAFragment extends Fragment {
    private FragmentConvertJsToJavaBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConvertJsToJavaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = getView().findViewById(R.id.getResult);
        WebView myWebView = (WebView) getView().findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = "function openCode() { var loopV = 0; while (loopV <= 9){ Android.blinkLeft(); loopV = loopV + 1; console.log(loopV);}}openCode()";
                myWebView.addJavascriptInterface(new BotFunction(), "Android");
                myWebView.evaluateJavascript(code, null);
            }
        });

    }

}
