/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

// Modified by Matthias Mueller - Intel Intelligent Systems Lab - 2020

package org.openbot;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

import androidx.annotation.NonNull;

import com.felhr.utils.Utils;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.Task;

public class NearbyConnection {
    private static final String TAG = "NearbyConnection";
    private String pairedDeviceEndpointId;
    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    private static PayloadCallback payloadCallback;
    private static final String SERVICE_ID = "OPENBOT_SERVICE_ID";

    // Our handle to Nearby Connections
    private ConnectionsClient connectionsClient;

    // Callbacks for finding other devices
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
                    Log.i(TAG, "onEndpointFound: endpoint found, connecting");
                    String connectionName = "OpenBotConnection";
                    connectionsClient.requestConnection(connectionName, endpointId, connectionLifecycleCallback).addOnSuccessListener(
                            unusedResult -> Log.d("requestConnection", "Connected OK")
                    ).addOnFailureListener(

                            e -> Log.d("requestConnection", "Unable to connet: Error: " + e.toString()));
                }

                @Override
                public void onEndpointLost(@NonNull String endpointId) {
                    Log.i(TAG, "onEndpointLost: endpoint lost");
                }
            };

    // Callbacks for connections to other devices
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                    Log.i(TAG, "onConnectionInitiated: accepting connection");
                    final Task<Void> voidTask = connectionsClient.acceptConnection(endpointId, payloadCallback);
                }

                @Override
                public void onConnectionResult(@NonNull String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i(TAG, "onConnectionResult: connection successful");
                        beep();

                        pairedDeviceEndpointId = endpointId;
                        connectionsClient.stopDiscovery();
                    } else {
                        Log.i(TAG, "onConnectionResult: connection failed");
                    }
                }

                @Override
                public void onDisconnected(@NonNull String endpointId) {
                    Log.i(TAG, "onDisconnected: disconnected from the opponent");
                }
            };

    /**
     * Finds an opponent to play the game with using Nearby Connections.
     */
    public void connect(Context context, PayloadCallback payloadCallback) {
        NearbyConnection.payloadCallback = payloadCallback;
        connectionsClient = Nearby.getConnectionsClient(context);
        startDiscovery(context);
    }

    /**
     * Disconnects from the opponent and reset the UI.
     */
    public void disconnect() {

        if (pairedDeviceEndpointId != null) {
            connectionsClient.disconnectFromEndpoint(pairedDeviceEndpointId);
        }
    }

    /**
     * Starts looking for other players using Nearby Connections.
     */
    private void startDiscovery(Context context) {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startDiscovery(
                SERVICE_ID, endpointDiscoveryCallback,
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build())
                .addOnSuccessListener(
                        unusedResult -> Log.d("startDiscovery", "We started discovery OK")
                ).addOnFailureListener(

                e -> Log.d("startDiscovery", "We were unable to start startDiscovery. Error: " + e.toString()));
    }

    private void beep () {
        final ToneGenerator tg = new ToneGenerator(6, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
    }
}
