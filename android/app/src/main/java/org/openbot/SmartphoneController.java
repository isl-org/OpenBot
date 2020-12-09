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
import static java.lang.Float.parseFloat;

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

                        // {r:0.22, l:0.3}
                        String rightValue = null;
                        String leftValue = null;
                        if (jsonCommand.has("r") && jsonCommand.has("l")) {
                            leftValue = jsonCommand.getString("l");
                            rightValue = jsonCommand.getString("r");
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

                        if (rightValue != null && leftValue != null) {
                            Log.i(TAG, "left: " + parseFloat(leftValue) + ", right: " + parseFloat(rightValue));
                        }

                        if (buttonValue != null) {
                            queue.add(evt);
                        }

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
