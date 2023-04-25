package org.openbot.projects;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.util.List;
import org.openbot.R;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentProjectsBinding;
import org.openbot.googleServices.GoogleServices;
import org.openbot.main.CommonRecyclerViewAdapter;
import org.openbot.utils.BotFunctionUtils;

public class ProjectsFragment extends ControlsFragment {
  private FragmentProjectsBinding binding;
  private View signInButton;
  private TextView projectScreenText;
  private TextView projectsNotFoundTxt;
  private GoogleServices googleServices;
  private RecyclerView projectsRV;
  private LinearLayout noProjectsLayout;
  private DriveProjectsAdapter adapter;
  private BarCodeScannerFragment barCodeScannerFragment;
  private BottomSheetBehavior dpBottomSheetBehavior;
  private View overlayView;
  private SwipeRefreshLayout swipeRefreshLayout;
  private boolean isSignedIn = false;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentProjectsBinding.inflate(inflater, container, false);
    noProjectsLayout = binding.getRoot().findViewById(R.id.noProjects_layout);
    signInButton = binding.getRoot().findViewById(R.id.sign_in_button);
    projectsNotFoundTxt = binding.getRoot().findViewById(R.id.projects_not_found);
    projectScreenText = binding.getRoot().findViewById(R.id.project_screen_info);
    googleServices = new GoogleServices(requireActivity(), requireContext(), newGoogleServices);
    projectsRV = binding.getRoot().findViewById(R.id.projects_rv);
    ConstraintLayout driveProjectBottomSheet = binding.getRoot().findViewById(R.id.dp_bottom_sheet);
    overlayView = binding.getRoot().findViewById(R.id.overlay_view);
    dpBottomSheetBehavior = BottomSheetBehavior.from(driveProjectBottomSheet);
    dpBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    barCodeScannerFragment = new BarCodeScannerFragment();
    signInButton.setOnClickListener(v -> signIn());
    swipeRefreshLayout = requireView().findViewById(R.id.refreshLayout);
    dpBottomSheetBehavior.addBottomSheetCallback(dpBottomSheetCallback);
    requireView()
        .findViewById(R.id.dp_start_btn)
        .setOnClickListener(
            v ->
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_projectsFragment_to_blocklyExecutingFragment));
    requireView()
        .findViewById(R.id.dp_cancel_btn)
        .setOnClickListener(v -> dpBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
    showProjectsRv();

    swipeRefreshLayout.setOnRefreshListener(
        () -> {
          showProjectsRv();
          swipeRefreshLayout.setRefreshing(false);
        });
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
            showProjectsRv();
          });

  private GoogleSignInCallback newGoogleServices =
      new GoogleSignInCallback() {
        @Override
        public void onSignInSuccess(GoogleSignInAccount account) {
          noProjectsLayout.setVisibility(View.GONE);
          signInButton.setVisibility(View.GONE);
          projectsNotFoundTxt.setVisibility(View.VISIBLE);
          isSignedIn = true;
          projectScreenText.setText("Looks like there are no projects in your google drive yet.");
        }

        @Override
        public void onSignInFailed(Exception exception) {
          noProjectsLayout.setVisibility(View.VISIBLE);
          signInButton.setVisibility(View.VISIBLE);
          projectsNotFoundTxt.setVisibility(View.GONE);
          isSignedIn = false;
          projectScreenText.setText("Set up your profile by signing in with your Google account.");
          binding.projectsLoader.setVisibility(View.GONE);
        }

        @Override
        public void onSignOutSuccess() {
          noProjectsLayout.setVisibility(View.VISIBLE);
          signInButton.setVisibility(View.VISIBLE);
          projectsNotFoundTxt.setVisibility(View.GONE);
          isSignedIn = false;
          projectScreenText.setText("Set up your profile by signing in with your Google account.");
        }

        @Override
        public void onSignOutFailed(Exception exception) {}
      };

  private void showProjectsRv() {
      if(isSignedIn){
          binding.projectsLoader.setVisibility(View.VISIBLE);
      }
    projectsRV.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
    SparseArray<int[]> driveRes = new SparseArray<>();
    driveRes.put(R.layout.projects_list_view, new int[] {R.id.project_name, R.id.project_date});
    setScanDeviceAdapter(
        new DriveProjectsAdapter(requireActivity(), googleServices.getDriveFiles(), driveRes),
        (itemView, position) -> readFileFromDrive(googleServices.getDriveFiles().get(position).getId()));
    googleServices.accessDriveFiles(adapter, binding);
    projectsRV.setAdapter(adapter);
  }

  /** update projects screen when there is 0 projects on google drive account. */
  public void updateMessage(List<File> driveFiles, FragmentProjectsBinding binding) {
    binding.projectsLoader.setVisibility(View.GONE);

    if (driveFiles.size() <= 0) {
      binding.noProjectsLayout.setVisibility(View.VISIBLE);
    }
  }

  private void setScanDeviceAdapter(
      DriveProjectsAdapter adapter,
      @NonNull CommonRecyclerViewAdapter.OnItemClickListener onItemClickListener) {
    this.adapter = adapter;
    adapter.setOnItemClickListener(onItemClickListener);
  }

  private void readFileFromDrive(String fileId) {
      binding.projectsLoader.setVisibility(View.VISIBLE);
      binding.overlayView.setVisibility(View.VISIBLE);
    new ReadFileTask(
            fileId,
            new ReadFileCallback() {
              @Override
              public void onFileReadSuccess(String fileContents) {
                String code = fileContents;
                for (String fun : BotFunctionUtils.botFunctionArray) {
                  if (code.contains(fun)) {
                    code = code.replace(fun, "Android." + fun);
                  }
                }
                barCodeScannerFragment.finalCode = code;
                  dpBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
              }

              @Override
              public void onFileReadFailed(IOException e) {
                requireActivity()
                    .runOnUiThread(
                        () -> {
                          AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                          builder.setTitle("Error");
                          builder.setMessage(
                              "Something error with this file! pull down to refresh and try again.");
                          builder.setCancelable(false);
                          builder.setNegativeButton("Ok", (dialog, which) -> dialog.cancel());
                          AlertDialog alertDialog = builder.create();
                          alertDialog.show();
                        });
              }
            })
        .execute();
  }

  private BottomSheetBehavior.BottomSheetCallback dpBottomSheetCallback =
      new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
          // Handle state changes here
          if (newState == BottomSheetBehavior.STATE_EXPANDED
              || newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
              binding.projectsLoader.setVisibility(View.GONE);
              overlayView.setVisibility(View.VISIBLE);
          } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            overlayView.setVisibility(View.GONE);
          }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
          // Handle sliding events here
        }
      };

  @Override
  public void onResume() {
    super.onResume();
    dpBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
  }

  @Override
  protected void processControllerKeyData(String command) {}

  @Override
  protected void processUSBData(String data) {}
}
