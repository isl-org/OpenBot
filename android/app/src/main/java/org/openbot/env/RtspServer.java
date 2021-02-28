package org.openbot.env;

import static org.openbot.common.Utils.getIPAddress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ToneGenerator;
import android.util.Pair;
import android.view.SurfaceHolder;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;
import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.video.VideoQuality;

import org.json.JSONObject;
import org.openbot.common.Utils;
import org.openbot.customview.AutoFitSurfaceView;

class RtspServer implements Session.Callback, SurfaceHolder.Callback {
  private final String TAG = "RtspServer";
  private AutoFitSurfaceView serfaceView;
  private Session session;
  private Context context;

  private final int WIDTH = 320;
  private final int HEIGHT = 240;
  private final int PORT = 1234;
  private boolean configured = false;

  public RtspServer() {}

  private void startServer(int width, int height, int port) {}

  private void startServer(Context context, SurfaceView surfaceView) {
    configured = false;

    // default use port 1234
    this.session =
        SessionBuilder.getInstance()
            .setCallback(this)
            .setSurfaceView(surfaceView)
            .setPreviewOrientation(90)
            .setContext(context)
            .setAudioEncoder(SessionBuilder.AUDIO_NONE)
            .setAudioQuality(new AudioQuality(16000, 32000))
            .setVideoEncoder(SessionBuilder.VIDEO_H264)
            .setVideoQuality(new VideoQuality(WIDTH, HEIGHT, 20, 500000))
            .build();

    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    editor.putString(net.majorkernelpanic.streaming.rtsp.RtspServer.KEY_PORT, String.valueOf(PORT));
    editor.apply();

    serfaceView.getHolder().addCallback(this);
    session.configure();
    context.startService(new Intent(context, net.majorkernelpanic.streaming.rtsp.RtspServer.class));
  }

  private void stopServer() {

    session.stop();
    context.stopService(new Intent(context, net.majorkernelpanic.streaming.rtsp.RtspServer.class));
  }

  public void init(Context context) {
    this.context = context;
  }

  protected void setView(AutoFitSurfaceView serfaceView) {

    this.serfaceView = serfaceView;
    startServer(this.context, this.serfaceView);
  }

  public boolean isRunning() {
    boolean running = session != null && session.isStreaming();
    return running;
  }

  @Override
  public void surfaceCreated(@NonNull SurfaceHolder holder) {
    session.startPreview();
    sendConnectionParams();
  }

  @Override
  public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

  @Override
  public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    if (session != null) {
      session.stop();
      session.release();
      session = null;
    }
    sendVideoStoppedStatus ();
  }

  public void sendConnectionParams() {
    List<Pair<String, String>> nameValues = new ArrayList<Pair<String, String>>();
    boolean ip_address = nameValues.add(new Pair<>("IP_ADDRESS", getIPAddress(true)));
    nameValues.add(new Pair<>("PORT", "" + PORT));
    nameValues.add(new Pair<>("EXTRA_VIDEO_PARAMS", "?h264=200-20-" + WIDTH + "-" + HEIGHT));
    nameValues.add(new Pair<>("VIDEO_COMMAND", "START"));

    BotToControllerEventBus.emitEvent(Utils.createStatusBulk(nameValues));
  }

  public void sendVideoStoppedStatus() {
    List<Pair<String, String>> nameValues = new ArrayList<Pair<String, String>>();
    nameValues.add(new Pair<>("VIDEO_COMMAND", "" + "STOP"));

    BotToControllerEventBus.emitEvent(Utils.createStatusBulk(nameValues));
  }

  @Override
  public void onBitrateUpdate(long l) {}

  @Override
  public void onSessionError(int i, int i1, Exception e) {}

  @Override
  public void onPreviewStarted() {}

  @Override
  public void onSessionConfigured() {
    this.configured = true;
    session.start();
  }

  @Override
  public void onSessionStarted() {}

  @Override
  public void onSessionStopped() {}

  private void beep() {
    final ToneGenerator tg = new ToneGenerator(6, 100);
    tg.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE);
  }
}
