package org.openbot.main;

import static org.openbot.common.Constants.USB_ACTION_CONNECTION_CLOSED;
import static org.openbot.common.Constants.USB_ACTION_CONNECTION_ESTABLISHED;
import static org.openbot.common.Constants.USB_ACTION_DATA_RECEIVED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.openbot.R;
import org.openbot.robot.RobotCommunicationViewModel;

// For a library module, uncomment the following line
// import org.openbot.controller.ControllerActivity;

public class MainActivity extends AppCompatActivity {

  private MainViewModel viewModel;
  private RobotCommunicationViewModel robotCommunicationViewModel;
  private LocalBroadcastManager localBroadcastManager;
  private BroadcastReceiver localBroadcastReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    robotCommunicationViewModel =
        new ViewModelProvider(this).get(RobotCommunicationViewModel.class);

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

    // Default to open this when app opens
    //    Intent intent = new Intent(this, NetworkActivity.class);
    //    startActivity(intent);
  }

  @Override
  public boolean dispatchGenericMotionEvent(MotionEvent event) {
    if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
        && event.getAction() == MotionEvent.ACTION_MOVE) {
      robotCommunicationViewModel.setGenericMotionEvent(event);
      return true;
    }
    return super.dispatchGenericMotionEvent(event);
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    // Check that the event came from a game controller
    if ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD
        && event.getAction() == KeyEvent.ACTION_UP) {
      robotCommunicationViewModel.setKeyEvent(event.getKeyCode());
      return true;
    }
    return super.dispatchKeyEvent(event);
  }
}
