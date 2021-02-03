package org.openbot.controller

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import com.abemart.wroup.client.WroupClient
import com.abemart.wroup.common.*
import com.abemart.wroup.common.listeners.*
import com.abemart.wroup.common.messages.MessageWrapper
import com.abemart.wroup.service.WroupService
import org.openbot.controller.utils.EventProcessor


object WiFiDirectConnection : ILocalConnection {
    private const val TAG = "WiFiDirectConnection"

    var intentFilter = IntentFilter()
    lateinit private var wroupClient: WroupClient
    private const val SERVICE_ID = "OPENBOT_SERVICE_ID"
    var connected = false
        private set

    private var dataReceivedCallback: IDataReceived? = null
    private var wiFiDirectBroadcastReceiver: WiFiDirectBroadcastReceiver? = null

    init {
    }

    override fun init(context: Context) {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        wiFiDirectBroadcastReceiver = WiFiP2PInstance.getInstance(context).broadcastReceiver
        context.registerReceiver(wiFiDirectBroadcastReceiver, intentFilter)
    }

    class DataReceiver : DataReceivedListener {
        override fun onDataReceived(messageWrapper: MessageWrapper) {
            dataReceivedCallback?.dataReceived(messageWrapper.message)
        }
    }

    override fun setDataCallback(dataCallback: IDataReceived?) {
        dataReceivedCallback = dataCallback
    }

    class ConnectionListener : ClientConnectedListener {
        override fun onClientConnected(wroupDevice: WroupDevice) {

            val event: EventProcessor.ProgressEvents =
                    EventProcessor.ProgressEvents.ConnectionSuccessful
            EventProcessor.onNext(event)

            connected = true
        }
    }

    class ServiceDisconnectionListener : ServiceDisconnectedListener {

        override fun onServerDisconnectedListener() {
            val event: EventProcessor.ProgressEvents =
                    EventProcessor.ProgressEvents.Disconnected
            EventProcessor.onNext(event)

            connected = false
        }
    }

    override fun connect(context: Context) {
        searchAvailableGroups(context)
    }

    override fun disconnect(context: Context?) {
        if (this::wroupClient.isInitialized)
            wroupClient.disconnect()
    }

    override fun isConnected(): Boolean {
        return connected
    }

    override fun sendMessage(message: String?) {
        val messageWrapper = MessageWrapper()
        messageWrapper.message = message
        messageWrapper.messageType = MessageWrapper.MessageType.NORMAL
        wroupClient.sendMessageToAllClients(messageWrapper)
    }

    private fun searchAvailableGroups(context: Context) {

        wroupClient = WroupClient.getInstance(context.applicationContext)
        wroupClient.setDataReceivedListener(DataReceiver())
        wroupClient.setClientConnectedListener(ConnectionListener())
        wroupClient.setServerDisconnetedListener (ServiceDisconnectionListener())

        class DiscoveryListener : ServiceDiscoveredListener {
            override fun onNewServiceDeviceDiscovered(serviceDevice: WroupServiceDevice) {
                Log.i(TAG, "New group found:")
                Log.i(TAG, "\tName: " + serviceDevice.txtRecordMap[WroupService.SERVICE_GROUP_NAME])

                class ServiceListener : ServiceConnectedListener {
                    override fun onServiceConnected(serviceDevice: WroupDevice?) {
                        val event: EventProcessor.ProgressEvents = EventProcessor.ProgressEvents.ConnectionSuccessful
                        EventProcessor.onNext(event)
                        connected = true
                    }
                }

                if (serviceDevice.txtRecordMap.get("GROUP_NAME") == SERVICE_ID) {
                }
            }

            override fun onFinishServiceDeviceDiscovered(serviceDevices: List<WroupServiceDevice>) {
                Log.i(TAG, "Found '" + serviceDevices.size + "' groups")
                if (serviceDevices.isEmpty()) {
                    Log.i(TAG, "No groups found")

                    val event: EventProcessor.ProgressEvents =
                            EventProcessor.ProgressEvents.TemporaryConnectionProblem
                    EventProcessor.onNext(event)


                } else {
                    Log.i(TAG, "Finished discovery")

                    for (serviceDevice in serviceDevices) {
                        val group = serviceDevice.txtRecordMap.get("GROUP_NAME")
                        if (group == WiFiDirectConnection.SERVICE_ID) {

                            class ServiceConListener : ServiceConnectedListener {
                                override fun onServiceConnected(serviceDevice: WroupDevice?) {
                                    val event: EventProcessor.ProgressEvents = EventProcessor.ProgressEvents.ConnectionSuccessful
                                    EventProcessor.onNext(event)
                                    connected = true
                                }
                            }
                            wroupClient.connectToService(serviceDevice, ServiceConListener())
                        }
                    }
                }
            }

            override fun onError(wiFiP2PError: WiFiP2PError) {
                Log.d(TAG, "Error searching groups: $wiFiP2PError")
            }
        }

        wroupClient.discoverServices(5 * 1000L, DiscoveryListener())
    }
}
