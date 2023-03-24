package org.openbot.projects;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import org.openbot.R;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentProjectsBinding;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

public class ProjectsFragment extends ControlsFragment {
    private FragmentProjectsBinding binding;
    private BarCodeScannerFragment barCodeScannerFragment;
    private WebView myWebView;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mGoogleSignInAccount;

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
//            downloadFileFromGDrive("1-vTtRQ1iyrrYrnTASLP3B2CH5R-h46Rp");
//            accessDriveFiles();
            Timber.tag("firebase").i("Name = %s", account.getDisplayName());
            Timber.tag("firebase").i("Token = %s", account.getIdToken());
        }
    }

    private Drive getDriveService(GoogleSignInAccount mGoogleSignInAccount, ProjectsFragment projectsFragment) {
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (googleAccount != null) {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    getContext(), Collections.singletonList(DriveScopes.DRIVE_FILE)
            );
            credential.setSelectedAccount(googleAccount.getAccount());
            return new Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
            )
                    .setApplicationName(getString(R.string.app_name))
                    .build();
        }
        return null;
    }

    private void accessDriveFiles() {
        Drive googleDriveService = getDriveService(mGoogleSignInAccount, this);
        if (googleDriveService != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String pageToken = null;
                    do {
                        try {
                            FileList result = googleDriveService.files().list()
                                    .setSpaces("drive")
                                    .setFields("nextPageToken, files(id, name)")
                                    .setPageToken(pageToken)
                                    .execute();
                            List<File> files = result.getFiles();
                            for (File file : files) {
                                Log.d("Google Drive File", "name=" + file.getName() + " id=" + file.getId());

                            }
                            pageToken = result.getNextPageToken();
                        } catch (IOException e) {
                            e.printStackTrace();
                            pageToken = null;
                        }
                    } while (pageToken != null);
                }
            }).start();
        }
    }

    private void downloadFileFromGDrive(String id) {

        Drive googleDriveService = getDriveService(mGoogleSignInAccount, this);
        if (googleDriveService != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File gDriveFile = googleDriveService.files().get(id).execute();

                        Log.i("Google Drive", String.valueOf(gDriveFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Log.e("Google Drive", "Signin error - not logged in");
        }
    }

    private void readFileFromDrive(String fileId) {
        DriveServiceHelper driveServiceHelper = new DriveServiceHelper(getDriveService(mGoogleSignInAccount, this));
        driveServiceHelper.readFile(fileId)
                .addOnSuccessListener(fileContents -> {
                    System.out.println(fileContents);
                    // Do something with the contents of the file
                    String code = fileContents;
//                    String code = "function start(){moveForward(10);stop();moveCircular(20);}start();";
                    String[] botFunctionArray = {"moveCircular", "moveForward"};

                    for (String fun : botFunctionArray) {
                        if (code.contains(fun)) {
                            code = code.replace(fun, "Android." + fun);
                        }
                    }

                    myWebView.addJavascriptInterface(new BotFunctions(vehicle), "Android");
                    myWebView.evaluateJavascript(code, null);
                })
                .addOnFailureListener(exception -> {
                    Log.e("TAG", "Unable to read file from Drive", exception);
                });
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
        String fileUrl = barCodeScannerFragment.barCodeValue;
        Timber.tag("TAG").d("callToBotFunction url: %s", fileUrl);

        if (fileUrl.contains("/file/d/")) {
            // Extract the ID from the URL string
            String id = fileUrl.substring(fileUrl.indexOf("/file/d/") + 8, fileUrl.indexOf("/edit"));
            readFileFromDrive(id);
        }

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
