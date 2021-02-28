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
import org.json.JSONObject
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

    private var extraVideoParams: String? = ""
    private var ipAddress: String? = ""
    private var ipPort: String? = "1935"

    private var mLibVLC: LibVLC? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mVideoLayout: VLCVideoLayout? = null

    init {
        hide ()

        StatusEventBus.addSubject("IP_ADDRESS")
        StatusEventBus.getProcessor("IP_ADDRESS")?.subscribe({
            this.ipAddress = it
        }, {
            Log.i(null, "Failed to send...")
        })

        StatusEventBus.addSubject("PORT")
        StatusEventBus.getProcessor("PORT")?.subscribe({
            ipPort = it
        }, {
            Log.i(null, "Failed to add PORT...")
        })

        StatusEventBus.addSubject("EXTRA_VIDEO_PARAMS")
        StatusEventBus.getProcessor("EXTRA_VIDEO_PARAMS")?.subscribe({
            this.extraVideoParams = it
        }, {
            Log.i(null, "Failed to add PORT...")
        })

        StatusEventBus.addSubject("VIDEO_COMMAND")
        StatusEventBus.getProcessor("VIDEO_COMMAND")?.subscribe {
            processVideoCommand(it as String)
        }

        val args = ArrayList<String>()
        args.add("-vvv")

        mLibVLC = LibVLC(context, args)
        mMediaPlayer = MediaPlayer(mLibVLC)

        mVideoLayout = this
    }

    private fun processVideoCommand(command: String) {

        when (command) {
            "STOP" -> {
                stop()
            }
            "START" -> {
                startSafe()
            }
        }
    }

    private fun startSafe () {
        if (ipAddress == null || ipPort == null) {
            Log.d(TAG, "Cannot start video, ipAddress: {ipAddress}, ipPort: {ipPort}")
            return
        }
        start("rtsp://${ipAddress}:${ipPort}${extraVideoParams}")
        show()
    }

    fun init(binding: ActivityFullscreenBinding) {
        hide()

        mMediaPlayer!!.detachViews()
        mMediaPlayer!!.attachViews(mVideoLayout!!, null, true, true)
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }

    fun release() {
        stop()
    }

    private fun start(url: String) {
        Log.i(null, "Stated the player..., playState: " + mMediaPlayer!!.playerState)

        val media = Media(mLibVLC, Uri.parse(url))
        Log.i (TAG, "media.state: " + media.state)

        mMediaPlayer!!.media = media
        media.release()

        mMediaPlayer!!.play()
        show()
    }

    fun stop() {
        mMediaPlayer!!.stop()
        // mMediaPlayer!!.detachViews()
        hide()
    }

    override fun onSurfacesCreated(vlcVout: IVLCVout?) {
        Log.i(null, "onSurfacesCreated.")
    }

    override fun onSurfacesDestroyed(vlcVout: IVLCVout?) {
        Log.i(null, "onSurfacesDestroyed")
    }
}
