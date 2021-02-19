package org.openbot.controller

import android.app.Activity
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.RegistrationListener
import android.net.nsd.NsdServiceInfo
import android.util.Log
import org.openbot.controller.utils.EventProcessor
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.OutputStream
import java.net.BindException
import java.net.InetSocketAddress
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
    private val SERVICE_TYPE = "_openbot._tcp"
    private var dataReceivedCallback: IDataReceived? = null
    var connected = false
        private set

    private const val port = 19400
    private lateinit var socketHandler: SocketHandler;
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
        return connected;
    }

    override fun sendMessage(message: String?) {
        socketHandler.put(message);
    }
    // end of interface //////////////////////////////////

    private fun runConnection() {
        socketHandler = SocketHandler(messageQueue)

        thread {
            val client: SocketHandler.ClientInfo? = socketHandler.connect(this.port)

            thread {
                socketHandler.runSender(client?.writer)
            }
            thread {
                socketHandler.runReceiver(client?.reader)
            }
        }
    }

    class SocketHandler(messageQueue: BlockingQueue<String>) {
        private lateinit var client: Socket
        private val serverSocket: ServerSocket = ServerSocket(port)
        private val messageQueue: BlockingQueue<String> = messageQueue

        class ClientInfo(val reader: Scanner, val writer: OutputStream) {
        }

        fun connect(port: Int): ClientInfo? {
            lateinit var clientInfo: ClientInfo
            serverSocket.reuseAddress = true;
            try {
                client = serverSocket.accept()

                var reader = Scanner(DataInputStream(BufferedInputStream(client.getInputStream())))
                var writer = client.getOutputStream()

                clientInfo = ClientInfo(reader, writer)

                if (!isConnected()) {
                    connected = true
                    val event: EventProcessor.ProgressEvents =
                            EventProcessor.ProgressEvents.ConnectionSuccessful
                    EventProcessor.onNext(event)
                }
                println("Client connected: ${client.inetAddress.hostAddress}")
            } catch (e: BindException) {
                Log.d(TAG, "Already connected");
                close()
            }
            return clientInfo
        }

        fun runReceiver(reader: Scanner?) {
            try {
                while (true) {
                    val payload: String? = reader?.nextLine()
                    Log.d(TAG, "Got payload ${payload}");

                    if (isConnected() && payload != null) {

                        (context as Activity).runOnUiThread(Runnable {
                            dataReceivedCallback?.dataReceived(String(
                                    payload.toByteArray(),
                                    StandardCharsets.UTF_8
                            ))
                        })


                    }
                }

            } catch (ex: Exception) {

                Log.d(TAG, "got exception ${ex}")
                if (reader != null) {
                    reader.close()
                }
                close()

            } finally {
            }
        }

        fun runSender(writer: OutputStream?) {
            try {
                while (true) {
                    val message = messageQueue.take() as String
                    writer?.write((message + '\n').toByteArray(Charset.defaultCharset()))
                }
            } catch (e: InterruptedException) {
                Log.d(TAG, "runSender InterruptedException: {e}");
                if (writer != null) {
                    writer.close()
                }
                close()
            } finally {
            }
        }

        fun close() {
            if (this::client.isInitialized && !client.isClosed) {
                client.close()
            }
            val event: EventProcessor.ProgressEvents =
                    EventProcessor.ProgressEvents.Disconnected
            EventProcessor.onNext(event)

            connected = false
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
        serviceInfo.serviceType = SERVICE_TYPE;
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
            // Registration failed! Put debugging code here to determine
            // why.
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
            Log.d(TAG, "Service Unregistered : " + serviceInfo.serviceName)
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Log.d(TAG, "onUnregistrationFailed : " + errorCode)
        }
    }
}