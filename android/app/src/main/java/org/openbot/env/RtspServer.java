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
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.pedro.rtplibrary.view.OpenGlView;
import com.pedro.rtplibrary.view.OpenGlView;

import java.util.ArrayList;
import java.util.List;
import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.video.VideoQuality;
import net.majorkernelpanic.streaming.video.VideoStream;

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
                .setVideoQuality(new VideoQuality(WIDTH,HEIGHT,20,2000000))
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
        ));
  }

  @Override
  public void sendVideoStoppedStatus() {
    BotToControllerEventBus.emitEvent(Utils.createStatus("VIDEO_COMMAND", "STOP"));
  }

  @Override
  public void setView(android.view.SurfaceView view) {
    if (this.serfaceView != null)
      this.serfaceView.setVisibility(View.GONE);

    this.serfaceView = (AutoFitSurfaceView) view;
    startServer(this.context, this.serfaceView);
  }

  @Override
  public void setView(TextureView view) {

  }

  @Override
  public void setView(OpenGlView view) {

  }

  private void beep() {
    final ToneGenerator tg = new ToneGenerator(6, 100);
    tg.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE);
  }
}
