package org.openbot.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import java.util.Objects;
import org.openbot.R;
import org.openbot.common.Constants;
import org.openbot.robot.NetworkActivity;

// For a library module, uncomment the following line
// import org.openbot.controller.ControllerActivity;

public class MainActivity extends AppCompatActivity {

  private MainViewModel viewModel;
  private NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    NavHostFragment navHostFragment =
        (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    navController = Objects.requireNonNull(navHostFragment).getNavController();

    viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    gotoSelectedFeature();

    // Default to open this when app opens
    Intent intent = new Intent(this, NetworkActivity.class);
    startActivity(intent);
  }

  private void gotoSelectedFeature() {
    viewModel
        .getSelectedMode()
        .observe(
            this,
            subCategory -> {
              switch (subCategory.getTitle()) {
                case Constants.GLOBAL_VIEW:
                  Intent intent = new Intent(this, NetworkActivity.class);
                  startActivity(intent);
                  break;

                case Constants.FREE_ROAM:
                  navController.navigate(R.id.action_mainFragment_to_gameFragment);
                  break;

                case Constants.CONTROLLER:
                  // For a library module, uncomment the following line
                  // intent = new Intent(this, ControllerActivity.class);
                  // startActivity(intent);
                  break;
              }
            });
  }
}
