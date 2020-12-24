package org.openbot.env;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class PhoneController {

  private static final String TAG = "PhoneController";

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
        @Override
        public void onPayloadReceived(@NotNull String endpointId, Payload payload) {
          String commandStr = new String(payload.asBytes(), StandardCharsets.UTF_8);
          try {
            ControllerEventProcessor.emitEvent(new JSONObject(commandStr));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void onPayloadTransferUpdate(
            @NonNull String endpointId, @NonNull PayloadTransferUpdate update) {}
      };

  public boolean isConnected() {
    return connection.isConnected();
  }
}
