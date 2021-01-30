package org.openbot.env;

import android.content.Context;
import android.content.IntentFilter;
import android.media.ToneGenerator;
import android.net.wifi.WifiInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.util.Log;
import android.widget.Toast;

import com.abemart.wroup.client.WroupClient;
import com.abemart.wroup.common.WiFiDirectBroadcastReceiver;
import com.abemart.wroup.common.WiFiP2PError;
import com.abemart.wroup.common.WiFiP2PInstance;
import com.abemart.wroup.common.WroupDevice;
import com.abemart.wroup.common.WroupServiceDevice;
import com.abemart.wroup.common.listeners.ClientConnectedListener;
import com.abemart.wroup.common.listeners.ClientDisconnectedListener;
import com.abemart.wroup.common.listeners.DataReceivedListener;
import com.abemart.wroup.common.listeners.PeerConnectedListener;
import com.abemart.wroup.common.listeners.ServiceDiscoveredListener;
import com.abemart.wroup.common.listeners.ServiceRegisteredListener;
import com.abemart.wroup.common.messages.MessageWrapper;
import com.abemart.wroup.service.WroupService;

import org.json.JSONException;
import org.json.JSONObject;
import org.openbot.robot.CameraActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WiFiDirectConnection implements ILocalConnection {

    private static final String TAG = "WiFiDirectConnection";

    WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver;
    IntentFilter intentFilter = new IntentFilter();
    WroupService wroupService;
    private boolean isConnected = false;
    private static final String SERVICE_ID = "OPENBOT_SERVICE_ID";
    private IDataReceived dataReceivedCallback;

    @Override
    public void init (Context context) {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wiFiDirectBroadcastReceiver = WiFiP2PInstance.getInstance(context).getBroadcastReceiver();
        context.registerReceiver(wiFiDirectBroadcastReceiver, intentFilter);

        wroupService = WroupService.getInstance(context);
        wroupService.setDataReceivedListener(new DataReceiver());
        wroupService.setClientDisconnectedListener(new DisconnectionListener());
        wroupService.setClientConnectedListener(new ConnectionListener());
    }

    class DataReceiver implements DataReceivedListener {

        @Override
        public void onDataReceived(MessageWrapper messageWrapper) {
            dataReceivedCallback.dataReceived(messageWrapper.getMessage());
        }
    }

    @Override
    public void setDataCallback(IDataReceived dataCallback) {
        this.dataReceivedCallback = dataCallback;
    }

    class ConnectionListener implements ClientConnectedListener {

        @Override
        public void onClientConnected(WroupDevice wroupDevice) {
            Log.i(TAG, "onConnectionResult: connection successful");
            try {
                isConnected = true;
                ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"CONNECTED\"}"));
                beep();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class DisconnectionListener implements ClientDisconnectedListener {
        @Override
        public void onClientDisconnected(WroupDevice wroupDevice) {
            Log.i(TAG, "onDisconnected: disconnected...");
            try {
                isConnected = false;
                ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"DISCONNECTED\"}"));
                beep();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connect(Context context) {

        wroupService.registerService(SERVICE_ID, new ServiceRegisteredListener() {

            @Override
            public void onSuccessServiceRegistered() {
                Log.i(TAG, "onSuccessServiceRegistered");
            }

            @Override
            public void onErrorServiceRegistered(WiFiP2PError wiFiP2PError) {
                Log.i(TAG, "onErrorServiceRegistered");
            }
        });
    }

    @Override
    public void disconnect(Context context) {
        if (wroupService != null) {
            wroupService.disconnect();
        }
    }

    @Override
    public boolean isConnected() {
        return this.isConnected;
    }

    @Override
    public void sendMessage(String message) {
        MessageWrapper messageWrapper = new MessageWrapper();
        messageWrapper.setMessage(message);
        messageWrapper.setMessageType(MessageWrapper.MessageType.NORMAL);

        wroupService.sendMessageToAllClients(messageWrapper);
    }

    private void beep() {
        final ToneGenerator tg = new ToneGenerator(6, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
    }
}
