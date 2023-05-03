package org.openbot.googleServices;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openbot.databinding.FragmentProjectsBinding;
import org.openbot.projects.DriveProjectsAdapter;
import org.openbot.projects.GoogleSignInCallback;
import org.openbot.projects.ProjectsFragment;

/** google signIn and google drive files management service class */
public class GoogleServices {

  // Set up logging tag for debugging purposes
  private static final String TAG = "GoogleServices";
  private final Activity mActivity;
  private final Context mContext;
  private final GoogleSignInCallback mCallback;
  public final GoogleSignInClient mGoogleSignInClient;
  private ProjectsFragment projectsFragment;
  private FirebaseAuth firebaseAuth;
  private List<File> driveFiles = new ArrayList<>();

  /**
   * Constructor for the GoogleServices class
   *
   * @param activity
   * @param context
   * @param callback
   */
  public GoogleServices(Activity activity, Context context, GoogleSignInCallback callback) {
    // Set instance variables
    mActivity = activity;
    mContext = context;
    mCallback = callback;
    projectsFragment = new ProjectsFragment();
    firebaseAuth = FirebaseAuth.getInstance();
    // Set up Google Sign-In options
    GoogleSignInOptions gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(
                "955078484078-kbadvp54dljcpk0g0j5bnf5c17q63ir2.apps.googleusercontent.com")
            .requestProfile()
            .build();
    // Set up Shared Preferences
    // Set up Google Sign-In client
    mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso);
    // Check if there is a signed-in account already and notify the callback accordingly
    FirebaseUser user = firebaseAuth.getCurrentUser();
    if (user != null) {
      mCallback.onSignInSuccess(user);
    } else {
      mCallback.onSignInFailed(null);
    }
  }

  /**
   * login with firebase using google signIn credential.
   *
   * @param credential use for firebase login.
   */
  private void firebaseAuthWithGoogle(AuthCredential credential) {
    firebaseAuth
        .signInWithCredential(credential)
        .addOnCompleteListener(
            mActivity,
            task -> {
              Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
              if (task.isSuccessful()) {
                Log.d(TAG, "onComplete: successful");
                // Get the signed-in account and Notify callback of successful sign-in.
                mCallback.onSignInSuccess(task.getResult().getUser());
              } else {
                Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                task.getException().printStackTrace();
                // The ApiException status code indicates the detailed failure reason and Notify
                // callback of
                // sign-in failure.
                Log.e(TAG, "handleSignInResult:failed", task.getException());
                mCallback.onSignInFailed(task.getException());
              }
            });
  }

  /** Method to sign out of the current Google account. */
  public void signOut() {
    mGoogleSignInClient
        .signOut()
        .addOnCompleteListener(
            mActivity,
            task -> {
              // Handle sign-out result.
              if (task.isSuccessful()) {
                // firebase user sign-out.
                firebaseAuth.signOut();
                // Notify callback of successful sign-out.
                mCallback.onSignOutSuccess();
                Log.d(TAG, "signOut:success");
              } else {
                // Notify callback of sign-out failure
                mCallback.onSignOutFailed(task.getException());
                Log.e(TAG, "signOut:failed", task.getException());
              }
            });
  }

  /**
   * Method to handle the result of a Google Sign-In attempt
   *
   * @param completedTask
   */
  public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      // Get the signed-in account and Notify callback of successful sign-in.
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);
      AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
      // Set credential for firebase authentication.
      firebaseAuthWithGoogle(credential);
      Log.d(TAG, "handleSignInResult:success - " + account.getEmail());
    } catch (ApiException e) {
      // The ApiException status code indicates the detailed failure reason and Notify callback of
      // sign-in failure.
      Log.e(TAG, "handleSignInResult:failed", e);
      mCallback.onSignInFailed(e);
    }
  }

  /**
   * Helper method to get the Drive API service
   *
   * @return
   */
  private Drive getDriveService() {
    GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(mContext);
    if (googleAccount != null) {
      // Set up Google Account Credential.
      GoogleAccountCredential credential =
          GoogleAccountCredential.usingOAuth2(
              mContext, Collections.singletonList(DriveScopes.DRIVE_FILE));
      credential.setSelectedAccount(googleAccount.getAccount());
      // Build and return the Drive API service.
      return new Drive.Builder(
              new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
          .setApplicationName("OpenBot")
          .build();
    }
    return null;
  }

  /**
   * Retrieves a list of Google Drive files that are not trashed and have the file extension ".js".
   *
   * @param binding
   * @param adapter
   * @param binding
   */
  public void accessDriveFiles(DriveProjectsAdapter adapter, FragmentProjectsBinding binding) {
    // get a Google Drive service instance.
    Drive googleDriveService = getDriveService();
    if (googleDriveService != null) {
      // create a new thread to perform the network call in the background.
      new Thread(
              () -> {
                String pageToken = null;
                do {
                  try {
                    // clear the existing list of drive files.
                    driveFiles.clear();
                    // query for files on Google Drive that are not in the trash folder and have a
                    // ".js" extension.
                    FileList result =
                        googleDriveService
                            .files()
                            .list()
                            .setSpaces("drive")
                            .setFields("nextPageToken, files(id, name, createdTime, modifiedTime)")
                            .setPageToken(pageToken)
                            .setQ("trashed = false")
                            .execute();
                    List<File> files = result.getFiles();
                    // add any file with a ".js" extension to the list of drive files.
                    for (File file : files) {
                      if (file.getName().endsWith(".js")) {
                        //                        File downloadDriveProjects =
                        // googleDriveService.files().get(file.getId()).execute();
                        driveFiles.add(file);
                      }
                    }
                    // update the UI on the main thread to reflect the changes in the list of drive
                    // files.
                    mActivity.runOnUiThread(
                        () -> {
                          adapter.notifyDataSetChanged();
                          projectsFragment.updateMessage(driveFiles, binding);
                        });
                    // update page token to get the next set of files if available.
                    pageToken = result.getNextPageToken();
                  } catch (IOException e) {
                    // log any errors that occur and set page token to null to exit the loop.
                    e.printStackTrace();
                    pageToken = null;
                  }
                  // continue querying for files while there is a valid page token.
                } while (pageToken != null);
              })
          .start();
    }
  }

  /**
   * Returns the list of files retrieved from Google Drive by the accessDriveFiles method.
   *
   * @return The list of project files
   */
  public List<File> getDriveFiles() {
    return driveFiles;
  }

  /**
   * Downloads a file from Google Drive with the given file ID.
   *
   * @param fileId the ID of the file to download
   */
  private void downloadFileFromGDrive(String fileId) {
    Drive googleDriveService = getDriveService();
    if (googleDriveService != null) {
      new Thread(
              () -> {
                try {
                  // Get the Google Drive file with the given ID
                  File gDriveFile = googleDriveService.files().get(fileId).execute();

                  Log.i("Google Drive", String.valueOf(gDriveFile));
                } catch (IOException e) {
                  e.printStackTrace();
                }
              })
          .start();
    } else {
      Log.e("Google Drive", "SignIn error - not logged in");
    }
  }

  /**
   * Renames a file with the given ID to the given new title.
   *
   * @param fileId the ID of the file to rename
   * @param newTitle the new title to give the file
   */
  public void renameFile(String fileId, String newTitle) {
    Drive googleDriveService = getDriveService();
    if (googleDriveService != null) {
      try {
        // Create a new File object with the given new title.
        File file = new File();
        file.setName(newTitle);
        // Update the file with the new title.
        googleDriveService.files().update(fileId, file).execute();
        Log.d("Google Drive File", "File renamed successfully");
      } catch (IOException e) {
        // log any errors that occur when renaming the file.
        e.printStackTrace();
        Log.e("Google Drive File", "Error renaming file");
      }
    }
  }

  /**
   * Deletes a file with the given ID from Google Drive.
   *
   * @param fileId fileId the ID of the file to delete.
   */
  public void deleteFile(String fileId) {
    Drive googleDriveService = getDriveService();
    if (googleDriveService != null) {
      try {
        // Delete the file with the given ID
        googleDriveService.files().delete(fileId).execute();
        Log.d("Google Drive File", "File deleted successfully");
      } catch (IOException e) {
        // log any errors that occur when deleting the file.
        e.printStackTrace();
        Log.e("Google Drive File", "Error deleting file");
      }
    }
  }
}
