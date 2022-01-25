package org.openbot.pointGoalNavigation;

import static java.lang.Math.abs;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.ar.core.Pose;
import com.google.ar.core.TrackingFailureReason;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import org.openbot.common.ControlsFragment;
import org.openbot.databinding.FragmentPointGoalNavigationBinding;
import org.openbot.main.MainViewModel;
import org.openbot.vehicle.Vehicle;

public class PointGoalNavigationFragment extends ControlsFragment implements ArCoreListener {

  private MainViewModel mainViewModel;
  private Vehicle vehicle;
  private Handler handlerMain;
  private ArCore arCore;
  private FragmentPointGoalNavigationBinding binding;
  private boolean isRunning = false;

  public PointGoalNavigationFragment() {
    // Required empty public constructor
  }

  public static PointGoalNavigationFragment newInstance() {
    PointGoalNavigationFragment fragment = new PointGoalNavigationFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentPointGoalNavigationBinding.inflate(inflater, container, false);

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    vehicle = mainViewModel.getVehicle().getValue();

    handlerMain = new Handler(Looper.getMainLooper());

    arCore = new ArCore(requireContext(), binding.surfaceView, handlerMain);

    showStartDialog();
  }

  @Override
  public void onArCoreUpdate(NavigationPoses navigationPoses, ImageFrame rgb,
      CameraIntrinsics cameraIntrinsics, long timestamp) {
    if(isRunning) {
      if(computeDistance(navigationPoses.getTargetPose(), navigationPoses.getCurrentPose()) < 0.15f) {
        isRunning = false;
        // TODO: show stop dialog
      } else {
        // TODO: execute policy
      }
    }
  }

  @Override
  public void onArCoreTrackingFailure(long timestamp, TrackingFailureReason trackingFailureReason) {

  }

  private static float computeDistance(Pose goalPose, Pose robotPose) {
    Float dx = abs(goalPose.tx() - robotPose.tx());
    Float dz = abs(goalPose.tz() - robotPose.tz());
    float distance = (float) Math.sqrt(dx * dx + dz * dz);

    return distance;
  }

  @Override
  public void onStart() {
    super.onStart();

    arCore.setArCoreListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    try {
      arCore.resume();
    } catch (UnavailableSdkTooOldException e) {
      e.printStackTrace();
    } catch (UnavailableDeviceNotCompatibleException e) {
      e.printStackTrace();
    } catch (UnavailableArcoreNotInstalledException e) {
      e.printStackTrace();
    } catch (UnavailableApkTooOldException e) {
      e.printStackTrace();
    } catch (CameraNotAvailableException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onPause() {
    super.onPause();

    arCore.pause();
  }

  @Override
  public void onStop() {
    super.onStop();

    arCore.removeArCoreListener();
  }

  @Override
  protected void processControllerKeyData(String command) {

  }

  @Override
  protected void processUSBData(String data) {

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    arCore.closeSession();
  }

  private void showStartDialog() {
    if (getChildFragmentManager().findFragmentByTag(SetGoalDialogFragment.TAG) == null) {
      SetGoalDialogFragment dialog = SetGoalDialogFragment.newInstance();
      dialog.setCancelable(false);
      dialog.show(getChildFragmentManager(), SetGoalDialogFragment.TAG);
    }

    getChildFragmentManager()
        .setFragmentResultListener(
            SetGoalDialogFragment.TAG,
            getViewLifecycleOwner(),
            (requestKey, result) -> {
              Boolean start = result.getBoolean("start");

              if(start) {
                Float forward = result.getFloat("forward");
                Float left = result.getFloat("left");

                // x: right, z: backwards
                startDriving(-left, -forward);
              }
              else {
                getActivity().onBackPressed();
              }
            });
  }

  private void startDriving(float goalX, float goalZ) {
    arCore.setStartAnchorAtCurrentPose();
    arCore.setTargetAnchor(Pose.makeTranslation(goalX, 0.0f, goalZ));

    isRunning = true;
  }
}
