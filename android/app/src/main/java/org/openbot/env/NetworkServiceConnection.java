package org.openbot.env;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NetworkServiceConnection implements ILocalConnection {

    private static final String TAG = "NetworkServiceConn";

    private String SERVICE_NAME_CONTROLLER = "OPEN_BOT_CONTROLLER";
    private String MY_SERVICE_NAME = "OPEN_BOT";
    private String SERVICE_TYPE = "_openbot._tcp";
    private int port = 19400;

    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;
    private boolean connected = false;
    private boolean discovering = false;
    private IDataReceived dataReceivedCallback;
    private SocketHandler socketHandler;
    private BlockingQueue<String> messageQueue =
            new ArrayBlockingQueue<String>(100);

    @Override
    public void init(Context context) {
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        socketHandler = new SocketHandler(messageQueue);
    }

    @Override
    public void setDataCallback(IDataReceived dataCallback) {
        this.dataReceivedCallback = dataCallback;
    }

    @Override
    public void connect(Context context) {
        runConnection();
    }

    @Override
    public void disconnect(Context context) {

        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
        socketHandler.close();
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public void sendMessage(String message) {
        if (socketHandler != null) {
            socketHandler.put(message);
        }
    }
    // end of interface //////////////////////////////

    private void runConnection() {
        if (!discovering) {
            mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
            discovering = true;
        }
    }

    NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {
        // Called as soon as service discovery begins.
        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d(TAG, "Service discovery started");
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            // A service was found! Do something with it.
            Log.d(TAG, "Service discovery success : " + service);
            Log.d(TAG, "Host = " + service.getServiceName());
            Log.d(TAG, "port = " + String.valueOf(service.getPort()));

            if (!service.getServiceType().equals(SERVICE_TYPE)) {
                mNsdManager.resolveService(service, mResolveListener);
            } else if (service.getServiceName().equals(MY_SERVICE_NAME)) {
                Log.d(TAG, "Same machine: " + MY_SERVICE_NAME);
            } else {
                mNsdManager.resolveService(service, mResolveListener);
            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo service) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Log.e(TAG, "service lost" + service);
            try {
                ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"DISCONNECTED\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            discovering = false;
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.i(TAG, "Discovery stopped: " + serviceType);
            discovering = false;
        }

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
            discovering = false;
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
            discovering = false;
        }
    };

    NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            // Called when the resolve fails. Use the error code to debug.
            Log.e(TAG, "Resolve failed " + errorCode);
            Log.e(TAG, "serivce = " + serviceInfo);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "Resolve Succeeded. " + serviceInfo);

            if (serviceInfo.getServiceName().equals(MY_SERVICE_NAME)) {
                Log.d(TAG, "Same IP.");
                return;
            }

            // Obtain port and IP
            int port = serviceInfo.getPort();
            String host = serviceInfo.getHost().getHostAddress();
            Log.d(TAG, "PORT: " + port + ", address: " + host);

            new Thread("Receiver Thread") {
                public void run() {
                    SocketHandler.ClientInfo clientInfo = socketHandler.connect(host, port);
                    if (clientInfo == null) {
                        try {
                            ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"DISCONNECTED\"}"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    startReceiver(socketHandler, clientInfo.reader);
                    startSender(socketHandler, clientInfo.writer);
                }
            }.start();
        }
    };

    private void startReceiver(SocketHandler socketHandler, Scanner reader) {
        new Thread("startReceiver Thread") {
            public void run() {
                socketHandler.runReceiver(reader);
            }
        }.start();

    }

    private void startSender(SocketHandler socketHandler, OutputStream writer) {
        new Thread("startSender Thread") {
            public void run() {
                socketHandler.runSender(writer);
            }
        }.start();
    }

    class SocketHandler {
        private BlockingQueue<String> messageQueue;
        private Socket client;

        SocketHandler(BlockingQueue<String> messageQueue) {
            this.messageQueue = messageQueue;
        }

        class ClientInfo {
            Scanner reader;
            OutputStream writer;

            ClientInfo(Scanner reader, OutputStream writer) {
                this.reader = reader;
                this.writer = writer;
            }
        }

        ClientInfo connect(String host, int port) {
            ClientInfo clientInfo = null;

            try {
                client = new Socket(host, port);
                if (client == null) {
                    return null;
                }
                clientInfo = new ClientInfo(new Scanner (new DataInputStream(new BufferedInputStream(client.getInputStream()))), client.getOutputStream());

                ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"CONNECTED\"}"));
                connected = true;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return clientInfo;
        }

        void runReceiver(Scanner reader) {
            try {
                while (true) {
                    String msg = reader.nextLine().trim();
                    dataReceivedCallback.dataReceived(msg);
                }
            } catch (Exception e) {
                close();
            }
        }

        void put(String message) {
            try {
                this.messageQueue.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void runSender(OutputStream writer) {
            while (true) {
                try {
                    String message = messageQueue.take();
                    Log.i(TAG, "queue capacity: " + messageQueue.remainingCapacity());
                    writer.write((message+"\n").getBytes(StandardCharsets.UTF_8));
                } catch (InterruptedException | IOException e) {
                    Log.i(TAG, "runSender got exception: " + e);
                    close();
                    break;
                }
            }
        }

        void close() {
            try {
                if (client != null) {
                    client.close();
                }
                ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"DISCONNECTED\"}"));
                connected = false;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
