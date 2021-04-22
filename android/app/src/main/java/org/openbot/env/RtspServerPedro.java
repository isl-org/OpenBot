package org.openbot.env;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.ToneGenerator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;

import com.pedro.rtplibrary.view.OpenGlView;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;
import com.pedro.rtspserver.RtspServerCamera1;

import org.openbot.customview.AutoFitSurfaceView;
import org.openbot.customview.AutoFitTextureView;
import org.openbot.utils.AndGate;
import org.openbot.utils.Utils;

import java.util.ArrayList;

import static org.openbot.utils.Utils.getIPAddress;

public class RtspServerPedro implements ConnectCheckerRtsp, SurfaceHolder.Callback, TextureView.SurfaceTextureListener, IVideoServer {
    private final String TAG = "RtspServerPedroOpenGL";
    private RtspServerCamera1 rtspServerCamera1;
    private View view;

    private AndGate andGate;
    private AndGate.Action action;

    private Context context;

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private final int PORT = 1935;

    public RtspServerPedro() {
    }

    // IVideoServer Interface
    @Override
    public void init(Context context) {
        this.context = context;

        /*
        AndGate will run 'startServer()' if all its input conditions are met.
        This is useful if we do not know the order of the updates to the conditions.
        */
        action = new AndGate.Action() {
            @Override
            public void runIt() {
                startServer();
            }
        };
        andGate = new AndGate(new ArrayList(), action);
        andGate.addCondition("connected");
        andGate.addCondition("surfaceCreated");
        andGate.addCondition("view set");
    }

    @Override
    public boolean isRunning() {
        return rtspServerCamera1 != null && rtspServerCamera1.isStreaming();
    }

    @Override
    public void startClient() {
        sendServerUrl();
        BotToControllerEventBus.emitEvent(Utils.createStatus("VIDEO_COMMAND", "START"));
    }

    @Override
    public void sendServerUrl() {
        BotToControllerEventBus.emitEvent(
                Utils.createStatus(
                        "VIDEO_SERVER_URL",
                        "rtsp://" + getIPAddress(true) + ":" + PORT
                        // "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
                ));

    }

    @Override
    public void sendVideoStoppedStatus() {
        BotToControllerEventBus.emitEvent(Utils.createStatus("VIDEO_COMMAND", "STOP"));
    }

    @Override
    public void setView(SurfaceView view) {
        this.view = view;
        ((AutoFitSurfaceView) this.view).getHolder().addCallback(this);
        andGate.update("view set", true);
    }

    @Override
    public void setView(TextureView view) {
        this.view = view;
        ((AutoFitTextureView) this.view).setSurfaceTextureListener(this);
        andGate.update("view set", true);
    }

    @Override
    public void setView(OpenGlView view) {
        this.view = view;
        ((OpenGlView) this.view).getHolder().addCallback(this);
        andGate.update("view set", true);
    }

    @Override
    public void startServer() {
        startServer(WIDTH, HEIGHT, PORT);
    }

    @Override
    public void setConnected(boolean connected) {
        andGate.update("connected", connected);
    }
    // end Interface

    // Local methods
    private void startServer(int width, int height, int port) {
        if (rtspServerCamera1 == null) {
            String viewType = this.view.getClass().getName();

            if (viewType.contains("AutoFitTextureView")) {
                rtspServerCamera1 = new RtspServerCamera1((TextureView) view, this, port);
            }
            if (viewType.contains("AutoFitSurfaceView")) {
                rtspServerCamera1 = new RtspServerCamera1((SurfaceView) view, this, port);
            }
            if (viewType.contains("AutoFitSurfaceGlView")) {
                rtspServerCamera1 = new RtspServerCamera1((OpenGlView) view, this, port);
            }
        }

        if (!rtspServerCamera1.isStreaming()) {
            if (
                    rtspServerCamera1.prepareAudio(64 * 1024, 32000, false, false, false)
                            && rtspServerCamera1.prepareVideo(width, height, 30, 1200 * 1024, 2, 0)) {

                rtspServerCamera1.startStream("");
                startClient();
            }
        }
    }

    public void stopServer() {
        try {
            if (rtspServerCamera1 != null) {
                if (rtspServerCamera1.isRecording()) {
                    rtspServerCamera1.stopRecord();
                }

                if (rtspServerCamera1.isStreaming()) {
                    rtspServerCamera1.stopStream();
                }

                rtspServerCamera1.stopPreview();
                rtspServerCamera1 = null;
            }
        } catch (Exception e) {
            Log.d(TAG, "Got error stopping server: " + e);
        }
    }

    // ConnectCheckerRtsp callbacks
    @Override
    public void onConnectionSuccessRtsp() {

        Log.i(TAG, "onConnectionSuccessRtsp");
    }

    @Override
    public void onConnectionFailedRtsp(final String reason) {
        rtspServerCamera1.stopStream();
        beep();
    }

    @Override
    public void onNewBitrateRtsp(long bitrate) {
        Log.i(TAG, "Bitrate set to " + bitrate);
    }

    @Override
    public void onDisconnectRtsp() {
        Log.i(TAG, "onDisconnectRtsp");
    }

    @Override
    public void onAuthErrorRtsp() {
        beep();
    }

    @Override
    public void onAuthSuccessRtsp() {
    }

    // SurfaceHolder.Callback callbacks
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.d(TAG, "Surface created...");
        andGate.update("surfaceCreated", true);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (rtspServerCamera1 != null) {
            rtspServerCamera1.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        andGate.update("surfaceCreated", false);
        sendVideoStoppedStatus();
        stopServer();
    }

    // SurfaceTextureListener callbacks
    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        andGate.update("surfaceCreated", true);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        andGate.update("surfaceCreated", false);
        sendVideoStoppedStatus();
        stopServer();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
    }

    // Utils
    private void beep() {
        final ToneGenerator tg = new ToneGenerator(6, 100);
        tg.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE);
    }
}