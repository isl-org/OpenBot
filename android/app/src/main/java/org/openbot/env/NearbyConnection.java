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

package org.openbot.env;

import android.content.Context;
import android.media.ToneGenerator;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.Strategy;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import org.openbot.robot.CameraActivity;

public class NearbyConnection {
  private static final String TAG = "NearbyConnection";
  private String pairedDeviceEndpointId;
  private static final Strategy STRATEGY = Strategy.P2P_POINT_TO_POINT;
  private static PayloadCallback payloadCallback;
  private static Context context;
  private static final String SERVICE_ID = "OPENBOT_SERVICE_ID";
  private final CancelableDiscovery discovery = new CancelableDiscovery(this);
  private boolean isConnected = false;

  // Our handle to Nearby Connections
  private ConnectionsClient connectionsClient;

  // Callbacks for finding other devices
  private final EndpointDiscoveryCallback endpointDiscoveryCallback =
      new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(
            @NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
          stopDiscovery();
          discovery.cancel();
          String connectionName = "OpenBotConnection";
          connectionsClient
              .requestConnection(connectionName, endpointId, connectionLifecycleCallback)
              .addOnSuccessListener(unusedResult -> Log.d("requestConnection", "Connected OK"))
              .addOnFailureListener(
                  e -> {
                    Log.d("requestConnection", "Unable to connect: Error: " + e.toString());

                    if (e.getMessage().contains("8012: STATUS_ENDPOINT_IO_ERROR")) {
                      // retry. this usually helps...
                      Log.d("NearbyConnection", "Got 8012. Reconnecting...");

                      connectionsClient.stopDiscovery();
                      connectionsClient.stopAdvertising();

                      connect(NearbyConnection.context, NearbyConnection.payloadCallback);
                    }
                    if (e.getMessage().contains("8002: STATUS_ALREADY_DISCOVERING")) {
                      Log.d("NearbyConnection", "Got 8002: STATUS_ALREADY_DISCOVERING");
                      connectionsClient.stopAdvertising();
                    }

                    if (e.getMessage().contains("8003: STATUS_ALREADY_CONNECTED_TO_ENDPOINT")) {
                      Log.d("NearbyConnection", "Got 8003. Do nothing...");
                    }

                    if (e.getMessage().contains("8007: STATUS_BLUETOOTH_ERROR")) {
                      Log.d("NearbyConnection", "Got 8007. Reconnecting...");
                      connectionsClient.stopDiscovery();
                      connectionsClient.stopAdvertising();

                      connect(NearbyConnection.context, NearbyConnection.payloadCallback);
                    }
                  });
        }

        @Override
        public void onEndpointLost(@NonNull String endpointId) {
          Log.i(TAG, "onEndpointLost: endpoint lost");
          discovery.cancel();
          stopDiscovery();
        }
      };

  // Callbacks for connections to other devices
  private final ConnectionLifecycleCallback connectionLifecycleCallback =
      new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(
            @NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
          Log.i(TAG, "onConnectionInitiated: accepting connection");
          connectionsClient.acceptConnection(endpointId, payloadCallback);
        }

        @Override
        public void onConnectionResult(@NonNull String endpointId, ConnectionResolution result) {
          if (result.getStatus().isSuccess()) {
            Log.i(TAG, "onConnectionResult: connection successful");
            beep();
            Toast.makeText(
                    CameraActivity.getContext(),
                    "Smartphone controller connected",
                    Toast.LENGTH_LONG)
                .show();

            pairedDeviceEndpointId = endpointId;
            isConnected = true;
            try {
              ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"CONNECTED\"}"));
            } catch (JSONException e) {
              e.printStackTrace();
            }
          } else {
            Log.i(TAG, "onConnectionResult: connection failed");
            isConnected = false;
            Toast.makeText(
                    CameraActivity.getContext(),
                    "Smartphone controller failed to connect",
                    Toast.LENGTH_LONG)
                .show();
          }
        }

        @Override
        public void onDisconnected(@NonNull String endpointId) {
          isConnected = false;
          Toast.makeText(
                  CameraActivity.getContext(),
                  "Smartphone controller disconnected",
                  Toast.LENGTH_LONG)
              .show();
          Log.i(TAG, "onDisconnected: disconnected from the opponent");
          try {
            ControllerToBotEventBus.emitEvent(new JSONObject("{command: \"DISCONNECTED\"}"));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      };

  private void stopDiscovery() {
    connectionsClient.stopDiscovery();
  }

  /** Finds an opponent to play the game with using Nearby Connections. */
  public void connect(Context context, PayloadCallback payloadCallback) {
    NearbyConnection.context = context;
    NearbyConnection.payloadCallback = payloadCallback;
    connectionsClient = Nearby.getConnectionsClient(context);

    // make sure we are not connecting
    disconnect();

    // If nothing found within timeout period (60 seconds), discovery will stop.
    discovery.startDiscovery(60);
  }

  /** Disconnects from the opponent and reset the UI. */
  public void disconnect() {

    discovery.cancel();

    if (pairedDeviceEndpointId != null) {
      connectionsClient.disconnectFromEndpoint(pairedDeviceEndpointId);
    }

    isConnected = false;
  }

  /** /** Starts looking for other players using Nearby Connections. */
  private void startDiscovery() {
    // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
    connectionsClient
        .startDiscovery(
            SERVICE_ID,
            endpointDiscoveryCallback,
            new DiscoveryOptions.Builder().setStrategy(STRATEGY).build())
        .addOnSuccessListener(unusedResult -> Log.d("startDiscovery", "We started discovery OK"))
        .addOnFailureListener(
            e ->
                Log.d(
                    "startDiscovery",
                    "We were unable to start startDiscovery. Error: " + e.toString()));
    Toast.makeText(
            CameraActivity.getContext(),
            "Searching for smartphone controller...",
            Toast.LENGTH_LONG)
        .show();
  }

  private void beep() {
    final ToneGenerator tg = new ToneGenerator(6, 100);
    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
  }

  public boolean isConnected() {
    return isConnected;
  }

  public void sendMessage(String message) {
    if (connectionsClient == null) {
      Log.d(TAG, "Cannot send...No connection!");
      return;
    }
    connectionsClient.sendPayload(
        pairedDeviceEndpointId, Payload.fromBytes(message.getBytes(StandardCharsets.UTF_8)));
  }

  public class CancelableDiscovery {
    Timer timer;
    NearbyConnection connection;

    public CancelableDiscovery(NearbyConnection connection) {
      this.connection = connection;
    }

    public void startDiscovery(int seconds) {
      timer = new Timer();
      timer.schedule(new StopDiscoveryTask(), seconds * 60);
      connection.startDiscovery();
    }

    class StopDiscoveryTask extends TimerTask {
      public void run() {
        connection.stopDiscovery();
        cancel();
      }
    }

    public void cancel() {
      if (timer != null) {
        timer.cancel();
        timer = null;
      }
    }
  }
}
