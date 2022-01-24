package org.openbot.pointGoalNavigation;

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
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingFailureReason;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import org.openbot.databinding.FragmentPointGoalNavigationBinding;
import org.openbot.main.MainViewModel;
import org.openbot.vehicle.Vehicle;

public class PointGoalNavigationFragment extends Fragment implements ArCoreListener {

  private MainViewModel mainViewModel;
  private Vehicle vehicle;
  private Handler handlerMain;
  private ArCore arCore;
  private FragmentPointGoalNavigationBinding binding;

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
  }

  @Override
  public void onArCoreUpdate(NavigationPoses navigationPoses, ImageFrame rgb,
      CameraIntrinsics cameraIntrinsics, long timestamp) {

  }

  @Override
  public void onArCoreTrackingFailure(long timestamp, TrackingFailureReason trackingFailureReason) {

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
  public void onStop() {
    super.onStop();

    arCore.removeArCoreListener();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    arCore.closeSession();
  }
}
