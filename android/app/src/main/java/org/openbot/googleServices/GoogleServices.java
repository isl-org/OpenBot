package org.openbot.googleServices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import org.openbot.R;
import org.openbot.projects.GoogleSignInCallback;
import org.openbot.projects.ProjectsFragment;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GoogleServices extends ProjectsFragment {
    private static final String TAG = "GoogleServices";
    private final Activity mActivity;
    private final Context mContext;
    private final GoogleSignInCallback mCallback;
    public final GoogleSignInClient mGoogleSignInClient;

    public GoogleServices(Activity activity, Context context, GoogleSignInCallback callback) {
        mActivity = activity;
        mContext = context;
        mCallback = callback;

        // Configure Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context);
        if (googleSignInAccount != null) {
            mCallback.onSignInSuccess(googleSignInAccount);
        } else {
            mCallback.onSignInFailed(null);
        }
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(signInIntent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            });

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(mActivity, task -> {
                    // Handle sign-out result
                    if (task.isSuccessful()) {
                        mCallback.onSignOutSuccess();
                        Log.d(TAG, "signOut:success");
                    } else {
                        mCallback.onSignOutFailed(task.getException());
                        Log.e(TAG, "signOut:failed", task.getException());
                    }
                });
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Log.d(TAG, "handleSignInResult:success - " + account.getEmail());
            mCallback.onSignInSuccess(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.e(TAG, "handleSignInResult:failed", e);
            mCallback.onSignInFailed(e);
        }
    }

    private Drive getDriveService() {
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
        Drive googleDriveService = getDriveService();
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

        Drive googleDriveService = getDriveService();
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
            Log.e("Google Drive", "SignIn error - not logged in");
        }
    }
}
