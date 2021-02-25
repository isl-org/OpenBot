package org.openbot.env;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.openbot.customview.AutoFitSurfaceView;
import timber.log.Timber;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PhoneController {

  private static final String TAG = "PhoneController";
  final ILocalConnection connection =
      // new NearbyConnection();
      new NetworkServiceConnection();

  private RtspServer rtspServer = new RtspServer();
  private Context context;

  {
    connection.setDataCallback(new DataReceived());
    handleBotEvents();
    handleControllerEvents();
  }

  public void setView(AutoFitSurfaceView videoWondow) {
    rtspServer.setView(videoWondow);
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

  public void init(Context context) {
    this.context = context;
    rtspServer.init(context);
  }

  public void connect(Context context) {
    connection.init(context);
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
    BotToControllerEventBus.getProcessor()
        .subscribe(
            this::send,
            error -> Timber.d("Error occurred in BotToControllerEventBus: " + error));
  }

  private void handleControllerEvents() {
    // Prevent multiple subscriptions. This happens if we select "Phone control multiple times.
    if (ControllerToBotEventBus.getProcessor().hasObservers()) {
      Timber.d("Aleady has subcribers.............");
    }

    ControllerToBotEventBus.getProcessor()
        .subscribe(
            event -> {
              String commandType;
              if (event.has("command")) {
                commandType = event.getString("command");
              } else {
                Timber.d("Got invalid command from controller: " + event.toString());
                return;
              }

              switch (commandType) {
                case "CONNECTED":
                  Timber.d("CONNECTED");
                  if (rtspServer.isRunning()) {
                    rtspServer.sendConnectionParams();
                  }
                  break;

                case "DISCONNECTED":
                  Timber.d("DISCONNECTED");
                  break;
              }
            },
            error -> {
              Timber.d("Error occurred in ControllerToBotEventBus: " + error);
            });
  }
}
