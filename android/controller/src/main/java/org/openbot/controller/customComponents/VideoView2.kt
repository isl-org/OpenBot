/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:56 p.m.
 */

package org.openbot.controller.customComponents

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import com.pedro.vlc.VlcListener
import com.pedro.vlc.VlcVideoLibrary
import org.json.JSONObject
import org.openbot.controller.StatusEventBus
import org.openbot.controller.databinding.ActivityFullscreenBinding
import java.util.*

class VideoView2 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private val TAG:String = "VideoView"
    private var ipAddress:String? = null
    private var ipPort:String? = null

    init {
        StatusEventBus.addSubject("IP_ADDRESS")
        StatusEventBus.getProcessor("IP_ADDRESS")?.subscribe {
            gotIpAddress(it as String)
        }

        StatusEventBus.addSubject("PORT")
        StatusEventBus.getProcessor("PORT")?.subscribe {
            gotPort(it as String)
        }

        StatusEventBus.addSubject("CAMERA_RESOLUTION")
        StatusEventBus.getProcessor("CAMERA_RESOLUTION")?.subscribe {
            gotCameraResolution(it as String)
        }
    }

    private fun gotIpAddress(address: String) {
        if (ipPort != null) {
            start("rtsp://${address}:${ipPort}")
        }
        else ipAddress = address;
    }

    private fun gotPort(port: String) {
        if (ipAddress != null) {
            start("rtsp://${ipAddress}:${port}")
        }
        else ipPort = port;
    }

    private fun gotCameraResolution(data: String) {
        val dimensionJson = JSONObject(data)
        val width = dimensionJson.getInt("width")
        val height = dimensionJson.getInt("height")
        setMeasuredDimension(width, height)
    }

    private inner class Listener : VlcListener {
        override fun onComplete() {
            // Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "onComplete")
        }

        override fun onError() {
            // Toast.makeText(this, "Error, make sure your endpoint is correct", Toast.LENGTH_SHORT).show()
            vlcVideoLibrary?.stop()
        }
    }

    private var vlcVideoLibrary: VlcVideoLibrary? = null
    private val listener = Listener()
    private val options = arrayOf(":fullscreen")

    init {
    }

    fun init(binding: ActivityFullscreenBinding) {
        val args = ArrayList<String>()
        args.add("-vvv")

        vlcVideoLibrary = VlcVideoLibrary(context, listener, this)
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = INVISIBLE
    }

    fun release() {
        stop()
    }

    fun start(url: String) {
        vlcVideoLibrary!!.play(url)
    }

    fun stop() {
    }
}
