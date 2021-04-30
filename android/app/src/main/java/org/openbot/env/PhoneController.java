package org.openbot.env;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import org.openbot.customview.AutoFitSurfaceGlView;
import org.openbot.customview.AutoFitSurfaceView;
import org.openbot.customview.AutoFitTextureView;
import org.openbot.utils.CameraUtils;
import timber.log.Timber;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PhoneController {
  private static final String TAG = "PhoneController";
  private static PhoneController _phoneController;
  private ConnectionManager connectionManager;
  private final IVideoServer videoServer = new RtspServer();

  public static PhoneController getInstance(Context context) {
    if (_phoneController == null) { // Check for the first time

      synchronized (PhoneController.class) { // Check for the second time.
        // if there is no instance available... create new one
        if (_phoneController == null) _phoneController = new PhoneController();
        _phoneController.init(context);
      }
    }

    return _phoneController;
  }

  public void setView(AutoFitSurfaceView videoWindow) {
    if (connectionManager.getConnection().isVideoCapable()) {
      videoServer.setView(videoWindow);
    }
  }

  public void setView(AutoFitSurfaceGlView videoWindow) {
    if (connectionManager.getConnection().isVideoCapable()) {
      videoServer.setView(videoWindow);
    }
  }

  public void setView(AutoFitTextureView videoWindow) {
    if (connectionManager.getConnection().isVideoCapable()) {
      videoServer.setView(videoWindow);
    }
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

  private void init(Context context) {
    videoServer.init(context);
    this.connectionManager = ConnectionManager.getInstance(context);
    connectionManager.getConnection().setDataCallback(new DataReceived());
    android.util.Size resolution =
        CameraUtils.getClosestCameraResolution(context, new android.util.Size(640, 360));

    videoServer.setResolution(resolution.getWidth(), resolution.getHeight());

    handleBotEvents();
  }

  public void connect(Context context) {
    ILocalConnection connection = connectionManager.getConnection();

    if (!connection.isConnected()) {
      connection.init(context);
      connection.connect(context);
    } else {
      connection.start();
    }

    videoServer.setConnected(true);
  }

  public void disconnect() {
    connectionManager.getConnection().stop();
    videoServer.setConnected(false);
    stopVideo();
  }

  public void send(JSONObject info) {
    connectionManager.getConnection().sendMessage(info.toString());
  }

  public boolean isConnected() {
    return connectionManager.getConnection().isConnected();
  }

  private void handleBotEvents() {
    BotToControllerEventBus.getProcessor()
        .subscribe(
            this::send, error -> Timber.d("Error occurred in BotToControllerEventBus: %s", error));
  }

  private PhoneController() {
    if (_phoneController != null) {
      throw new RuntimeException(
          "Use getInstance() method to get the single instance of this class.");
    }
  }
}
