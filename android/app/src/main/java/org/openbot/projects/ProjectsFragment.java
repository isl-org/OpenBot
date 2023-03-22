package org.openbot.projects;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.openbot.R;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentProjectsBinding;

import timber.log.Timber;

public class ProjectsFragment extends ControlsFragment {
    private FragmentProjectsBinding binding;
    private BarCodeScannerFragment barCodeScannerFragment;
    private WebView myWebView;
    private GoogleSignInClient mGoogleSignInClient;

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

        SignInButton signInButton = getView().findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        getView().findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        // Check for existing Google Sign In account, if the user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            Timber.tag("firebase").i("Name = %s", account.getDisplayName());
            Timber.tag("firebase").i("Token = %s", account.getIdToken());
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(signInIntent);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Timber.tag("firebase").i("sign-out%s", task.getResult());
                    }
                });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                }
            });

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Timber.tag("firebase").i("signInResult%s", account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.tag("firebase").w("signInResult:failed code=%s", e.getStatusCode());
        }
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
