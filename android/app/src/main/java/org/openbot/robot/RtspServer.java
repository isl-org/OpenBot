package org.openbot.robot;

import android.graphics.SurfaceTexture;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pedro.rtplibrary.rtsp.RtspCamera2;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;
import com.pedro.rtspserver.RtspServerCamera1;
import com.pedro.rtspserver.RtspServerCamera2;

import org.openbot.R;
import org.openbot.customview.AutoFitTextureView;

public class RtspServer implements ConnectCheckerRtsp, TextureView.SurfaceTextureListener {
    private final String TAG = "RtspServer";
    private RtspServerCamera1 rtspServerCamera1;
    private AutoFitTextureView textureView;

    public RtspServer() {
    }

    public void startServer(int width, int height, int port) {
        if (rtspServerCamera1 == null) {
            rtspServerCamera1 = new RtspServerCamera1(textureView, this, port);
        }

        if (!rtspServerCamera1.isStreaming()) {
            if (rtspServerCamera1.prepareAudio()
                    // && rtspServerCamera1.prepareVideo()) {
                    && rtspServerCamera1.prepareVideo(width, height, 30, 2000 * 1024, 2, 90)) {
                rtspServerCamera1.startStream("");
            }
        }
    }

    public void startServer() {
        startServer(1920, 1080, 1935);
    }

    public void stopServer() {
        if (rtspServerCamera1 != null) {
            if (rtspServerCamera1.isRecording()) {
                rtspServerCamera1.stopRecord();
            }

            if (rtspServerCamera1.isStreaming()) {
                rtspServerCamera1.stopStream();
            }

            // rtspServerCamera1.stopPreview();
            rtspServerCamera1 = null;
        }
    }

    public void init() {
    }

    protected void setView(AutoFitTextureView textureView) {
        this.textureView = textureView;
    }

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
        // do nothing
    }

    @Override
    public void onDisconnectRtsp() {
        // do nothing
        beep();
    }

    @Override
    public void onAuthErrorRtsp() {

        beep();
    }

    @Override
    public void onAuthSuccessRtsp() {
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        // INZ remove later
        // this.startServer(1280, 720, 1935);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
        //textureView.setAspectRatio(width, height);
        //rtspServerCamera1.startPreview();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        try {
            if (rtspServerCamera1.isRecording()) {
                rtspServerCamera1.stopRecord();
            }
            if (rtspServerCamera1.isStreaming()) {
                rtspServerCamera1.stopStream();
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, e.toString());
        }
        rtspServerCamera1.stopPreview();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
        beep();
    }

    private void beep() {
        final ToneGenerator tg = new ToneGenerator(6, 100);
        tg.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE);
    }
}
