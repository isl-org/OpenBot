package org.openbot.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import java.util.Objects;
import org.openbot.NetworkActivity;
import org.openbot.R;
import org.openbot.common.Constants;
import org.openbot.env.Logger;

public class MainActivity extends AppCompatActivity {

  private MainViewModel viewModel;
  private static final Logger LOGGER = new Logger();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    NavHostFragment navHostFragment =
        (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

    viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    gotoSelectedFeature();
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
                  finish();

                  break;
              }
            });
  }
}
