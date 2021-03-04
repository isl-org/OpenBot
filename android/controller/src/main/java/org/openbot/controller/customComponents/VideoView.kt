/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:56 p.m.
 */

package org.openbot.controller.customComponents

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import org.openbot.controller.StatusEventBus
import org.openbot.controller.databinding.ActivityFullscreenBinding
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.interfaces.IVLCVout
import org.videolan.libvlc.util.VLCVideoLayout
import java.util.*

@SuppressLint("CheckResult")
class VideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : org.videolan.libvlc.util.VLCVideoLayout(context, attrs, defStyleAttr), IVLCVout.Callback {

    private val TAG: String = "VideoView"
    private val player = VlcPlayer(context, this)

    private var serverUrl: String? = ""

    init {
        hide()

        StatusEventBus.addSubject("VIDEO_SERVER_URL")
        StatusEventBus.getProcessor("VIDEO_SERVER_URL")?.subscribe({
            this.serverUrl = it
        }, {
            Log.i(null, "Failed to send...")
        })

        StatusEventBus.addSubject("VIDEO_COMMAND")
        StatusEventBus.getProcessor("VIDEO_COMMAND")?.subscribe {
            processVideoCommand(it as String)
        }
    }

    fun init(binding: ActivityFullscreenBinding) {
        hide()
        player.init()
    }

    private fun processVideoCommand(command: String) {

        when (command) {
            "STOP" -> {
                stop()
            }
            "START" -> {
                start()
            }
        }
    }

    private fun start() {
        if (this.serverUrl == null) {
            Log.d(TAG, "Cannot start video, serverUrl: {serverUrl}")
            return
        }
        player.start(this.serverUrl!!)
        show()
    }

    fun stop() {
        player.stop()
        hide()
    }

    override fun onSurfacesCreated(vlcVout: IVLCVout?) {
        Log.i(null, "onSurfacesCreated.")
    }

    override fun onSurfacesDestroyed(vlcVout: IVLCVout?) {
        Log.i(null, "onSurfacesDestroyed")
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }
}
