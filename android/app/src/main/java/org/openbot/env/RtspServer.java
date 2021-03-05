package org.openbot.env;

import static org.openbot.common.Utils.getIPAddress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ToneGenerator;
import android.util.Log;
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
import org.openbot.common.Utils;
import org.openbot.customview.AutoFitSurfaceView;

class RtspServer implements IVideoServer {
  private final String TAG = "RtspServer";
  private AutoFitSurfaceView serfaceView;
  private Session session;
  private Context context;

  private final int WIDTH = 320;
  private final int HEIGHT = 240;
  private final int PORT = 1234;

  public RtspServer() {}

  private void startServer(int width, int height, int port) {}

  /*
   * <ul><li>rtsp://xxx.xxx.xxx.xxx:8086?h264&flash=on</li>
   * <li>rtsp://xxx.xxx.xxx.xxx:8086?h263&camera=front&flash=on</li>
   * <li>rtsp://xxx.xxx.xxx.xxx:8086?h264=200-20-320-240</li>
   * <li>rtsp://xxx.xxx.xxx.xxx:8086?aac</li></ul>
   */

  public void startServer(Context context, SurfaceView surfaceView) {
    // default use port 1234
    this.session =
        SessionBuilder.getInstance()
            .setCallback(this)
            .setSurfaceView(surfaceView)
            .setPreviewOrientation(90)
            .setContext(context)
            .setAudioEncoder(SessionBuilder.AUDIO_AAC)
            .setAudioQuality(new AudioQuality(16000, 32000))
            .setVideoEncoder(SessionBuilder.VIDEO_H264)
            .setVideoQuality(new VideoQuality(WIDTH, HEIGHT, 20, 500000))
            .build();

    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    editor.putString(net.majorkernelpanic.streaming.rtsp.RtspServer.KEY_PORT, String.valueOf(PORT));
    editor.apply();

    // session.getVideoTrack().setStreamingMethod(MediaStream.MODE_MEDIACODEC_API_2);

    serfaceView.getHolder().addCallback(this);
    session.configure();
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

  public void startPreview() {
    session.startPreview();
  }

  @Override
  public void surfaceCreated(@NonNull SurfaceHolder holder) {
    session.startPreview();
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
    if (session != null) {
      session.stop();
      session.release();
      session = null;
    }
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

  @Override
  public void onBitrateUpdate(long l) {}

  @Override
  public void onSessionError(int i, int i1, Exception e) {}

  @Override
  public void onPreviewStarted() {}

  @Override
  public void onSessionConfigured() {
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

  private void switchCamera() {
    session.switchCamera();
  }

  private void toggleFlash() {
    session.toggleFlash();
  }
}
