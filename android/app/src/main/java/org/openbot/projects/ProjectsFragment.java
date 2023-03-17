package org.openbot.projects;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import org.openbot.R;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentProjectsBinding;

public class ProjectsFragment extends ControlsFragment {
    private FragmentProjectsBinding binding;
    private BarCodeScannerFragment barCodeScannerFragment;
    private WebView myWebView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = getView().findViewById(R.id.btnScan);
        barCodeScannerFragment = new BarCodeScannerFragment();
        imageView.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.barCodeScannerFragment));
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

        myWebView.addJavascriptInterface(new BotFunctions(vehicle), "Android");
        myWebView.evaluateJavascript(code, null);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (barCodeScannerFragment.barCodeValue != null) {
            callToBotFunction();
        }
    }

    @Override
    protected void processControllerKeyData(String command) {
    }

    @Override
    protected void processUSBData(String data) {
    }

}
