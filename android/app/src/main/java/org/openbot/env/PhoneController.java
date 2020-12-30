package org.openbot.env;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PhoneController {

  {
    handleBotEvents();
  }

  private static final String TAG = "PhoneController";

  private static final NearbyConnection connection;

  static {
    connection = new NearbyConnection();
  }

  public void connect(Context context) {
    connection.connect(context, payloadCallback);
  }

  public void disconnect() {
    connection.disconnect();
  }

  public void send(JSONObject info) {
    connection.sendMessage(info.toString());
  }

  // Callbacks for receiving payloads
  private final PayloadCallback payloadCallback =
      new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NotNull String endpointId, Payload payload) {
          String commandStr = new String(payload.asBytes(), StandardCharsets.UTF_8);
          try {
            ControllerToBotEventBus.emitEvent(new JSONObject(commandStr));
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

  private void handleBotEvents() {
    BotToControllerEventBus.getProcessor().subscribe(event -> send(event));
  }
}
