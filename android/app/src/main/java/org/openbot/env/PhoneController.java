package org.openbot.env;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.json.JSONObject;
import org.openbot.R;
import org.openbot.customview.AutoFitSurfaceGlView;
import org.openbot.customview.WebRTCSurfaceView;
import org.openbot.utils.CameraUtils;

import timber.log.Timber;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PhoneController {
  private static final String TAG = "PhoneController";
  private static PhoneController _phoneController;
  private ConnectionSelector connectionSelector;
  private IVideoServer videoServer;
  private View view = null;

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

  class DataReceived implements IDataReceived {
    @Override
    public void dataReceived(String commandStr) {
      ControllerToBotEventBus.emitEvent(commandStr);
    }
  }

  private void init(Context context) {
    Log.d(TAG, "initialized");
    ControllerConfig.getInstance().init(context);

    switch (ControllerConfig.getInstance().getVideoServerType()) {
      case "RTP":
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          videoServer = new RtpServer();
        }
        break;
      case "RTSP":
        videoServer = new RtspServer();
        break;
      case "WEBRTC":
        videoServer = new WebRtcServer();
    }

    videoServer.init(context);
    videoServer.setCanStart(true);

    //Video Stream port
    String[] videoServerAddress = ControllerConfig.getInstance().getVideoServerAddress();
    videoServer.setServerAddress(videoServerAddress[0], videoServerAddress[1]);

    //Controller port
    this.connectionSelector = ConnectionSelector.getInstance(context);
    connectionSelector.getConnection().setServerAddress(videoServerAddress[0], videoServerAddress[2]);
    connectionSelector.getConnection().setDataCallback(new DataReceived());

    //1280 x 960 is the best resolution that is desirable for both video streaming quality and efficiency
    Size resolution =
        CameraUtils.getClosestCameraResolution(context, new Size(1280, 960));
    videoServer.setResolution(resolution.getWidth(), resolution.getHeight());

    handleBotEvents();
    createAndSetView(context);
    monitorConnection();
  }

  private void createAndSetView(Context context) {
    if (videoServer instanceof WebRtcServer) {
      view = new org.openbot.customview.WebRTCSurfaceView(context);
    } else if (videoServer instanceof RtspServer) {
      view = new org.openbot.customview.AutoFitSurfaceGlView(context);
    } else if (videoServer instanceof RtpServer) {
      view = new org.openbot.customview.AutoFitSurfaceView(context);
    }
    if (view != null) {
      addVideoView(view, context);
    }
  }

  private void addVideoView(View videoView, Context context) {
    ViewGroup viewGroup = (ViewGroup) ((Activity) context).getWindow().getDecorView();

    ViewGroup.LayoutParams layoutParams =
        new ViewGroup.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    videoView.setLayoutParams(layoutParams);
    videoView.setId(R.id.video_window);
    videoView.setAlpha(0f);
    viewGroup.addView(videoView, 0); // send to back

    if (videoView instanceof WebRTCSurfaceView) {
      videoServer.setView((WebRTCSurfaceView) videoView);
    } else if (videoView instanceof AutoFitSurfaceGlView) {
      videoServer.setView((AutoFitSurfaceGlView) videoView);
    } else if (videoView instanceof SurfaceView) {
      videoServer.setView((SurfaceView) videoView);
    }
  }

  public void connect(Context context) {
    ILocalConnection connection = connectionSelector.getConnection();

    if (!connection.isConnected()) {
      connection.init(context);
      connection.connect(context);
    } else {
      connection.start();
    }
  }

  public void disconnect() {
    connectionSelector.getConnection().stop();
  }

  public void send(JSONObject info) {
    connectionSelector.getConnection().sendMessage(info.toString());
  }

  public boolean isConnected() {
    return connectionSelector.getConnection().isConnected();
  }

  private void handleBotEvents() {
    BotToControllerEventBus.subscribe(
        this::send, error -> Timber.d("Error occurred in BotToControllerEventBus: %s", error));
  }

  private void monitorConnection() {
    ControllerToBotEventBus.subscribe(
        this.getClass().getSimpleName(),
        event -> {
          switch (event.getString("command")) {
            case "CONNECTED":
              videoServer.setConnected(true);
              break;

            case "DISCONNECTED":
              videoServer.setConnected(false);
              break;
          }
        },
        error -> {
          Log.d(null, "Error occurred in monitorConnection: " + error);
        },
        event ->
            event.has("command")
                && ("CONNECTED".equals(event.getString("command"))
                    || "DISCONNECTED".equals(event.getString("command"))) // filter everything else
        );
  }

  private PhoneController() {
    if (_phoneController != null) {
      throw new RuntimeException(
          "Use getInstance() method to get the single instance of this class.");
    }
  }
}
