package org.openbot.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import org.openbot.R;
import org.openbot.common.FeatureList;
import org.openbot.databinding.FragmentMainBinding;
import org.openbot.googleServices.GoogleServices;
import org.openbot.model.SubCategory;
import org.openbot.original.DefaultActivity;
import org.openbot.projects.GoogleSignInCallback;
import timber.log.Timber;

public class MainFragment extends Fragment implements OnItemClickListener<SubCategory> {

  private MainViewModel mViewModel;
  private FragmentMainBinding binding;
  private View overlayView;
  private BottomSheetBehavior signInBottomSheetBehavior;
  private GoogleServices googleServices;
  private SharedPreferences sharedPref;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentMainBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.list.setAdapter(new CategoryAdapter(FeatureList.getCategories(), this));
    LinearLayout signInButton = requireView().findViewById(R.id.sign_in_button);
    TextView guestBtn = requireView().findViewById(R.id.continue_as_guest);
    overlayView = requireView().findViewById(R.id.overlay_view);
    ConstraintLayout signInBottomSheet = binding.getRoot().findViewById(R.id.signIn_bottom_sheet);
    signInBottomSheetBehavior = BottomSheetBehavior.from(signInBottomSheet);
    sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
    googleServices = new GoogleServices(requireActivity(), requireContext(), newGoogleServices);
    signInButton.setOnClickListener(v -> signIn());
    guestBtn.setOnClickListener(v -> continueAsGuest());
    String signInKey = sharedPref.getString("signInMode", "");
    if (signInKey.equals("continueAsGuest") || signInKey.equals("googleSignIn")) {
      signInBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
      overlayView.setVisibility(View.GONE);
    }
    signInBottomSheetBehavior.setBottomSheetCallback(signInBottomSheetCallback);
  }

  @Override
  public void onItemClick(SubCategory subCategory) {

    Timber.d("onItemClick: %s", subCategory.getTitle());

    switch (subCategory.getTitle()) {
      case FeatureList.PROJECTS:
        Navigation.findNavController(requireView()).navigate(R.id.projectsFragment);
        break;
      case FeatureList.FREE_ROAM:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_freeRoamFragment);
        break;

      case FeatureList.DATA_COLLECTION:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_loggerFragment);
        break;

      case FeatureList.CONTROLLER:
        // For a library module, uncomment the following line
        // intent = new Intent(this, ControllerActivity.class);
        // startActivity(intent);
        break;

      case FeatureList.CONTROLLER_MAPPING:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_controllerMappingFragment);
        break;

      case FeatureList.ROBOT_INFO:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_robotInfoFragment);
        break;

      case FeatureList.AUTOPILOT:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_autopilotFragment);
        break;

      case FeatureList.OBJECT_NAV:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_objectNavFragment);
        break;

      case FeatureList.POINT_GOAL_NAVIGATION:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_pointGoalNavigationFragment);
        break;

      case FeatureList.MODEL_MANAGEMENT:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_modelManagementFragment);
        break;

      case FeatureList.DEFAULT:
        Intent intent = new Intent(requireActivity(), DefaultActivity.class);
        startActivity(intent);
        break;
    }
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
          signInBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
          SharedPreferences.Editor editor = sharedPref.edit();
          editor.putString("signInMode", "googleSignIn");
          editor.apply();
        }

        @Override
        public void onSignInFailed(Exception exception) {}

        @Override
        public void onSignOutSuccess() {}

        @Override
        public void onSignOutFailed(Exception exception) {}
      };

  private void continueAsGuest() {
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString("signInMode", "continueAsGuest");
    editor.apply();
    signInBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
  }

  private BottomSheetBehavior.BottomSheetCallback signInBottomSheetCallback =
      new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
          // Handle state changes here
          if (newState == BottomSheetBehavior.STATE_EXPANDED
              || newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            overlayView.setVisibility(View.VISIBLE);
          } else {
            overlayView.setVisibility(View.GONE);
          }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
          // Handle sliding events here
        }
      };
}
