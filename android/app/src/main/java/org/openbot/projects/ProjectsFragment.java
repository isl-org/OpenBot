package org.openbot.projects;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

import org.openbot.R;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentProjectsBinding;
import org.openbot.googleServices.GoogleServices;

import java.io.IOException;

public class ProjectsFragment extends ControlsFragment {
    private FragmentProjectsBinding binding;
    private BarCodeScannerFragment barCodeScannerFragment;
    private WebView myWebView;
    private SignInButton signInButton;
    private Button signOutButton;
    private Button runOpenBot;
    private TextView userName;
    private TextView loadingText;
    private GoogleServices googleServices;
    private int previousSpeedMultiplier;

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
        signInButton = requireView().findViewById(R.id.sign_in_button);
        signOutButton = requireView().findViewById(R.id.sign_out_button);
        userName = requireView().findViewById(R.id.user_name);
        loadingText = requireView().findViewById(R.id.wait_for_cmd);
        runOpenBot = requireView().findViewById(R.id.openbot_run_command);
        barCodeScannerFragment = new BarCodeScannerFragment();
        googleServices = new GoogleServices(requireActivity(), requireContext(), newGoogleServices);
        myWebView = new WebView(getContext());
        myWebView.getSettings().setJavaScriptEnabled(true);
        requireView().findViewById(R.id.btnScan)
                .setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.barCodeScannerFragment));
        signInButton.setOnClickListener(v -> signIn());

        signOutButton.setOnClickListener(v -> googleServices.signOut());

        runOpenBot.setOnClickListener(v -> {
            if (barCodeScannerFragment.barCodeValue != null) {
                getFileID(barCodeScannerFragment.barCodeValue);
            }
        });
    }

    private void readFileFromDrive(String fileId) throws IOException {
        new ReadFileTask(fileId, new ReadFileCallback() {
            @Override
            public void onFileRead(String fileContents) {
                String code = fileContents;
                String[] botFunctionArray = {
                        "moveForward",
                        "moveBackward",
                        "moveLeft",
                        "moveRight",
                        "moveOpenBot",
                        //"moveCircular",
                        "pause",
                        "stopRobot",

                        "sonarReading",
                        "speedReading",
                        "voltageDividerReading",
                        "frontWheelReading",
                        "backWheelReading",
                        "gyroscopeReading",
                        "accelerationReading",
                        "magneticReading",
                        "indicatorReading",

                        "noiseEnable",
                        "playSoundSpeed",
                        "rightIndicatorOn",
                        "leftIndicatorOn",
                        "IndicatorOff",
                };
                for (String fun : botFunctionArray) {
                    if (code.contains(fun)) {
                        code = code.replace(fun, "Android." + fun);
                    }
                }
                String finalCode = code;
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            previousSpeedMultiplier = vehicle.getSpeedMultiplier();
                            vehicle.setSpeedMultiplier(255);
                            loadingText.setVisibility(View.GONE);
                            runOpenBot.setVisibility(View.VISIBLE);
                            myWebView.addJavascriptInterface(new BotFunctions(vehicle, audioPlayer), "Android");
                            myWebView.evaluateJavascript(finalCode, null);
                        }
                    });
                }
            }
        }).execute();
    }

    private void signIn() {
        Intent signInIntent = googleServices.mGoogleSignInClient.getSignInIntent();
        googleLogInActivityResultLauncher.launch(signInIntent);
    }

    ActivityResultLauncher<Intent> googleLogInActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                googleServices.handleSignInResult(task);
            });

    private GoogleSignInCallback newGoogleServices = new GoogleSignInCallback() {
        @Override
        public void onSignInSuccess(GoogleSignInAccount account) {
            userName.setText(account.getDisplayName());
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSignInFailed(Exception exception) {
            userName.setText("Unknown");
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }

        @Override
        public void onSignOutSuccess() {
            userName.setText("Unknown");
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }

        @Override
        public void onSignOutFailed(Exception exception) {

        }
    };

    private void getFileID(String urlLink) {
        if (urlLink.contains("/file/d/")) {
            // Extract the ID from the URL string
            String id = urlLink.substring(urlLink.indexOf("/file/d/") + 8, urlLink.indexOf("/edit"));
            try {
                loadingText.setVisibility(View.VISIBLE);
                runOpenBot.setVisibility(View.GONE);
                readFileFromDrive(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingText.setVisibility(View.GONE);
        if (barCodeScannerFragment.barCodeValue != null) {
            runOpenBot.setVisibility(View.VISIBLE);
        } else {
            runOpenBot.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (previousSpeedMultiplier != 0) {
            vehicle.setSpeedMultiplier(previousSpeedMultiplier);
        }
    }

    @Override
    protected void processControllerKeyData(String command) {
    }

    @Override
    protected void processUSBData(String data) {
    }
}
