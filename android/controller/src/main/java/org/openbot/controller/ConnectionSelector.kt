package org.openbot.controller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager

@SuppressLint("StaticFieldLeak")
object ConnectionSelector {

    private val connection: ILocalConnection? = null
    private const val TAG = "ConnectionManager"
    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context

    fun init (context: Context) {
        this.context = context
    }

    fun getConnection(): ILocalConnection {
        val connected = isConnectedViaWifi(context) || isWifiApEnabled(context)
        return if (connected) NetworkServiceConnection else NearbyConnection
    }

    private fun isConnectedViaWifi(context: Context): Boolean {

        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager?
        val info = wifiManager!!.connectionInfo ?: return false
        val networkId = info.networkId

        return networkId > 0
    }

    private fun isWifiApEnabled(context: Context): Boolean {
        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
        return try {
            val method = wifiManager.javaClass.getDeclaredMethod("isWifiApEnabled")
            method.invoke(wifiManager) as? Boolean == true
        } catch (ignored: Throwable) {
            false
        }
    }
}
