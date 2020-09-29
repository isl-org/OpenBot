// Created by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.openbot.env.Logger;

public class SensorService extends Service implements SensorEventListener {
  private SensorManager sensorManager;
  private Sensor mAccelerometer;
  private Sensor mGyroscope;
  private Sensor mGravity;
  private Sensor mMagnetic;
  private Sensor mLight;
  private Sensor mProximity;
  private Sensor mPressure;
  private Sensor mPose;
  private Sensor mMotion;
  private Sensor mStationary;

  private BufferedWriter mAccelerometerLog;
  private BufferedWriter mGyroscopeLog;
  private BufferedWriter mGravityLog;
  private BufferedWriter mMagneticLog;
  private BufferedWriter mLightLog;
  private BufferedWriter mProximityLog;
  private BufferedWriter mPressureLog;
  private BufferedWriter mPoseLog;
  private BufferedWriter mMotionLog;
  private BufferedWriter mGpsLog;
  private BufferedWriter mFrameLog;
  private BufferedWriter mInferenceLog;
  private BufferedWriter mCtrlLog;
  private BufferedWriter mIndicatorLog;
  private BufferedWriter mVehicleLog;

  private boolean mTrackingLocation = false;
  private FusedLocationProviderClient mFusedLocationClient;
  private LocationCallback mLocationCallback;

  // Message Types
  public static final int MSG_FRAME = 0;
  public static final int MSG_INFERENCE = 1;
  public static final int MSG_CONTROL = 2;
  public static final int MSG_INDICATOR = 3;

  private static final Logger LOGGER = new Logger();
  Messenger mMessenger = new Messenger(new SensorMessageHandler());
  private LocalBroadcastManager mLocalBroadcastManager;
  private BroadcastReceiver mLocalBroadcastReceiver;
  public static final String USB_ACTION_DATA_RECEIVED = "usb.data_received";
  public static final String USB_ACTION_CONNECTION_ESTABLISHED = "usb.connection_established";
  public static final String USB_ACTION_CONNECTION_CLOSED = "usb.connection_closed";

