package org.openbot.convertJStoJAVA;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.openbot.R;
import org.openbot.databinding.FragmentConvertJsToJavaBinding;

import java.io.IOException;

import timber.log.Timber;

public class convertJStoJAVAFragment extends Fragment {
    private FragmentConvertJsToJavaBinding binding;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private WebView myWebView;
    private String barcodeData;
    private TextView barcodeText;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConvertJsToJavaBinding.inflate(inflater, container, false);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myWebView = new WebView(getContext());
        myWebView.getSettings().setJavaScriptEnabled(true);
        surfaceView = getView().findViewById(R.id.surface_view);
        barcodeText = getView().findViewById(R.id.barcode_text);
        initialiseDetectorsAndSources();


    }


    private void onClick() {


        String code = "function start(){for(var count = 0; count < 20; count++){moveForward(10);}}function forever(){moveCircular(20);}start();forever();";
        String[] botFunctionArray = {"blinkLeft", "blinkRight", "moveCircular", "moveForward"};
        for (String fun : botFunctionArray) {
            if (code.contains(fun)) {
                code = code.replace(fun, "Android." + fun);
            }
        }
//                System.out.println("code is :" + code);

        myWebView.addJavascriptInterface(new BotFunction(), "Android");
        myWebView.evaluateJavascript(code, null);
    }


    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {



                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                            }
                            barcodeText.setText(barcodeData);
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            Timber.tag("Qr").d(barcodeData);

                        }
                    });

                }
            }
        });
    }

}
