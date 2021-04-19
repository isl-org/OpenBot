package org.openbot.controller

import android.content.Context
import android.content.IntentFilter
import android.media.ToneGenerator
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import com.abemart.wroup.common.WiFiDirectBroadcastReceiver
import com.abemart.wroup.common.WiFiP2PError
import com.abemart.wroup.common.WiFiP2PInstance
import com.abemart.wroup.common.WroupDevice
import com.abemart.wroup.common.listeners.ClientConnectedListener
import com.abemart.wroup.common.listeners.ClientDisconnectedListener
import com.abemart.wroup.common.listeners.DataReceivedListener
import com.abemart.wroup.common.listeners.ServiceRegisteredListener
import com.abemart.wroup.common.messages.MessageWrapper
import com.abemart.wroup.service.WroupService
import org.json.JSONException
import org.openbot.controller.utils.EventProcessor

object WiFiDirectConnection : ILocalConnection {
    var wiFiDirectBroadcastReceiver: WiFiDirectBroadcastReceiver? = null
    var intentFilter = IntentFilter()
    var wroupService: WroupService? = null
    var connected = false
        private set
    private var dataReceivedCallback: IDataReceived? = null
    private const val TAG = "WiFiDirectConnection"
    private const val SERVICE_ID = "OPENBOT_SERVICE_ID"


    override fun init(context: Context) {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        wiFiDirectBroadcastReceiver = WiFiP2PInstance.getInstance(context).broadcastReceiver
        context.registerReceiver(wiFiDirectBroadcastReceiver, intentFilter)

        wroupService = WroupService.getInstance(context)
        wroupService?.setDataReceivedListener(DataReceiver())
        wroupService?.setClientDisconnectedListener(DisconnectionListener())
        wroupService?.setClientConnectedListener(ConnectionListener())
    }

    override fun setDataCallback(dataCallback: IDataReceived?) {
        dataReceivedCallback = dataCallback
    }

    internal class DataReceiver : DataReceivedListener {
        override fun onDataReceived(messageWrapper: MessageWrapper) {
            dataReceivedCallback?.dataReceived(messageWrapper.message)
        }
    }

    internal class ConnectionListener : ClientConnectedListener {
        override fun onClientConnected(wroupDevice: WroupDevice) {
            Log.i(TAG, "onConnectionResult: connection successful")
            try {
                connected = true

                val event: EventProcessor.ProgressEvents =
                        EventProcessor.ProgressEvents.ConnectionSuccessful
                EventProcessor.onNext(event)

                beep()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    internal class DisconnectionListener : ClientDisconnectedListener {
        override fun onClientDisconnected(wroupDevice: WroupDevice) {
            Log.i(TAG, "onDisconnected: disconnected...")
            try {
                connected = false

                val event: EventProcessor.ProgressEvents =
                        EventProcessor.ProgressEvents.Disconnected
                EventProcessor.onNext(event)

                beep()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun connect(context: Context) {
        wroupService!!.registerService(SERVICE_ID, object : ServiceRegisteredListener {
            override fun onSuccessServiceRegistered() {
                Log.i(TAG, "onSuccessServiceRegistered")
            }

            override fun onErrorServiceRegistered(wiFiP2PError: WiFiP2PError) {
                Log.i(TAG, "onErrorServiceRegistered")
            }
        })
    }

    override fun disconnect(context: Context?) {
        if (wroupService != null) {
            wroupService!!.disconnect()
        }
    }

    override fun isConnected(): Boolean {
        return connected
    }

    override fun sendMessage(message: String?) {
        val messageWrapper = MessageWrapper()
        messageWrapper.message = message
        messageWrapper.messageType = MessageWrapper.MessageType.NORMAL
        wroupService!!.sendMessageToAllClients(messageWrapper)
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    private fun beep() {
        val tg = ToneGenerator(6, 100)
        tg.startTone(ToneGenerator.TONE_PROP_BEEP)
    }
}
