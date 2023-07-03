package org.openbot.googleServices;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openbot.databinding.FragmentProjectsBinding;
import org.openbot.env.SharedPreferencesManager;
import org.openbot.projects.DriveProjectsAdapter;
import org.openbot.projects.GoogleSignInCallback;
import org.openbot.projects.ProjectsDataInObject;
import org.openbot.projects.ProjectsFragment;
import org.openbot.tflite.Model;
import org.openbot.utils.FileUtils;

import timber.log.Timber;

/** google signIn and google drive files management service class */
public class GoogleServices {

  // Set up logging tag for debugging purposes
  private static final String TAG = "GoogleServices";
  private final Activity mActivity;
  private final Context mContext;
  private final GoogleSignInCallback mCallback;
  public final GoogleSignInClient mGoogleSignInClient;
  private final ProjectsFragment projectsFragment;
  private final FirebaseAuth firebaseAuth;
  public ArrayList<ProjectsDataInObject> projectsList = new ArrayList<>();
  private final SharedPreferencesManager sharedPreferencesManager;

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
    sharedPreferencesManager = new SharedPreferencesManager(mContext);
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
                      Timber.tag(TAG).d("signInWithCredential:onComplete:%s", task.isSuccessful());
                      if (task.isSuccessful()) {
                        Timber.tag(TAG).d("onComplete: successful");
                        // Get the signed-in account and Notify callback of successful sign-in.
                        mCallback.onSignInSuccess(task.getResult().getUser());
                        createAndUploadJsonFile(FileUtils.loadConfigJSONFromAsset(mActivity));
                      } else {
                        Timber.tag(TAG).w("signInWithCredential%s", task.getException().getMessage());
                        task.getException().printStackTrace();
                        // The ApiException status code indicates the detailed failure reason and Notify
                        // callback of
                        // sign-in failure.
                        Timber.tag(TAG).e(task.getException(), "handleSignInResult:failed");
                        mCallback.onSignInFailed(task.getException());
                      }
                    });
  }

  /**
   * Method to sign out of the current Google account.
   */
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
                        Timber.tag(TAG).d("signOut:success");
                      } else {
                        // Notify callback of sign-out failure
                        mCallback.onSignOutFailed(task.getException());
                        Timber.tag(TAG).e(task.getException(), "signOut:failed");
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
      Timber.tag(TAG).d("handleSignInResult:success - %s", account.getEmail());
    } catch (ApiException e) {
      // The ApiException status code indicates the detailed failure reason and Notify callback of
      // sign-in failure.
      Timber.tag(TAG).e(e, "handleSignInResult:failed");
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
                    List<File> driveProjectFiles = result.getFiles();

                    // Create a HashSet to store the drive project IDs.
                    Set<String> driveProjectId = new HashSet<>();

                    // Create a HashSet to store the local project IDs.
                    Set<String> localProjectId = new HashSet<>();
                    for (ProjectsDataInObject obj : projectsList) {
                      localProjectId.add(obj.getProjectId());
                    }
                    // Iterate through the files
                    for (File file : driveProjectFiles) {
                      if (file.getName().endsWith(".js")) {
                        driveProjectId.add(file.getId());
                        String projectName = file.getName();
                        String projectId = file.getId();
                        DateTime projectDate = file.getModifiedTime();

                        // Read the content of the file
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        googleDriveService
                                .files()
                                .get(file.getId())
                                .executeMediaAndDownloadTo(outputStream);
                        String projectCommands = outputStream.toString();

                        if (!localProjectId.contains(file.getId())) {
                          // Create a Project object and add it to the ArrayList
                          projectsList.add(
                                  new ProjectsDataInObject(
                                          projectId, projectName, projectDate, projectCommands));
                        }
                      }
                    }

                    // Iterate over the local projects and remove any that are not present in the
                    // driveProjectId set
                    Iterator<ProjectsDataInObject> iterator = projectsList.iterator();
                    while (iterator.hasNext()) {
                      ProjectsDataInObject project = iterator.next();
                      if (!driveProjectId.contains(project.getProjectId())) {
                        iterator.remove();
                      }
                      sharedPreferencesManager.setProjectLIst(projectsList);
                    }

                    // update the UI on the main thread to reflect the changes in the list of drive
                    // files.
                    mActivity.runOnUiThread(
                            () -> {
                              adapter.notifyDataSetChanged();
                              projectsFragment.updateMessage(projectsList, binding);
                            });
                    // update page token to get the next set of files if available.
                    pageToken = result.getNextPageToken();
                  } catch (IOException e) {
                    // log any errors that occur and set page token to null to exit the loop.
                    e.printStackTrace();
                    pageToken = null;
                    mActivity.runOnUiThread(
                            () -> {
                              adapter.notifyDataSetChanged();
                              projectsFragment.updateMessage(projectsList, binding);
                            });
                  }
                  // continue querying for files while there is a valid page token.
                } while (pageToken != null);
              })
              .start();
    }
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

                  Timber.tag("Google Drive").i(String.valueOf(gDriveFile));
                } catch (IOException e) {
                  e.printStackTrace();
                }
              })
              .start();
    } else {
      Timber.tag("Google Drive").e("SignIn error - not logged in");
    }
  }

  /**
   * Renames a file with the given ID to the given new title.
   *
   * @param fileId   the ID of the file to rename
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
        Timber.tag("Google Drive File").d("File renamed successfully");
      } catch (IOException e) {
        // log any errors that occur when renaming the file.
        e.printStackTrace();
        Timber.tag("Google Drive File").e("Error renaming file");
      }
    }
  }

  /**
   * Deletes a file with the given ID from Google Drive.
   *
   * @param fileId fileId the ID of the file to delete.
   */
  public void deleteFile(
          String fileId,
          String projectName,
          DriveProjectsAdapter adapter,
          FragmentProjectsBinding binding) {
    // Create an ExecutorService with a single thread
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(
            () -> {
              Drive googleDriveService = getDriveService();
              if (googleDriveService != null) {
                try {
                  // Get the XML file ID associated with the project name
                  String xmlFileId = getXmlFileId(projectName, googleDriveService);
                  // Delete the file with the given ID
                  googleDriveService.files().delete(fileId).execute();
                  // If XML file ID exists, delete the XML file as well
                  if (xmlFileId != null) {
                    googleDriveService.files().delete(xmlFileId).execute();
                  }
                  // Access Drive files again to update the adapter and binding
                  accessDriveFiles(adapter, binding);
                  Timber.tag("Google Drive File").d("File deleted successfully");
                } catch (IOException error) {
                  // log any errors that occur when deleting the file.
                  error.printStackTrace();
                  Timber.tag("Google Drive File").e(error);
                }
              }
            });
  }

  /**
   * This method queries Google Drive for files that are not in the trash folder and have a .js
   * extension. It iterates through the files to find the one whose name matches the provided
   * projectName with the .xml extension. The corresponding XML file ID is returned if found.
   *
   * @param projectName
   * @param googleDriveService
   * @return
   */
  private String getXmlFileId(String projectName, Drive googleDriveService) {
    String pageToken = null;
    String xmlProjectId = null;
    do {
      try {
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
        List<File> driveProjectFiles = result.getFiles();
        // Iterate through the files and check for the XML file associated with the project name
        for (File file : driveProjectFiles) {
          if ((projectName.replace(".js", ".xml")).equals(file.getName())) {
            xmlProjectId = file.getId();
          }
        }
        // update page token to get the next set of files if available.
        pageToken = result.getNextPageToken();
      } catch (IOException e) {
        // log any errors that occur and set page token to null to exit the loop.
        e.printStackTrace();
        pageToken = null;
      }
      // continue querying for files while there is a valid page token.
    } while (pageToken != null);
    return xmlProjectId;
  }

  public void createAndUploadJsonFile(List<Model> modelList) {
    Drive getDriveService = getDriveService();
    Gson gson = new GsonBuilder().create();
    String modelListContent = gson.toJson(modelList);
    new Thread(
            () -> {
              String pageToken = null;
              do {
                try {
                  if (getDriveService != null) {
                    FileList result =
                            getDriveService
                                    .files()
                                    .list()
                                    .setSpaces("drive")
                                    .setPageToken(pageToken)
                                    .setQ("trashed = false")
                                    .execute();
                    List<File> driveProjectFiles = result.getFiles();
                    boolean getOpenBotPlayGroundFile = false;
                    boolean getConfigFIle = false;
                    String configFileId = null;
                    String openBotPlayGroundFileId = null;
                    for (File driveProjectFile : driveProjectFiles) {
                      if (driveProjectFile.getName().equals("openBot-Playground")) {
                        getOpenBotPlayGroundFile = true;
                        openBotPlayGroundFileId = driveProjectFile.getId();
                      }
                      if (driveProjectFile.getName().equals("config.json")) {
                        getConfigFIle = true;
                        configFileId = driveProjectFile.getId();
                      }
                    }

                    if (getOpenBotPlayGroundFile && getConfigFIle)
                      updateModelListFile(modelListContent, configFileId);
                    if (getOpenBotPlayGroundFile && !getConfigFIle)
                      createModelListFile(modelListContent, openBotPlayGroundFileId);
                    if (!getOpenBotPlayGroundFile && !getConfigFIle)
                      createOpenBotFolder(modelListContent);
                    pageToken = result.getNextPageToken();
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                  pageToken = null;
                }
              } while (pageToken != null);
            }).start();
  }

  private void createOpenBotFolder(String modelListContent) {
    Drive driveService = getDriveService();
    File fileMetadata = new File();
    fileMetadata.setName("openBot-Playground");
    fileMetadata.setMimeType("application/vnd.google-apps.folder");

    if (driveService != null) {
      try {
        File file = driveService.files().create(fileMetadata)
                .setFields("id")
                .execute();
        createModelListFile(modelListContent, file.getId());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void createModelListFile(String modelListContent, String playGroundFolderId) {
    Drive getDriveService = getDriveService();
    File fileMetadata = new File();
    fileMetadata.setName("config.json").setParents(Collections.singletonList(playGroundFolderId));
    ByteArrayContent content = ByteArrayContent.fromString("application/json", modelListContent);

    new Thread(() -> {
      try {
        if (getDriveService != null) {
          File file = getDriveService.files().create(fileMetadata, content).setFields("id").execute();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
  }

  private void updateModelListFile(String modelListContent, String fileId) {
    Drive driveService = getDriveService();
    new Thread(() -> {
      if (driveService != null) {
        try {
          // Create the file content from the new JSON content
          ByteArrayContent content = ByteArrayContent.fromString("application/json", modelListContent);
          // Update the file's content
          File updatedFile = driveService.files().update(fileId, null, content).execute();
          // File content updated successfully
        } catch (IOException e) {
          e.printStackTrace();
          // Handle the update error
        }
      }
    }).start();
  }

}
