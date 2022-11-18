package org.openbot.env;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Base64;
import android.util.Log;
import android.util.Range;
import android.util.Size;

import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import net.majorkernelpanic.streaming.hw.EncoderDebugger;
import net.majorkernelpanic.streaming.mp4.MP4Config;
import net.majorkernelpanic.streaming.rtp.H264Packetizer;
import net.majorkernelpanic.streaming.rtp.MediaCodecInputStream;

import org.openbot.customview.AutoFitSurfaceView;
import org.openbot.utils.CameraUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@RequiresApi(Build.VERSION_CODES.R)
public class RtpClientCamera2 {
    private static final String TAG = "camera2";
    private static final Logger LOGGER = new Logger();
    private AutoFitSurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Surface mediaCodecSurface;

    // Preview Resolution
    private int previewWidth = 1280;
    private int previewHeight = 960;
    private int inferenceInputWidth = 640;
    private int inferenceInputHeight = 360;
    private static final int MINIMUM_PREVIEW_SIZE = 320;

    // Camera2 setup
    private final Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private String mCameraId;
    private int mCameraFacing = CameraCharacteristics.LENS_FACING_BACK;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    /** An additional thread for running tasks that shouldn't block the UI. */
    private HandlerThread backgroundThread;
    /** A {@link Handler} for running tasks in the background. */
    private Handler backgroundHandler;
    private ImageReader inferenceImageReader;
    private final ImageReader.OnImageAvailableListener imageListener;

    private final int mOrientation = Configuration.ORIENTATION_LANDSCAPE;
    private Range<Float> range;
    private MediaCodec mediaCodec;
    private H264Packetizer mPacketizer;
    private byte mChannelIdentifier = 0;
    private int mTTL = 64;
    private InetAddress IP = null;


    private int PORT = 8046;
    private final Context context;
    private SharedPreferences settings = null;
    private boolean streaming = false;

    public RtpClientCamera2(AutoFitSurfaceView surfaceView, ImageReader.OnImageAvailableListener imageListener) {
        Log.d(TAG, "RtpClientCamera2 constructed");
        mSurfaceView = surfaceView;
        this.imageListener = imageListener;
        context = surfaceView.getContext();
        assert context != null;
        prepareCamera(); // initialize camera manager and choose back camera as default
        createBackgroundThread();
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        createBackgroundThread();

    }

    public void setSurfaceView(AutoFitSurfaceView surfaceView) {
        mSurfaceView = surfaceView;
        mSurfaceHolder = mSurfaceView.getHolder();

    }

    private void createBackgroundThread() {
        if (backgroundThread == null) {
            backgroundThread = new HandlerThread("cameraBackground");
            backgroundThread.start();
            backgroundHandler = new Handler(backgroundThread.getLooper());
        }
    }

