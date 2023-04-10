package org.openbot.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import org.openbot.R;
import org.openbot.databinding.FragmentProfileBinding;
import org.openbot.googleServices.GoogleServices;
import org.openbot.projects.GoogleSignInCallback;

public class ProfileFragment extends Fragment {

  private FragmentProfileBinding binding;
  private LinearLayout linearLayoutSignIn;
  private LinearLayout linearLayoutProfile;
  private LinearLayout signInButton;
  private TextView logoutButton;
  private GoogleServices googleServices;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentProfileBinding.inflate(inflater, container, false);
    linearLayoutSignIn = binding.getRoot().findViewById(R.id.profile_signIn_view);
    linearLayoutProfile = binding.getRoot().findViewById(R.id.profile_settings);
    signInButton = binding.getRoot().findViewById(R.id.sign_in_button);
    logoutButton = binding.getRoot().findViewById(R.id.logout_btn);
    googleServices = new GoogleServices(requireActivity(), requireContext(), newGoogleServices);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    signInButton.setOnClickListener(v -> signIn());
    logoutButton.setOnClickListener(v -> googleServices.signOut());
  }

  private void signIn() {
    Intent signInIntent = googleServices.mGoogleSignInClient.getSignInIntent();
    googleLogInActivityResultLauncher.launch(signInIntent);
  }

  ActivityResultLauncher<Intent> googleLogInActivityResultLauncher =
      registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          result -> {
            Intent data = result.getData();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            googleServices.handleSignInResult(task);
          });

  private GoogleSignInCallback newGoogleServices =
      new GoogleSignInCallback() {
        @Override
        public void onSignInSuccess(GoogleSignInAccount account) {
          linearLayoutSignIn.setVisibility(View.GONE);
          linearLayoutProfile.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSignInFailed(Exception exception) {
          linearLayoutSignIn.setVisibility(View.VISIBLE);
          linearLayoutProfile.setVisibility(View.GONE);
        }

        @Override
        public void onSignOutSuccess() {
          linearLayoutSignIn.setVisibility(View.VISIBLE);
          linearLayoutProfile.setVisibility(View.GONE);
        }

        @Override
        public void onSignOutFailed(Exception exception) {}
      };
}
