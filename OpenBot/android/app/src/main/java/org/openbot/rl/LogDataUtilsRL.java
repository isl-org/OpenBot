package org.openbot.rl;

import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;

import org.openbot.logging.SensorService;

public class LogDataUtilsRL {

    public static Message generateControlDataMessage(int left, int right) {
        Message msg = Message.obtain();
        msg.arg1 = left;
        msg.arg2 = right;
        msg.what = SensorService.MSG_CONTROL;
        return msg;
    }

    public static Message generateFrameNumberMessage(long frameNumber) {
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putLong("frameNumber", frameNumber);
        bundle.putLong("timestamp", SystemClock.elapsedRealtimeNanos());
        msg.setData(bundle);
        msg.what = SensorService.MSG_FRAME;
        return msg;
    }

    public static Message generateIndicatorMessage(int indicator) {
        Message msg = Message.obtain();
        msg.arg1 = indicator;
        msg.what = SensorService.MSG_INDICATOR;
        return msg;
    }

    public static Message generateVehicleDataMessage(long timestamp, String data, int type) {
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putLong("timestamp", timestamp);
        bundle.putString("data", data);
        msg.setData(bundle);
        msg.what = type;
        return msg;
    }
    public static Message generateRLMessage(long[] info){ // info should be [p_a1, p_a2, p_a3, rewards, done] with p_a... the probability for this action, might be 0 or 1 or between 0 and 1
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putLongArray("info",  info);
        bundle.putLong("timestamp", SystemClock.elapsedRealtimeNanos());
        msg.setData(bundle);
        msg.what = SensorService.MSG_REWARD;

        return msg;
    }


}
