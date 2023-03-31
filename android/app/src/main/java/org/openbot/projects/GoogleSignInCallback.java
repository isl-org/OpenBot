package org.openbot.projects;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface GoogleSignInCallback {
    void onSignInSuccess(GoogleSignInAccount account);

    void onSignInFailed(Exception exception);

    void onSignOutSuccess();

    void onSignOutFailed(Exception exception);
}
