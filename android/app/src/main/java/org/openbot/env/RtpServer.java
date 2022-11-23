package org.openbot.env;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.ImageReader;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import com.pedro.rtplibrary.view.OpenGlView;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.openbot.customview.AutoFitSurfaceView;
import org.openbot.utils.AndGate;
import org.openbot.utils.ConnectionUtils;
import org.webrtc.SurfaceViewRenderer;
import timber.log.Timber;

@RequiresApi(Build.VERSION_CODES.R)
public class RtpServer implements IVideoServer, SurfaceHolder.Callback {

  private static final String TAG = "RtpServer";
  private RtpClientCamera2 rtpClientCamera2;
  private View view;
  private ImageReader.OnImageAvailableListener imageListener;
  private AndGate andGate;
  private Context context;
  private Size resolution = new Size(1280, 960);
  private Size inputSizeForInference =
      new Size(1280, 720); // This has to be the same as the DESIRED_PREVIEW_SIZE in DefaultActivity
  private InetAddress IP;
  private int PORT;

  public RtpServer() {}

  @Override
  public void init(Context context) {
    Log.d(TAG, "initialized");
    this.context = context;
    imageListener = (ImageReader.OnImageAvailableListener) context;
    /*
    AndGate will run 'startServer()' if all its input conditions are met.
    This is useful if we do not know the order of the updates to the conditions.
    */
    AndGate.Action startAction = () -> startServer();
    AndGate.Action stopAction = () -> stopServer();
    andGate = new AndGate(startAction, stopAction);
    andGate.addCondition("connected");
    andGate.addCondition("server address set");
    andGate.addCondition("surfaceCreated");
    andGate.addCondition("view set");
    andGate.addCondition("camera permission");
    andGate.addCondition("resolution set");
    andGate.addCondition("can start");

    int camera = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
    andGate.set("camera permission", camera == PackageManager.PERMISSION_GRANTED);
  }

  private void stopServer() {
    // stop streaming and close camera
    try {
      if (rtpClientCamera2 != null) {
        if (rtpClientCamera2.isStreaming()) {
          rtpClientCamera2.stopStream();
        }
        rtpClientCamera2.closeCamera();
        rtpClientCamera2 = null;
      }
    } catch (Exception e) {
      Log.d(TAG, "Got error stopping server: " + e);
    }
  }

  private void startServer() {
    // create camera and start streaming here
    Log.d(TAG, "startServer");
    if (rtpClientCamera2 == null) {
      Timber.d("Resolution %dx%d", resolution.getWidth(), resolution.getHeight());
      rtpClientCamera2 = new RtpClientCamera2((AutoFitSurfaceView) view, imageListener);
      rtpClientCamera2.setResolution(resolution.getWidth(), resolution.getHeight());
      rtpClientCamera2.setInferenceInputSize(
          inputSizeForInference.getWidth(), inputSizeForInference.getHeight());
      rtpClientCamera2.setDestination(IP, PORT);
      rtpClientCamera2.start();
    }

    // Wait for the rtpClientCamera2 to finish configuration
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    Runnable action =
        () -> {
          rtpClientCamera2.startStream();
          startClient();
        };
    executorService.schedule(action, 500, TimeUnit.MILLISECONDS);
  }

  public void setServerAddress(String ip, String port) {
    Log.d(TAG, "setServerAddress: " + ip + " : " + port);
    try {
      this.IP = InetAddress.getByName(ip);
      this.PORT = Integer.parseInt(port);
      andGate.set("server address set", true);
    } catch (UnknownHostException e) {
      andGate.set("server address set", false);
      e.printStackTrace();
    }
  }

  @Override
  public void setResolution(int w, int h) {
    resolution = new Size(w, h);
    andGate.set("resolution set", true);
  }

  public void setInputSizeForInference(int w, int h) {
    inputSizeForInference = new Size(w, h);
  }

  @Override
  public void setConnected(boolean connected) {
    int camera = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
    andGate.set("camera permission", camera == PackageManager.PERMISSION_GRANTED);

    andGate.set("connected", connected);
  }

  @Override
  public boolean isRunning() {
    return rtpClientCamera2 != null && rtpClientCamera2.isStreaming();
  }

  @Override
  public void startClient() {
    BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("VIDEO_PROTOCOL", "RTP"));
    BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("VIDEO_COMMAND", "START"));
  }

  @Override
  public void sendServerUrl() {}

  @Override
  public void sendVideoStoppedStatus() {
    BotToControllerEventBus.emitEvent(ConnectionUtils.createStatus("VIDEO_COMMAND", "STOP"));
  }

  @Override
  public void setView(SurfaceView view) {
    this.view = view;
    ((AutoFitSurfaceView) this.view).getHolder().addCallback(this);
    andGate.set("view set", true);
  }

  @Override
  public void setView(TextureView view) {}

  @Override
  public void setView(SurfaceViewRenderer view) {}

  @Override
  public void setView(OpenGlView view) {}

  @Override
  public void setCanStart(boolean canStart) {
    andGate.set("can start", canStart);
  }

  @Override
  public void surfaceCreated(@NonNull SurfaceHolder holder) {
    Log.d(TAG, "Surface created...");
    andGate.set("surfaceCreated", true);
  }

  @Override
  public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    Log.d(TAG, "Surface changed...");
  }

  @Override
  public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    andGate.set("surfaceCreated", false);
    sendVideoStoppedStatus();
    // TODO: stop camera2 streaming
    andGate.set("surfaceCreated", false);
  }
}
