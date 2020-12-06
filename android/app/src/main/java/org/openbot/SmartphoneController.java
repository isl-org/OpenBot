package org.openbot;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.view.MotionEvent.obtain;

public class SmartphoneController {

    private static final String TAG = "SmartphoneController";

    private static final NearbyConnection connection = new NearbyConnection();

    public void connect(Context context) {
        connection.connect(context, payloadCallback);
    }
    public void disconnect() {
        connection.disconnect();
    }

    // Callbacks for receiving payloads
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                final BlockingQueue<KeyEvent> queue = new LinkedBlockingQueue<>(100);

                {
                    new Thread(new KeySender(queue)).start();
                }

                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                    String command = new String(payload.asBytes(), StandardCharsets.UTF_8);
                    try {
                        JSONObject jsonCommand = new JSONObject(command);
                        KeyEvent evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_D, 0, 0);

                        String buttonValue = null;
                        if (jsonCommand.has("buttonValue")) {
                            buttonValue = jsonCommand.getString("buttonValue");
                        }
                        String rightDriveValue = null;
                        if (jsonCommand.has("rightDrive")) {
                            rightDriveValue = jsonCommand.getString("rightDrive");
                        }

                        String leftDriveValue = null;
                        if (jsonCommand.has("leftDrive")) {
                            leftDriveValue = jsonCommand.getString("leftDrive");
                        }

                        if (buttonValue != null) {
                            switch (buttonValue) {
                                case "LOGS":
                                    evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_A, 0, 0);
                                    break;
                                case "NOISE":
                                    evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_START, 0, 0);
                                    break;
                                case "DRIVE_BY_NETWORK":
                                    evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_L1, 0, 0);
                                    break;
                                case "INDICATOR_RIGHT":
                                    evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_B, 0, 0);
                                    break;
                                case "INDICATOR_LEFT":
                                    evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_Y, 0, 0);
                                    break;
                                case "INDICATOR_STOP":
                                    evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_X, 0, 0);
                                    break;
                                default:
                                    Log.e(TAG, "Woooo, what are you doing? Will take away your keys!");
                                    break;
                            }
                            evt.setSource(InputDevice.SOURCE_GAMEPAD);
                        }

                        if (rightDriveValue != null) {
                            Log.e(TAG, "rightDriveValue: " + rightDriveValue);
                            evt.setSource(InputDevice.SOURCE_JOYSTICK);
                        }

                        if (leftDriveValue != null) {
                            Log.e(TAG, "leftDriveValue: " + leftDriveValue);
                            evt.setSource(InputDevice.SOURCE_JOYSTICK);
                        }

                        queue.add(evt);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate update) {
                }
            };

    public boolean isConnected() {
        return connection.isConnected();
    }

    private static class KeySender implements Runnable {

        private final BlockingQueue<KeyEvent> queue;
        private final Instrumentation instrumentation = new Instrumentation();

        public KeySender(BlockingQueue<KeyEvent> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    KeyEvent keyEvent = queue.take();

                    // This must run in the background...
                    instrumentation.sendKeySync(keyEvent);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
