/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:58 p.m.
 */

package org.openbot.controller

import android.content.Context
import android.util.Log
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import org.openbot.controller.utils.EventProcessor
import org.openbot.controller.utils.Utils
import java.nio.charset.StandardCharsets
import kotlin.reflect.KProperty0

object NearbyConnection : ILocalConnection {
    private var dataReceivedCallback: IDataReceived? = null
    private const val TAG = "NearbyConnection"
    private val connectionName = "OpenBotConnection"

    // Our handle to Nearby Connections
    private var connectionsClient: ConnectionsClient? = null
    private var pairedDeviceEndpointId: String? = null
    private var pairedDeviceName: String? = null
    private const val SERVICE_ID = "OPENBOT_SERVICE_ID"

    private val STRATEGY = Strategy.P2P_POINT_TO_POINT

    override fun init(context: Context) {
    }

    override fun setDataCallback(dataCallback: IDataReceived?) {
        dataReceivedCallback = dataCallback
    }

    // Callbacks for receiving payloads. The NearbyConnection will call this upon receiving new data.
    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(
                endpointId: String,
                payload: Payload
        ) {
            dataReceivedCallback?.dataReceived(String(
                    payload.asBytes()!!,
                    StandardCharsets.UTF_8
            ))
        }

        override fun onPayloadTransferUpdate(
                endpointId: String,
                update: PayloadTransferUpdate
        ) {
        }
    }


    // Callbacks for connections to other devices
    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(
                endpointId: String,
                connectionInfo: ConnectionInfo
            ) {
                Log.i(
                    TAG,
                    "onConnectionInitiated: accepting connection"
                )
                connectionsClient!!.acceptConnection(endpointId, payloadCallback)
                pairedDeviceName = connectionInfo.endpointName
            }

            override fun onConnectionResult(
                endpointId: String,
                result: ConnectionResolution
            ) {
                if (result.status.isSuccess) {
                    Utils.beep(Utils.TONE.INTERCEPT)

                    Log.i(
                        TAG,
                        "onConnectionResult: connection successful"
                    )
                    val event: EventProcessor.ProgressEvents =
                        EventProcessor.ProgressEvents.ConnectionSuccessful
                    EventProcessor.onNext(event)

                    connectionsClient!!.stopAdvertising()
                    pairedDeviceEndpointId = endpointId

                } else {
                    abortConnection()

                    val event: EventProcessor.ProgressEvents =
                        EventProcessor.ProgressEvents.ConnectionFailed
                    EventProcessor.onNext(event)

                    Log.i(
                        TAG,
                        "onConnectionResult: connection failed"
                    )
                }
            }

            override fun onDisconnected(endpointId: String) {
                val event: EventProcessor.ProgressEvents =
                    EventProcessor.ProgressEvents.Disconnected
                EventProcessor.onNext(event)

                Log.i(
                    TAG,
                    "onDisconnected: disconnected from the nearby device"
                )
            }
        }

    override fun connect(context: Context) {
        connectionsClient = Nearby.getConnectionsClient(context)
        startAdvertising()
    }

    override fun disconnect(context: Context?) {
        val event: EventProcessor.ProgressEvents =
                EventProcessor.ProgressEvents.Disconnecting
        EventProcessor.onNext(event)

        connectionsClient?.stopAdvertising()

        if (pairedDeviceEndpointId != null) {
            connectionsClient?.disconnectFromEndpoint(pairedDeviceEndpointId!!)
        }
        connectionsClient?.stopAllEndpoints()
    }

    override fun isConnected(): Boolean {
        TODO("Not yet implemented")
    }

    /** Broadcasts our presence using Nearby Connections so the bot can find us  */
    private fun startAdvertising() {
        connectionsClient!!.startAdvertising(
            connectionName, SERVICE_ID, connectionLifecycleCallback,
            AdvertisingOptions.Builder()
                .setStrategy(STRATEGY)
                .build()
        )
            .addOnSuccessListener {
                Log.d("startAdvertising", "We're advertising")

                val event: EventProcessor.ProgressEvents =
                    EventProcessor.ProgressEvents.StartAdvertising
                EventProcessor.onNext(event)

            }.addOnFailureListener {
                abortConnection()

                val event: EventProcessor.ProgressEvents =
                    EventProcessor.ProgressEvents.AdvertisingFailed
                EventProcessor.onNext(event)

                Log.d(
                    "startAdvertising",
                    "We were unable to start advertising."
                )
            }
    }

    private fun abortConnection() {
        disconnect()
    }

    override fun sendMessage(message: String?) {
        if (connectionsClient == null || pairedDeviceEndpointId == null) {
            Log.d(TAG, "Cannot send...No connection!")
            return
        }
        connectionsClient!!.sendPayload(
            pairedDeviceEndpointId!!,
            Payload.fromBytes(message!!.toByteArray(StandardCharsets.UTF_8))
        )
    }

    override fun start() {
    }

    override fun stop() {
    }
}
