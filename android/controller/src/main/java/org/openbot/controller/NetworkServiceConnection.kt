package org.openbot.controller

import android.app.Activity
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.RegistrationListener
import android.net.nsd.NsdServiceInfo
import android.util.Log
import org.openbot.controller.utils.EventProcessor
import org.openbot.controller.utils.Utils
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import kotlin.concurrent.thread

object NetworkServiceConnection : ILocalConnection {

    private val TAG = "NetworkServiceConn"

    private var mNsdManager: NsdManager? = null

    private var SERVICE_NAME = "OPEN_BOT_CONTROLLER"
    private val SERVICE_TYPE = "_openbot._tcp."
    private var dataReceivedCallback: IDataReceived? = null

    private const val port = 19400
    private lateinit var socketHandler: SocketHandler
    private val messageQueue: BlockingQueue<String> = ArrayBlockingQueue(100)
    private lateinit var context: Context

    override fun init(context: Context) {
        this.context = context
        mNsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager?
        registerService(port)
    }

    override fun setDataCallback(dataCallback: IDataReceived?) {
        dataReceivedCallback = dataCallback
    }

    override fun connect(context: Context) {
        runConnection()
    }

    override fun disconnect(context: Context?) {
        mNsdManager!!.unregisterService(mRegistrationListener)
        socketHandler.close()
    }

    override fun isConnected(): Boolean {
        return socketHandler.isConnected()
    }

    override fun sendMessage(message: String?) {
        socketHandler.put(message)
    }

    override fun start() {
    }

    override fun stop() {
    }
    // end of interface

    private fun runConnection() {
        socketHandler = SocketHandler(messageQueue)

        thread {
            val client: SocketHandler.ClientInfo? = socketHandler.connect(this.port)
            if (client == null) {
                return@thread
            }

            thread {
                socketHandler.runSender(client.writer)
            }
            thread {
                socketHandler.runReceiver(client.reader)
            }
        }
    }

    class SocketHandler(private val messageQueue: BlockingQueue<String>) {
        private lateinit var client: Socket
        private lateinit var serverSocket: ServerSocket
        private lateinit var clientInfo: ClientInfo

        class ClientInfo(val reader: Scanner, val writer: OutputStream) {
        }

        fun isConnected(): Boolean {
            return !client.isClosed()
        }

        fun connect(port: Int): ClientInfo? {
            serverSocket = ServerSocket(port)
            serverSocket.reuseAddress = true
            try {
                while (true) {
                    client = serverSocket.accept()

                    // only connect if the app is NOT running on this device.
                    if (client.inetAddress.hostAddress != Utils.getIPAddress(true)) {
                        break
                    }
                }

                val reader = Scanner(DataInputStream(BufferedInputStream(client.getInputStream())))
                val writer = client.getOutputStream()

                clientInfo = ClientInfo(reader, writer)

                val event: EventProcessor.ProgressEvents =
                        EventProcessor.ProgressEvents.ConnectionSuccessful
                EventProcessor.onNext(event)

                println("Client connected: ${client.inetAddress.hostAddress}")
            } catch (e: Exception) {
                Log.d(TAG, "Got exception: " + e)
                close()
                return null
            }
            return clientInfo
        }

        fun runReceiver(reader: Scanner?) {
            try {
                while (true) {
                    val payload: String? = reader?.nextLine()
                    if (payload != null) {

                        (context as Activity).runOnUiThread {
                            dataReceivedCallback?.dataReceived(String(
                                    payload.toByteArray(),
                                    StandardCharsets.UTF_8
                            ))
                        }
                    }
                }

            } catch (ex: Exception) {
                reader?.close()
                Log.d(TAG, "got exception ${ex}")
                close()

            } finally {
            }
        }

        fun runSender(writer: OutputStream?) {
            Log.i(TAG, "runSender started...")
            try {
                while (true) {
                    val message = messageQueue.take() as String
                    writer?.write((message + '\n').toByteArray(Charset.defaultCharset()))
                }
            } catch (e: Exception) {
                Log.d(TAG, "runSender InterruptedException: {e}")
                if (writer != null) {
                    writer.close()
                }
            } finally {
            }
            Log.i(TAG, "end of runSender thread...")
        }

        fun close() {
            if (this::client.isInitialized && !client.isClosed) {
                client.close()
            }
            serverSocket.close()

            val event: EventProcessor.ProgressEvents =
                    EventProcessor.ProgressEvents.Disconnected
            EventProcessor.onNext(event)
        }

        fun put(message: String?) {
            try {
                this.messageQueue.put(message)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun registerService(port: Int) {
        val serviceInfo = NsdServiceInfo()
        serviceInfo.serviceName = SERVICE_NAME
        serviceInfo.serviceType = SERVICE_TYPE
        serviceInfo.port = port

        mNsdManager!!.registerService(serviceInfo,
                NsdManager.PROTOCOL_DNS_SD,
                mRegistrationListener)
    }

    var mRegistrationListener: RegistrationListener = object : RegistrationListener {
        override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
            val mServiceName = NsdServiceInfo.serviceName
            SERVICE_NAME = mServiceName
            Log.d(TAG, "Registered name : $mServiceName")
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Log.d(TAG, "onRegistrationFailed")

            val event: EventProcessor.ProgressEvents =
                    EventProcessor.ProgressEvents.ConnectionFailed
            EventProcessor.onNext(event)
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
            Log.d(TAG, "Service Unregistered : " + serviceInfo.serviceName)
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Log.d(TAG, "onUnregistrationFailed : " + errorCode)
        }
    }
}