    private void destroyBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void chooseCamera(int selectedFacing) {
        Log.d(TAG, "chooseCamera");
        try {
            for (final String cameraId : mCameraManager.getCameraIdList()) {
                final CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);

                LOGGER.i(
                        "CAMERA ID: "
                                + cameraId
                                + " FACING: "
                                + characteristics.get(CameraCharacteristics.LENS_FACING));
                // We don't use a front facing camera in this sample.
                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing != selectedFacing) {
                    continue;
                }

                final StreamConfigurationMap map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                if (map == null) {
                    continue;
                }
                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void prepareCamera() {
        Log.d(TAG, "prepareCamera");
        if (mCameraManager == null) {
            mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        }
        chooseCamera(mCameraFacing);
    }

    public void setResolution(int width, int height) {
        Log.d(TAG, "setResolution");
        previewWidth = width;
        previewHeight = height;
    }

    public void setInferenceInputSize(int width, int height) {
        Log.d(TAG, "setInferenceInputSize");
        inferenceInputWidth = width;
        inferenceInputHeight = height;
    }

    public void setDestination(InetAddress inetAddress, int port) {
        Log.d(TAG, "setDestination");
        IP = inetAddress;
        PORT = port;
    }

    public boolean isStreaming() {
        return streaming;
    }

    private void setUpCameraOutputs(int width, int height) {
        Log.d(TAG, "setUpCameraOutputs");
        try {
            CameraCharacteristics characteristics
                    = mCameraManager.getCameraCharacteristics(mCameraId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                range = characteristics.get(CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE);
            }
            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null) {
                return;
            }
            int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            Size mPreviewSize = CameraUtils.chooseOptimalSize(
                    map.getOutputSizes(SurfaceTexture.class),
                    new Size(previewWidth, previewHeight),
                    new Size(MINIMUM_PREVIEW_SIZE, MINIMUM_PREVIEW_SIZE));
            if (sensorOrientation == mOrientation) {
                mSurfaceView.setAspectRatio(
                        mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                mSurfaceView.setAspectRatio(
                        mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        Log.d(TAG, "closeCamera");
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.abortCaptures();
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } finally {
            mCameraOpenCloseLock.release();
        }
        destroyBackgroundThread();
    }

    @SuppressLint("MissingPermission")
    public void openCamera(int width, int height) {
        Log.d(TAG, "OpenCamera");
        setUpCameraOutputs(width, height);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            Log.d(TAG, "[openCamera]");
            Log.d(TAG, "[openCamera]  cameraId:" + mCameraId);
            mCameraManager.openCamera(mCameraId, mStateCallback, backgroundHandler);
            Log.d(TAG, "OpenCamera successfully");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    public void startPreview(){
        Log.d(TAG,"[startPreview]");
        try {
            setUpCaptureRequestBuilder(mPreviewRequestBuilder);
            mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mSessionCaptureCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder){
        Log.d(TAG, "setUpCaptureRequestBuilder");
        builder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        builder.set(CaptureRequest.CONTROL_ZOOM_RATIO, range.getLower()); //zoom out to get the largest field of view
    }

    public void start() {
        Log.d(TAG, "started");
        openCamera(mSurfaceView.getWidth(), mSurfaceView.getHeight());
    }

    public void startStream(){
        Log.d(TAG, "startStream");
        mPacketizer.setInputStream(new MediaCodecInputStream(mediaCodec));
        mPacketizer.start();
        streaming = true;
    }

    public void stopStream() {
        Log.d(TAG, "stopStream");
        if (streaming) {
            streaming = false;
        }
        mPacketizer.stop();
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }
    }

    public void configureEncoder() throws IOException {
        Log.d(TAG, "configureEncoder");
        EncoderDebugger debugger = EncoderDebugger.debug(settings, previewWidth, previewHeight);
        mPacketizer = new H264Packetizer();
        mPacketizer.setDestination(IP, PORT, 19401);
        mPacketizer.getRtpSocket().setOutputStream(null, mChannelIdentifier);
        mPacketizer.setTimeToLive(mTTL);
        MP4Config mConfig = new MP4Config(debugger.getB64SPS(), debugger.getB64PPS());
        byte[] pps = Base64.decode(mConfig.getB64PPS(), Base64.NO_WRAP);
        byte[] sps = Base64.decode(mConfig.getB64SPS(), Base64.NO_WRAP);
        mPacketizer.setStreamParameters(pps, sps);

        if (IP==null)
            throw new IllegalStateException("No destination ip address set for the stream !");

        if (PORT <= 0)
            throw new IllegalStateException("No destination ports set for the stream !");
        Log.d("Stream", "Started");

        mediaCodec = MediaCodec.createByCodecName(debugger.getEncoderName());
        MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", previewWidth, previewHeight);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2000000);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mediaCodecSurface = mediaCodec.createInputSurface();
        mediaCodec.start();
    }

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            Log.d(TAG,"[open camera onOpened]");
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            try {
                try {
                    configureEncoder();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mPreviewRequestBuilder.addTarget(mediaCodecSurface);

                inferenceImageReader = ImageReader.newInstance(
                        inferenceInputWidth, inferenceInputHeight, ImageFormat.YUV_420_888, 1);
                inferenceImageReader.setOnImageAvailableListener(imageListener, backgroundHandler);
                mPreviewRequestBuilder.addTarget(inferenceImageReader.getSurface());
                List<Surface> mSurfaces = Arrays.asList(mediaCodecSurface, inferenceImageReader.getSurface());
                cameraDevice.createCaptureSession(mSurfaces, mSessionStateCallback, backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            Log.d(TAG,"[open camera onDisconnected]");
            mCameraOpenCloseLock.release();
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            Log.d(TAG,"[open camera onError]");
            mCameraOpenCloseLock.release();
        }
    };

    private final CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            Log.d(TAG,"[captureSession onConfigured]");
            mCaptureSession = session;
            startPreview();
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {}
    };

    private final CameraCaptureSession.CaptureCallback mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }

        @Override
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }
    };










































}
