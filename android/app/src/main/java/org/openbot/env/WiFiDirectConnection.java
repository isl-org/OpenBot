package org.openbot.env;

import android.content.Context;
import android.content.IntentFilter;
import android.media.ToneGenerator;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.abemart.wroup.client.WroupClient;
import com.abemart.wroup.common.WiFiDirectBroadcastReceiver;
import com.abemart.wroup.common.WiFiP2PError;
import com.abemart.wroup.common.WiFiP2PInstance;
import com.abemart.wroup.common.WroupDevice;
import com.abemart.wroup.common.WroupServiceDevice;
import com.abemart.wroup.common.listeners.ClientConnectedListener;
import com.abemart.wroup.common.listeners.DataReceivedListener;
import com.abemart.wroup.common.listeners.ServiceConnectedListener;
import com.abemart.wroup.common.listeners.ServiceDisconnectedListener;
import com.abemart.wroup.common.listeners.ServiceDiscoveredListener;
import com.abemart.wroup.common.messages.MessageWrapper;
import com.abemart.wroup.service.WroupService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WiFiDirectConnection implements ILocalConnection {
    private static final String TAG = "WiFiDirectConnection";

    private IntentFilter intentFilter = new IntentFilter();
    private WroupClient wroupClient;
    private final String SERVICE_ID = "OPENBOT_SERVICE_ID";
    private boolean connected = false;

    private IDataReceived dataReceivedCallback = null;
    private WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver = null;

    @Override
    public void init(Context context) {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wiFiDirectBroadcastReceiver = WiFiP2PInstance.getInstance(context).getBroadcastReceiver();
        context.registerReceiver(wiFiDirectBroadcastReceiver, intentFilter);
    }

    private void beep() {
        final ToneGenerator tg = new ToneGenerator(6, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
    }

    @Override
    public void setDataCallback(IDataReceived dataCallback) {
        dataReceivedCallback = dataCallback;
    }

    @Override
    public void connect(Context context) {
        searchAvailableGroups(context);
    }

    @Override
    public void disconnect(Context context) {
        if (wroupClient != null) {
            wroupClient.disconnect();
        }
        wroupClient = null;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void sendMessage(String message) {
        if (wroupClient != null) {
            try {
                MessageWrapper messageWrapper = new MessageWrapper();
                messageWrapper.setMessage(message);
                messageWrapper.setMessageType(MessageWrapper.MessageType.NORMAL);
                wroupClient.sendMessageToAllClients(messageWrapper);
            } catch (Throwable t) {
                Log.d(TAG, "Something went wrong while trying to send..." + t);
            }
        }
    }

    class DataReceiver implements DataReceivedListener {
        @Override
        public void onDataReceived(MessageWrapper messageWrapper) {
            dataReceivedCallback.dataReceived(messageWrapper.getMessage());
        }
    }

    class ConnectionListener implements ClientConnectedListener {

        @Override
        public void onClientConnected(WroupDevice wroupDevice) {
            connected = true;
            try {
                ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"CONNECTED\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            beep();
        }
    }

    class ServiceDisconnectionListener implements ServiceDisconnectedListener {

        @Override
        public void onServerDisconnectedListener() {
            connected = false;
            try {
                ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"DISCONNECTED\"}"));
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            beep();
        }
    }

    private void searchAvailableGroups(Context context) {

        wroupClient = WroupClient.getInstance(context.getApplicationContext());
        wroupClient.setDataReceivedListener(new DataReceiver());
        wroupClient.setClientConnectedListener(new ConnectionListener());
        wroupClient.setServerDisconnetedListener(new ServiceDisconnectionListener());

        class DiscoveryListener implements ServiceDiscoveredListener {

            @Override
            public void onNewServiceDeviceDiscovered(WroupServiceDevice serviceDevice) {
                Log.i(TAG, "New group found:");
                Log.i(TAG, "\tName: " + serviceDevice.getTxtRecordMap().get(WroupService.SERVICE_GROUP_NAME));

                class ServiceListener implements ServiceConnectedListener {
                    @Override
                    public void onServiceConnected(WroupDevice serviceDevice) {
                        connected = true;
                        try {
                            ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"DISCONNECTED\"}"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                if (SERVICE_ID.equals(serviceDevice.getTxtRecordMap().get("GROUP_NAME"))) {
                }

            }

            @Override
            public void onFinishServiceDeviceDiscovered(List<WroupServiceDevice> serviceDevices) {
                Log.i(TAG, "Found '" + serviceDevices.size() + "' groups");
                if (serviceDevices.isEmpty()) {
                    Log.i(TAG, "No groups found");

                    // maybe the controller is not up yet, keep trying
                    searchAvailableGroups(context);
                } else {
                    Log.i(TAG, "Finished discovery");

                    for (WroupServiceDevice serviceDevice : serviceDevices) {
                        String group = serviceDevice.getTxtRecordMap().get("GROUP_NAME");
                        if (SERVICE_ID.equals(group)) {

                            class ServiceConListener implements ServiceConnectedListener {
                                @Override
                                public void onServiceConnected(WroupDevice serviceDevice) {
                                    try {
                                        ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"CONNECTED\"}"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    connected = true;
                                }
                            }
                            if (wroupClient != null) {
                                wroupClient.connectToService(serviceDevice, new ServiceConListener());
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(WiFiP2PError wiFiP2PError) {
                Log.i(TAG, "onError: " + wiFiP2PError.toString());
            }
        }
        if (wroupClient != null) {
            wroupClient.discoverServices(5 * 1000L, new DiscoveryListener());
        }
    }
}