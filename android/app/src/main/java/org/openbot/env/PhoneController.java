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
    private static PhoneController _phoneController;

    final ILocalConnection connection =
            // new NearbyConnection();
            new NetworkServiceConnection();

    private RtspServer rtspServer = new RtspServer();
    private Context context;

    {
        connection.setDataCallback(new DataReceived());
        handleBotEvents();
    }

    public void setView(AutoFitSurfaceView videoWondow) {
        rtspServer.setView(videoWondow);
    }

    public void startVideo() {
        if (rtspServer.isRunning()) {
            rtspServer.sendConnectionParams();
        }
    }

    public void stopVideo() {
        rtspServer.sendVideoStoppedStatus();
    }

    class DataReceived implements IDataReceived {
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
        if (!connection.isConnected()) {
            connection.init(context);
            connection.connect(context);
        } else {
            connection.start();
        }
    }

    public void disconnect(Context context) {
        connection.stop();
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

  private PhoneController(){
    if (_phoneController != null){
      throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
    }
  }

    public static PhoneController getInstance() {
        if (_phoneController == null) { //Check for the first time

            synchronized (PhoneController.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (_phoneController == null) _phoneController = new PhoneController();
            }
        }

        return _phoneController;
    }
}
