package org.openbot.pointGoalNavigation;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.openbot.R;
import org.openbot.common.ControlsFragment;

public class PointGoalNavigationFragment extends ControlsFragment {

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
    return inflater.inflate(R.layout.fragment_point_goal_navigation, container, false);
  }

  @Override
  protected void processControllerKeyData(String command) {

  }

  @Override
  protected void processUSBData(String data) {

  }
}
