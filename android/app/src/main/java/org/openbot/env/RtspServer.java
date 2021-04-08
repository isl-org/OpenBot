package org.openbot.env;

import static org.openbot.utils.Utils.getIPAddress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ToneGenerator;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;
import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import org.openbot.customview.AutoFitSurfaceView;
import org.openbot.utils.Utils;

class RtspServer implements SurfaceHolder.Callback, IVideoServer {
  private final String TAG = "RtspServer";
  private AutoFitSurfaceView serfaceView;
  private Context context;

  private final int WIDTH = 320;
  private final int HEIGHT = 240;
  private final int PORT = 1234;
  private Session session;

  public RtspServer() {}

  public void startServer(Context context, SurfaceView surfaceView) {

    // set the port to 1234
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    editor.putString(net.majorkernelpanic.streaming.rtsp.RtspServer.KEY_PORT, String.valueOf(PORT));
    editor.apply();

    this.session =
        SessionBuilder.getInstance()
            .setSurfaceView((net.majorkernelpanic.streaming.gl.SurfaceView) surfaceView)
            .setContext(context)
            .setAudioEncoder(SessionBuilder.AUDIO_AAC)
            .setVideoEncoder(SessionBuilder.VIDEO_H264)
            .build();

    serfaceView.getHolder().addCallback(this);
    context.startService(new Intent(context, net.majorkernelpanic.streaming.rtsp.RtspServer.class));
  }

  @Override
  public void stopServer() {
    session.stop();
    context.stopService(new Intent(context, net.majorkernelpanic.streaming.rtsp.RtspServer.class));
  }

  @Override
  public void init(Context context) {
    this.context = context;
  }

  @Override
  public boolean isRunning() {
    return session != null && session.isStreaming();
  }

  @Override
  public void surfaceCreated(@NonNull SurfaceHolder holder) {
    startClient();
  }

  @Override
  public void startClient() {
    sendServerUrl();
    BotToControllerEventBus.emitEvent(Utils.createStatus("VIDEO_COMMAND", "START"));
  }

  @Override
  public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    Log.d(TAG, "surfaceChanged...");
  }

  @Override
  public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    sendVideoStoppedStatus();
  }

  @Override
  public void sendServerUrl() {
    BotToControllerEventBus.emitEvent(
        Utils.createStatus(
            "VIDEO_SERVER_URL",
            "rtsp://"
                + getIPAddress(true)
                + ":"
                + PORT
                + "?trackId=0&h264=200-20-"
                + WIDTH
                + "-"
                + HEIGHT));
  }

  @Override
  public void sendVideoStoppedStatus() {
    List<Pair<String, String>> nameValues = new ArrayList<>();
    nameValues.add(new Pair<>("VIDEO_COMMAND", "STOP"));

    BotToControllerEventBus.emitEvent(Utils.createStatusBulk(nameValues));
  }

  @Override
  public void setView(android.view.SurfaceView view) {
    this.serfaceView = (AutoFitSurfaceView) view;
    startServer(this.context, this.serfaceView);
  }

  private void beep() {
    final ToneGenerator tg = new ToneGenerator(6, 100);
    tg.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE);
  }
}