  @Override
  public final void onCreate() {
    super.onCreate();
    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    mGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    mMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    mProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
    mPose = sensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF);
    mMotion = sensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);
    mStationary = sensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT);
    // Initialize the FusedLocationClient.
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Bundle extras = intent.getExtras();
    String logFolder;
    if (extras == null) {
      logFolder =
          Environment.getExternalStorageDirectory().getAbsolutePath()
              + File.separator
              + getString(R.string.app_name);
    } else {
      logFolder = (String) extras.get("logFolder");
    }

    sensorManager.registerListener(
        this, mAccelerometer, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(
        this, mGyroscope, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(
        this, mGravity, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(
        this, mMagnetic, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(
        this, mLight, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(
        this, mProximity, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(
        this, mPressure, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(
        this, mPose, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(
        this, mMotion, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(
        this, mStationary, android.hardware.SensorManager.SENSOR_DELAY_FASTEST);

    mAccelerometerLog = openLog(logFolder, "accelerometerLog.txt");
    // appendLog(mAccelerometerLog, mAccelerometer.getName());
    appendLog(mAccelerometerLog, "timestamp[ns],x[m/s^2],y[m/s^2],z[m/s^2]");

    mGyroscopeLog = openLog(logFolder, "gyroscopeLog.txt");
    // appendLog(mGyroscopeLog, mGyroscope.getName());
    appendLog(mGyroscopeLog, "timestamp[ns],x[rad/s],y[rad/s],z[rad/s]");

    mGravityLog = openLog(logFolder, "gravityLog.txt");
    // appendLog(mGravityLog, mGravity.getName());
    appendLog(mGravityLog, "timestamp[ns],x[m/s^2],y[m/s^2],z[m/s^2]");

    mMagneticLog = openLog(logFolder, "magneticLog.txt");
    // appendLog(mMagneticLog, mMagnetic.getName());
    appendLog(mMagneticLog, "timestamp[ns],x[uT],y[uT],z[uT]");

    mLightLog = openLog(logFolder, "lightLog.txt");
    // appendLog(mLightLog, mLight.getName());
    appendLog(mLightLog, "timestamp[ns],light[lux]");

    mProximityLog = openLog(logFolder, "proximityLog.txt");
    // appendLog(mProximityLog, mProximity.getName());
    appendLog(mProximityLog, "timestamp[ns],proximity[cm]");

    mPressureLog = openLog(logFolder, "pressureLog.txt");
    // appendLog(mPressureLog, mPressure.getName());
    appendLog(mPressureLog, "timestamp[ns],pressure[hPa]");

    mPoseLog = openLog(logFolder, "poseLog.txt");
    // appendLog(mPoseLog, mPose.getName());
    appendLog(mPoseLog, "timestamp[ns],x,y,z,w,x,y,z,dx,dy,dz,dw,dx,dy,dz,id");

    mMotionLog = openLog(logFolder, "motionLog.txt");
    // appendLog(mMotionLog, mMotion.getName());
    appendLog(mMotionLog, "timestamp[ns],motion");

    mGpsLog = openLog(logFolder, "gpsLog.txt");
    appendLog(mGpsLog, "timestamp[ns],latitude,longitude,altitude[m],bearing,speed[m/s]");

    mFrameLog = openLog(logFolder, "rgbFrames.txt");
    appendLog(mFrameLog, "timestamp[ns],frame");

    mInferenceLog = openLog(logFolder, "inferenceTime.txt");
    appendLog(mInferenceLog, "frame, inferenceTime [ns]");

    mCtrlLog = openLog(logFolder, "ctrlLog.txt");
    appendLog(mCtrlLog, "timestamp[ns],leftCtrl,rightCtrl");

    mIndicatorLog = openLog(logFolder, "indicatorLog.txt");
    appendLog(mIndicatorLog, "timestamp[ns],signal");

    mVehicleLog = openLog(logFolder, "vehicleLog.txt");
    appendLog(mVehicleLog, "timestamp[ns],batteryVoltage,leftWheel,rightWheel,obstacle");

    mLocationCallback =
        new LocationCallback() {
          @Override
          public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            if (location != null) {
              appendLog(
                  mGpsLog,
                  location.getElapsedRealtimeNanos()
                      + ","
                      + location.getLatitude()
                      + ","
                      + location.getLongitude()
                      + ","
                      + location.getAltitude()
                      + ","
                      + location.getBearing()
                      + ","
                      + location.getSpeed());
            }
          }
        };

    startTrackingLocation();

    mLocalBroadcastReceiver =
        new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action != null) {

              switch (action) {
                case USB_ACTION_CONNECTION_ESTABLISHED:
                  break;

                case USB_ACTION_CONNECTION_CLOSED:
                  break;

                case USB_ACTION_DATA_RECEIVED:
                  long timestamp = SystemClock.elapsedRealtimeNanos();
                  String data = intent.getStringExtra("data");
                  // Data has the following form: voltage, lWheel, rWheel, obstacle
                  appendLog(mVehicleLog, timestamp + "," + data);
                  break;
              }
            }
          }
        };
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction(USB_ACTION_CONNECTION_ESTABLISHED);
    localIntentFilter.addAction(USB_ACTION_CONNECTION_CLOSED);
    localIntentFilter.addAction(USB_ACTION_DATA_RECEIVED);
    mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    mLocalBroadcastManager.registerReceiver(mLocalBroadcastReceiver, localIntentFilter);

    return START_REDELIVER_INTENT;
  }

  @Override
  public IBinder onBind(Intent intent) {
    // We don't provide binding, so return null
    // return null;
    return mMessenger.getBinder();
  }

  @Override
  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Do something here if sensor accuracy changes.
  }

  @Override
  public final void onSensorChanged(SensorEvent event) {
    // The light sensor returns a single value.
    // Many sensors return 3 values, one for each axis.
    String sensorName = event.sensor.getName();
    // Do something with this sensor value.
    switch (event.sensor.getType()) {
      case Sensor.TYPE_ACCELEROMETER:
        // Acceleration including gravity along the X, Y and Z axis
        // Units are m/s^2
        appendLog(
            mAccelerometerLog,
            event.timestamp
                + ","
                + event.values[0]
                + ","
                + event.values[1]
                + ","
                + event.values[2]);
        break;
      case Sensor.TYPE_GYROSCOPE:
        // Angular speed around the device's local X, Y and Z axis
        // Units are radians/second
        // The coordinate system is the same as is used by the acceleration sensor
        appendLog(
            mGyroscopeLog,
            event.timestamp
                + ","
                + event.values[0]
                + ","
                + event.values[1]
                + ","
                + event.values[2]);
        break;
      case Sensor.TYPE_GRAVITY:
        // A three dimensional vector indicating the direction and magnitude of gravity
        // Units are m/s^2
        // The coordinate system is the same as is used by the acceleration sensor
        appendLog(
            mGravityLog,
            event.timestamp
                + ","
                + event.values[0]
                + ","
                + event.values[1]
                + ","
                + event.values[2]);
        break;
      case Sensor.TYPE_MAGNETIC_FIELD:
        // Ambient magnetic field in the X, Y and Z axis in micro-Tesla (uT).
        appendLog(
            mMagneticLog,
            event.timestamp
                + ","
                + event.values[0]
                + ","
                + event.values[1]
                + ","
                + event.values[2]);
        break;
      case Sensor.TYPE_LIGHT:
        // Ambient light level in SI lux units
        appendLog(mLightLog, event.timestamp + "," + event.values[0]);
        break;
      case Sensor.TYPE_PROXIMITY:
        // Proximity sensor distance measured in centimeters
        appendLog(mProximityLog, event.timestamp + "," + event.values[0]);
        break;
      case Sensor.TYPE_PRESSURE:
        // Atmospheric pressure in mPa (millibar)
        appendLog(mPressureLog, event.timestamp + "," + event.values[0]);
        break;
      case Sensor.TYPE_POSE_6DOF:
        // values[0]: x*sin(θ/2)
        // values[1]: y*sin(θ/2)
        // values[2]: z*sin(θ/2)
        // values[3]: cos(θ/2)
        // values[4]: Translation along x axis from an arbitrary origin.
        // values[5]: Translation along y axis from an arbitrary origin.
        // values[6]: Translation along z axis from an arbitrary origin.
        // values[7]: Delta quaternion rotation x*sin(θ/2)
        // values[8]: Delta quaternion rotation y*sin(θ/2)
        // values[9]: Delta quaternion rotation z*sin(θ/2)
        // values[10]: Delta quaternion rotation cos(θ/2)
        // values[11]: Delta translation along x axis.
        // values[12]: Delta translation along y axis.
        // values[13]: Delta translation along z axis.
        // values[14]: Sequence number
        appendLog(
            mPoseLog,
            event.timestamp
                + ","
                + event.values[0]
                + ","
                + event.values[1]
                + ","
                + event.values[2]
                + ","
                + event.values[3]
                + ","
                + event.values[4]
                + ","
                + event.values[5]
                + ","
                + event.values[6]
                + ","
                + event.values[7]
                + ","
                + event.values[8]
                + ","
                + event.values[9]
                + ","
                + event.values[10]
                + ","
                + event.values[11]
                + ","
                + event.values[12]
                + ","
                + event.values[13]
                + ","
                + event.values[14]);
        break;
      case Sensor.TYPE_MOTION_DETECT:
        appendLog(mMotionLog, event.timestamp + "," + event.values[0]);
        break;
      case Sensor.TYPE_STATIONARY_DETECT:
        appendLog(mMotionLog, event.timestamp + "," + (-1) * event.values[0]);
        break;
      default:
        // Unknown sensor
        break;
    }
  }

  private class SensorMessageHandler extends android.os.Handler {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == MSG_FRAME) {
        long frameNumber = msg.getData().getLong("frameNumber");
        long timestamp = msg.getData().getLong("timestamp");
        appendLog(mFrameLog, timestamp + "," + frameNumber);
      } else if (msg.what == MSG_INFERENCE) {
        long frameNumber = msg.getData().getLong("frameNumber");
        long inferenceTime = msg.getData().getLong("inferenceTime");
        appendLog(mInferenceLog, frameNumber + "," + inferenceTime);
      } else if (msg.what == MSG_CONTROL) {
        // msg.arg1 and msg.arg2 contain left and right control signals respectively
        appendLog(mCtrlLog, SystemClock.elapsedRealtimeNanos() + "," + msg.arg1 + "," + msg.arg2);
      } else if (msg.what == MSG_INDICATOR) {
        // msg.arg1 contains indicator signal
        appendLog(mIndicatorLog, SystemClock.elapsedRealtimeNanos() + "," + msg.arg1);
      }
    }
  }

  @Override
  public void onDestroy() {
    sensorManager.unregisterListener(this);
    stopTrackingLocation();
    if (mLocalBroadcastManager != null) {
      mLocalBroadcastManager.unregisterReceiver(mLocalBroadcastReceiver);
      mLocalBroadcastManager = null;
    }
    if (mLocalBroadcastReceiver != null) mLocalBroadcastReceiver = null;
    if (mAccelerometerLog != null) closeLog(mAccelerometerLog);
    if (mGyroscopeLog != null) closeLog(mGyroscopeLog);
    if (mGravityLog != null) closeLog(mGravityLog);
    if (mMagneticLog != null) closeLog(mMagneticLog);
    if (mLightLog != null) closeLog(mLightLog);
    if (mProximityLog != null) closeLog(mProximityLog);
    if (mPressureLog != null) closeLog(mPressureLog);
    if (mPoseLog != null) closeLog(mPoseLog);
    if (mMotionLog != null) closeLog(mMotionLog);
    if (mGpsLog != null) closeLog(mGpsLog);
    if (mFrameLog != null) closeLog(mFrameLog);
    if (mInferenceLog != null) closeLog(mInferenceLog);
    if (mCtrlLog != null) closeLog(mCtrlLog);
    if (mIndicatorLog != null) closeLog(mIndicatorLog);
    if (mVehicleLog != null) closeLog(mVehicleLog);
  }

  public BufferedWriter openLog(String path, String filename) {
    LOGGER.i("Opening log file: " + filename);
    final File myDir = new File(path);

    if (!myDir.exists()) {
      if (!myDir.mkdirs()) {
        LOGGER.i("Make dir failed");
      }
    }

    final File file = new File(myDir, filename);

    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    try {
      // BufferedWriter for performance, true to set append to file flag
      return new BufferedWriter(new FileWriter(file, true));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  };

  public void appendLog(BufferedWriter writer, String text) {
    try {
      writer.append(text);
      writer.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  };

  public void closeLog(BufferedWriter writer) {
    try {
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  };

  private void startTrackingLocation() {
    try {
      mFusedLocationClient.requestLocationUpdates(
          getLocationRequest(), mLocationCallback, null /* Looper */);
      mTrackingLocation = true;
    } catch (SecurityException e) {
      mTrackingLocation = false;
      throw new SecurityException("No permission to use location.", e);
    }
  }

  /**
   * Method that stops tracking the device. It removes the location updates, stops the animation and
   * reset the UI.
   */
  private void stopTrackingLocation() {
    if (mTrackingLocation) {
      mTrackingLocation = false;
      mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
  }

  private LocationRequest getLocationRequest() {
    LocationRequest locationRequest = new LocationRequest();
    locationRequest.setInterval(1000);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    return locationRequest;
  }
}
