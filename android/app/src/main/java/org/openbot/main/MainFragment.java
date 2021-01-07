package org.openbot.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.openbot.R;
import org.openbot.common.Constants;
import org.openbot.common.OnItemClickListener;
import org.openbot.databinding.FragmentMainBinding;
import org.openbot.env.Logger;
import org.openbot.model.SubCategory;
import org.openbot.robot.NetworkActivity;

public class MainFragment extends Fragment implements OnItemClickListener<SubCategory> {

  private MainViewModel mViewModel;
  private FragmentMainBinding binding;
  private static final Logger LOGGER = new Logger();

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
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.list.setAdapter(new CategoryAdapter(Constants.getCategories(), this));
    binding.toolbar.inflateMenu(R.menu.menu_items);

    binding.toolbar.setOnMenuItemClickListener(
        item -> {
          switch (item.getItemId()) {
            case R.id.action_settings:
              // Navigate to settings screen
              return true;

            default:
              return false;
          }
        });
  }

  @Override
  public void onItemClick(SubCategory subCategory) {

    LOGGER.d("onItemClick: " + subCategory.getTitle());

    switch (subCategory.getTitle()) {
      case Constants.GLOBAL_VIEW:
        Intent intent = new Intent(requireActivity(), NetworkActivity.class);
        startActivity(intent);
        break;

      case Constants.FREE_ROAM:
        Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_robotCommunicationFragment);
        break;

      case Constants.CONTROLLER:
        // For a library module, uncomment the following line
        // intent = new Intent(this, ControllerActivity.class);
        // startActivity(intent);
        break;
    }
  }
}
