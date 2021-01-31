package org.openbot.env;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.media.ToneGenerator;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.abemart.wroup.common.WiFiDirectBroadcastReceiver;
import com.abemart.wroup.common.WiFiP2PError;
import com.abemart.wroup.common.WiFiP2PInstance;
import com.abemart.wroup.common.WroupDevice;
import com.abemart.wroup.common.listeners.ClientConnectedListener;
import com.abemart.wroup.common.listeners.ClientDisconnectedListener;
import com.abemart.wroup.common.listeners.DataReceivedListener;
import com.abemart.wroup.common.listeners.ServiceRegisteredListener;
import com.abemart.wroup.common.messages.MessageWrapper;
import com.abemart.wroup.service.WroupService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluethootConnection implements ILocalConnection {

    // This is a better starting point
    // https://github.com/bauerjj/Android-Simple-Bluetooth-Example

    private static final String TAG = "BluethootConnection";

    private boolean isConnected = false;
    private IDataReceived dataReceivedCallback;

    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    final byte delimiter = 33;
    int readBufferPosition = 0;

    BluethootConnection() {
        new Thread(new Runnable() {

            public void run() {
                startReceiver();
            }
        }).start();
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void setDataCallback(IDataReceived dataCallback) {
        this.dataReceivedCallback = dataCallback;
    }

    @Override
    public void connect(Context context) {
    }

    @Override
    public void disconnect(Context context) {
    }

    @Override
    public boolean isConnected() {
        return this.isConnected;
    }

    private void startReceiver() {
        int bytesAvailable;

        sendMessage ("hi");

        while (true) {
            try {
                final InputStream mmInputStream;
                mmInputStream = mmSocket.getInputStream();
                bytesAvailable = mmInputStream.available();
                if (bytesAvailable > 0) {

                    byte[] packetBytes = new byte[bytesAvailable];
                    Log.e("Aquarium recv bt", "bytes available");
                    byte[] readBuffer = new byte[1024];
                    mmInputStream.read(packetBytes);

                    for (int i = 0; i < bytesAvailable; i++) {
                        byte b = packetBytes[i];
                        if (b == delimiter) {
                            byte[] encodedBytes = new byte[readBufferPosition];
                            System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            final String data = new String(encodedBytes, "US-ASCII");
                            readBufferPosition = 0;

                            //The variable data now contains our full command
                            // inform data received...
                            dataReceivedCallback.dataReceived(data);
                            break;
                        } else {
                            readBuffer[readBufferPosition++] = b;
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public void sendMessage(String message) {

        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        try {

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mmSocket.isConnected()) {
                mmSocket.connect();
            }

            String msg = message;
            //msg += "\n";
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
