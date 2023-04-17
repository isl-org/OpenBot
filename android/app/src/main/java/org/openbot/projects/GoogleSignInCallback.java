package org.openbot.projects;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * This is an interface that defines four methods for handling the results of google signIn and signOut services.
 */
public interface GoogleSignInCallback {
  void onSignInSuccess(GoogleSignInAccount account);

  void onSignInFailed(Exception exception);

  void onSignOutSuccess();

  void onSignOutFailed(Exception exception);
}
