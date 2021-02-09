package org.openbot.main;

import static org.openbot.common.Constants.USB_ACTION_CONNECTION_CLOSED;
import static org.openbot.common.Constants.USB_ACTION_CONNECTION_ESTABLISHED;
import static org.openbot.common.Constants.USB_ACTION_DATA_RECEIVED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import org.openbot.R;
import org.openbot.common.Constants;
import org.openbot.env.Vehicle;
import org.openbot.robot.NetworkActivity;

// For a library module, uncomment the following line
// import org.openbot.controller.ControllerActivity;

public class MainActivity extends AppCompatActivity {

  private MainViewModel viewModel;
  private LocalBroadcastManager localBroadcastManager;
  private BroadcastReceiver localBroadcastReceiver;
  private Vehicle vehicle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    viewModel = new ViewModelProvider(this).get(MainViewModel.class);

    localBroadcastReceiver =
        new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action != null) {
              switch (action) {
                case USB_ACTION_CONNECTION_ESTABLISHED:

                case USB_ACTION_CONNECTION_CLOSED:
                  break;

                case USB_ACTION_DATA_RECEIVED:
                  viewModel.setUsbData(intent.getStringExtra("data"));
                  break;
              }
            }
          }
        };
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction(USB_ACTION_CONNECTION_ESTABLISHED);
    localIntentFilter.addAction(USB_ACTION_CONNECTION_CLOSED);
    localIntentFilter.addAction(USB_ACTION_DATA_RECEIVED);
    localBroadcastManager = LocalBroadcastManager.getInstance(this);
    localBroadcastManager.registerReceiver(localBroadcastReceiver, localIntentFilter);

    NavHostFragment navHostFragment =
        (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    NavController navController = navHostFragment.getNavController();
    AppBarConfiguration appBarConfiguration =
        new AppBarConfiguration.Builder(navController.getGraph()).build();
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

    navController.addOnDestinationChangedListener(
        (controller, destination, arguments) -> {
          if (destination.getId() == R.id.mainFragment
              || destination.getId() == R.id.settingsFragment) toolbar.setVisibility(View.VISIBLE);
          else toolbar.setVisibility(View.GONE);
        });

    if (savedInstanceState == null) {
      // Default to open this when app opens
      Intent intent = new Intent(this, NetworkActivity.class);
      startActivity(intent);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_items, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return NavigationUI.onNavDestinationSelected(item, navController)
        || super.onOptionsItemSelected(item);
  }

  @Override
  public boolean dispatchGenericMotionEvent(MotionEvent event) {
    if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
        && event.getAction() == MotionEvent.ACTION_MOVE) {
      Bundle bundle = new Bundle();
      bundle.putParcelable(Constants.DATA, event);
      getSupportFragmentManager().setFragmentResult(Constants.GENERIC_MOTION_EVENT, bundle);
      return true;
    }
    return super.dispatchGenericMotionEvent(event);
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    // Check that the event came from a game controller
    if ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
      if (event.getAction() == KeyEvent.ACTION_UP) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, event);
        getSupportFragmentManager().setFragmentResult(Constants.KEY_EVENT, bundle);
      }
      return true;
    }
    return super.dispatchKeyEvent(event);
  }

  @Override
  public synchronized void onDestroy() {
    if (localBroadcastManager != null) {
      localBroadcastManager.unregisterReceiver(localBroadcastReceiver);
      localBroadcastManager = null;
    }
    if (localBroadcastReceiver != null) localBroadcastReceiver = null;
    vehicle.disconnectUsb();
    super.onDestroy();
  }

  @Override
  protected void onResume() {
    super.onResume();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    int baudRate = Integer.parseInt(sharedPreferences.getString("baud_rate", "115200"));
    vehicle = new Vehicle(this, baudRate);
    viewModel.setVehicle(vehicle);
  }
}
