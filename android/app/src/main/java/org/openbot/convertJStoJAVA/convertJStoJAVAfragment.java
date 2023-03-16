package org.openbot.convertJStoJAVA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.openbot.R;
import org.openbot.databinding.FragmentConvertJsToJavaBinding;

import timber.log.Timber;

public class convertJStoJAVAFragment extends Fragment {
    private FragmentConvertJsToJavaBinding binding;
    private BarCodeScannerFragment barCodeScannerFragment;
    private WebView myWebView;

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
        Button button = getView().findViewById(R.id.btnScan);
        barCodeScannerFragment = new BarCodeScannerFragment();

        button.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.barCodeScannerFragment));
        myWebView = new WebView(getContext());
        myWebView.getSettings().setJavaScriptEnabled(true);
    }


    private void callToBotFunction() {
        String code = barCodeScannerFragment.barCodeValue;

//        String code = "function start(){moveForward(10);stop();moveCircular(20);}start();";
        String[] botFunctionArray = {"moveCircular", "moveForward"};

        for (String fun : botFunctionArray) {
            if (code.contains(fun)) {
                code = code.replace(fun, "Android." + fun);
            }
        }

        Timber.tag("Qr sanjeev").d(code);
        myWebView.addJavascriptInterface(new BotFunction(), "Android");
        myWebView.evaluateJavascript(code, null);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (barCodeScannerFragment.barCodeValue != null) {
            callToBotFunction();
        }
    }


}
