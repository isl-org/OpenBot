/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:58 p.m.
 */

package org.openbot.controller

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import org.json.JSONObject
import java.nio.charset.StandardCharsets

/*
This class listens for status data from the Bot and emits events.
These events are received by various custom components which update their UI accordingly.
For example, a right indicator will start blinking if the status on the bot is set.
 */
object BotDataListener {
    fun init() {
        NearbyConnection.setPayloadCallback(::payloadCallback)
    }

    // Callbacks for receiving payloads. The NearbyConnection will call this upon receiving new data.
    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(
            endpointId: String,
            payload: Payload
        ) {
            val dataJson = JSONObject(
                String(
                    payload.asBytes()!!,
                    StandardCharsets.UTF_8
                )
            )
            val statusValues = dataJson.getJSONObject("status")

            for (key in statusValues.keys()) {
                val value: String = statusValues.getString(key)

                /*
                Send an event on a particular subject.
                The custom components are listening on their subject.
                */
                StatusEventBus.emitEvent(key, value)
            }
        }

        override fun onPayloadTransferUpdate(
            endpointId: String,
            update: PayloadTransferUpdate
        ) {
        }
    }
}