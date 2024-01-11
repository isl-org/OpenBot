package org.openbot.rl;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import org.openbot.R;
import org.openbot.env.Logger;
import org.openbot.logging.SensorService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SensorServiceRL extends Service {

    private BufferedWriter frameLog;
    private BufferedWriter ctrlLog;
    private BufferedWriter infoLog;

    private boolean hasStarted = false;

    public static final int MSG_FRAME = 0;
    public static final int MSG_CONTROL = 1;
    public static final int MSG_INFO = 2;

    private static final Logger LOGGER = new Logger();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String logFolder;
        String rewardFolder;
        if (extras == null) {
            logFolder =
                    Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator
                            + getString(R.string.app_name);
        } else {
            logFolder = (String) extras.get("logFolder");

        }

        frameLog = openLog(logFolder, "rgbFrames.txt");
        appendLog(frameLog, "timestamp[ns],frame");

        ctrlLog = openLog(logFolder, "ctrlLog.txt");
        appendLog(ctrlLog, "timestamp[ns],leftCtrl,rightCtrl");

        infoLog = openLog(logFolder, "info.txt");
        appendLog(infoLog, "timestamp[ns], actions, rewards, done");

        hasStarted = true;

        return START_REDELIVER_INTENT;
    }

    private class SensorMessageHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            if (hasStarted) {
                if (msg.what == MSG_FRAME) {
                    long frameNumber = msg.getData().getLong("frameNumber");
                    long timestamp = msg.getData().getLong("timestamp");
                    if (frameLog != null) appendLog(frameLog, timestamp + "," + frameNumber);
                } else if (msg.what == MSG_CONTROL) {
                    // msg.arg1 and msg.arg2 contain left and right control signals respectively
                    if (ctrlLog != null)
                        appendLog(
                                ctrlLog, SystemClock.elapsedRealtimeNanos() + "," + msg.arg1 + "," + msg.arg2);
                } else if (msg.what == MSG_INFO) {
                    long[] info = msg.getData().getLongArray("info");
                    long timestamp = msg.getData().getLong("timestamp");
                    if (infoLog != null) appendLog(infoLog, timestamp + "," + info);
                }
            }
        }
    }

    public void onDestroy() {
        hasStarted = false;

        if (frameLog != null) closeLog(frameLog);
        if (ctrlLog != null) closeLog(ctrlLog);
        if (infoLog != null) closeLog(infoLog);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
    }

    public void appendLog(BufferedWriter writer, String text) {
        try {
            writer.append(text);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeLog(BufferedWriter writer) {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
