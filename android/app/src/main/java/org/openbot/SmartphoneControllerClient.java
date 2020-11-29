package org.openbot;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SmartphoneControllerClient {

    private static final String TAG = "SmartphoneController";

    private static final NearbyConnection connection = new NearbyConnection();

    public void connect(Context context) {
        connection.connect(context, payloadCallback);
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

                    KeyEvent evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_D, 0, 0);

                    switch (command) {
                        case "LEFT":
                            evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_L1, 0, 0);
                            break;
                        case "RIGHT":
                            evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_R1, 0, 0);
                            break;
                        case "GO":
                            evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_X, 0, 0);
                            break;
                        case "STOP":
                            evt = new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_Y, 0, 0);
                            break;
                        default:
                            Log.e(TAG, "Woooo, what are you doing? Will take away your keys!");
                            break;
                    }

                    Log.d("Received Command", command);
                    evt.setSource(InputDevice.SOURCE_GAMEPAD);
                    queue.add(evt);
                }

                @Override
                public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate update) {
                }
            };


    private class KeySender implements Runnable {

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
