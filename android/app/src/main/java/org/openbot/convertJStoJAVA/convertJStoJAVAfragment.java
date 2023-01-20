package org.openbot.convertJStoJAVA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

import org.openbot.R;
import org.openbot.databinding.FragmentConvertJsToJavaBinding;

public class convertJStoJAVAfragment extends Fragment {
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });

    }

    private void evaluate() {
        JsEvaluator jsEvaluator = new JsEvaluator(this.getActivity());

        System.out.println("FATAL = " + jsEvaluator);
        jsEvaluator.evaluate("2 * 20", new JsCallback() {
            @Override
            public void onResult(String result) {
                System.out.println("result = " + result);
                // Process result here.
                // This method is called in the UI thread.
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("errorMessage = " + errorMessage);

                // Process JavaScript error here.
                // This method is called in the UI thread.
            }
        });

        jsEvaluator.callFunction("function greet(name) { return \"ajhdcdsjhc, \" + name; }", new JsCallback() {
            @Override
            public void onResult(String value) {
                System.out.println(value);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        }, "name", 1);
    }

}
