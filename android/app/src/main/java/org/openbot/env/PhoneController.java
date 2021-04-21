package org.openbot.env;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.openbot.customview.AutoFitSurfaceGlView;
import org.openbot.customview.AutoFitSurfaceView;
import org.openbot.customview.AutoFitTextureView;
import org.openbot.utils.Constants;
import org.openbot.utils.Enums;
import org.openbot.utils.Utils;

import timber.log.Timber;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PhoneController {
    private static final String TAG = "PhoneController";
    private static PhoneController _phoneController;
    private final AndGate andGate = new AndGate();

    final ILocalConnection connection = new ConnectionManager().getConnection();

    private final IVideoServer videoServer = new RtspServerPedro();
    private Context context;

    {
        connection.setDataCallback(new DataReceived());
        handleBotEvents();
        handlePhoneControllerEvents();
    }

    public void setView(AutoFitSurfaceView videoWindow) {
        if (connection.isVideoCapable()) {
            videoServer.setView(videoWindow);
        }
    }

    public void setView(AutoFitSurfaceGlView videoWindow) {
        if (connection.isVideoCapable()) {
            videoServer.setView(videoWindow);
        }
    }

    public void setView(AutoFitTextureView videoWindow) {
        if (connection.isVideoCapable()) {
            videoServer.setView(videoWindow);
        }
    }

    public void startVideo() {
        videoServer.startServer();
    }
    public void stopVideo() {
        videoServer.stopServer();
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
        videoServer.init(context);
    }

    public void connect(Context context) {
        if (!connection.isConnected()) {
            connection.init(context);
            connection.connect(context);
        } else {
            connection.start();
        }
    }

    public void disconnect() {
        andGate.setConnected(false);
        connection.stop();
        videoServer.setConnected(false);
        stopVideo();
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
                        this::send, error -> Timber.d("Error occurred in BotToControllerEventBus: %s", error));
    }

    private void handlePhoneControllerEvents() {
        ControllerToBotEventBus.subscribe(
                this.getClass().getSimpleName(),
                event -> {
                    String commandType = "";
                    if (event.has("command")) {
                        commandType = event.getString("command");
                    }

                    switch (commandType) {

                        // We re connected to the controller, send back status info
                        case Constants.CMD_CONNECTED:
                            videoServer.setConnected(true);
                            andGate.setConnected(true);
                            break;

                        case Constants.CMD_DISCONNECTED:
                            break;
                    }
                },
                error -> {
                    Log.d(null, "Error occurred in ControllerToBotEventBus: " + error);
                });
    }

    private PhoneController() {
        if (_phoneController != null) {
            throw new RuntimeException(
                    "Use getInstance() method to get the single instance of this class.");
        }
    }

    public static PhoneController getInstance() {
        if (_phoneController == null) { // Check for the first time

            synchronized (PhoneController.class) { // Check for the second time.
                // if there is no instance available... create new one
                if (_phoneController == null) _phoneController = new PhoneController();
            }
        }

        return _phoneController;
    }

    private class AndGate {
        private boolean connected = false;
        private AutoFitSurfaceView view = null;

        void setConnected(boolean connected) {
            this.connected = connected;
            if (this.connected && this.view != null) {
                videoServer.setView(view);
            }
        }

        void setView(AutoFitSurfaceView view) {
            this.view = view;
            if (this.connected && this.view != null) {
                videoServer.setView(view);
            }
        }
    }
}
