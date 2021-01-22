package org.openbot.env;

import android.content.Context;

import com.google.android.gms.nearby.connection.PayloadCallback;

import java.util.concurrent.Callable;

public interface ILocalConnection {
    void setDataCallback(IDataReceived dataCallback);

    void connect(Context context);

    void disconnect(Context context);

    boolean isConnected();

    void sendMessage(String message);
}
