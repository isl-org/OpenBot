package org.openbot.env;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PhoneController {

    private static final String TAG = "PhoneController";
    final ILocalConnection connection =
            new WiFiDirectConnection();
            // new NearbyConnection();

    {
        connection.setDataCallback (new DataReceived());
        handleBotEvents();
    }

    static class DataReceived implements IDataReceived {
        @Override
        public void dataReceived(String commandStr) {
            try {
                ControllerToBotEventBus.emitEvent(new JSONObject(commandStr));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void connect(Context context) {
        connection.init (context);
        connection.connect(context);
    }

    public void disconnect(Context context) {
        connection.disconnect(context);
    }

    public void send(JSONObject info) {
        connection.sendMessage(info.toString());
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    private void handleBotEvents() {
        BotToControllerEventBus.getProcessor().subscribe(event -> send(event));
    }
}
