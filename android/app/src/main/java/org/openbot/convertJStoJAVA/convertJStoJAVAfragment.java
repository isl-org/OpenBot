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
//                qrScanResult();
            }
        });

    }

    public String qrScanResult(){
        String cmdObject = "let cmdObject = []; function blinkLeft() {cmdObject.push({\"INDICATOR_LEFT\" : \"true\"});}";
        String returncmdObject = "return JSON.stringify(cmdObject)";
        String code = "function openCode() { var name = 1; while (name <= 5){ blinkLeft(); name = name + 1; }}";
        String newString = new String();

        for (int i = 0; i < code.length(); i++) {
            // Insert the original string character
            // into the new string
            newString += code.charAt(i);
            if (i == 20) {
                // Insert the string to be inserted
                // into the new string
                newString += cmdObject;
            }
            if (i == code.length()-2){
                newString += returncmdObject;
            }
        }
//        System.out.println("value = "+newString);
        return newString;
    }

    private void evaluate() {
        JsEvaluator jsEvaluator = new JsEvaluator(this.getActivity());
        System.out.println("value = 1 " +qrScanResult());

//        jsEvaluator.evaluate("2 * 20", new JsCallback() {
//            @Override
//            public void onResult(String result) {
//                System.out.println("result = " + result);
//                // Process result here.
//                // This method is called in the UI thread.
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                System.out.println("errorMessage = " + errorMessage);
//
//                // Process JavaScript error here.
//                // This method is called in the UI thread.
//            }
//        });


//        jsEvaluator.callFunction("function greet(name) { return \"hello, \" + name; }", new JsCallback() {
//            @Override
//            public void onResult(String value) {
//                System.out.println(value);
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                System.out.println("error = " + errorMessage);
//            }
//        }, "greet", "something");
//
//        jsEvaluator.callFunction("function greet() { var name = 1; while (name <= 3){ console.log(\"sanjeev\"); name = name + 1} return name;}", new JsCallback() {
//            @Override
//            public void onResult(String value) {
//                System.out.println(value);
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                System.out.println("error = " + errorMessage);
//            }
//        }, "greet");
        jsEvaluator.callFunction(qrScanResult(), new JsCallback() {
            @Override
            public void onResult(String value) {

                System.out.println("value = 2 " +value);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("value = e " +errorMessage);


            }
        }, "openCode");
    }

}
