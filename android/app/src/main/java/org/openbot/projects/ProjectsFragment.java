package org.openbot.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import org.openbot.R;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentProjectsBinding;
import org.openbot.googleServices.GoogleServices;

public class ProjectsFragment extends ControlsFragment {
  private FragmentProjectsBinding binding;
  private View signInButton;
  private TextView projectScreenText;
  private TextView projectsNotFoundTxt;
  private GoogleServices googleServices;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentProjectsBinding.inflate(inflater, container, false);
    signInButton = binding.getRoot().findViewById(R.id.sign_in_button);
    projectsNotFoundTxt = binding.getRoot().findViewById(R.id.projects_not_found);
    projectScreenText = binding.getRoot().findViewById(R.id.project_screen_info);
    googleServices = new GoogleServices(requireActivity(), requireContext(), newGoogleServices);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    requireView()
        .findViewById(R.id.btnScan)
        .setOnClickListener(
            v -> Navigation.findNavController(requireView()).navigate(R.id.barCodeScannerFragment));
    signInButton.setOnClickListener(v -> signIn());
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
          signInButton.setVisibility(View.GONE);
          projectsNotFoundTxt.setVisibility(View.VISIBLE);
          projectScreenText.setText("Looks like there are no projects in your google drive yet.");
        }

        @Override
        public void onSignInFailed(Exception exception) {
          signInButton.setVisibility(View.VISIBLE);
          projectsNotFoundTxt.setVisibility(View.GONE);
          projectScreenText.setText("Set up your profile by signing in with your Google account.");
        }

        @Override
        public void onSignOutSuccess() {
          signInButton.setVisibility(View.VISIBLE);
          projectsNotFoundTxt.setVisibility(View.GONE);
          projectScreenText.setText("Set up your profile by signing in with your Google account.");
        }

        @Override
        public void onSignOutFailed(Exception exception) {}
      };

  @Override
  protected void processControllerKeyData(String command) {}

  @Override
  protected void processUSBData(String data) {}
}
