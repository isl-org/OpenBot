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
import android.net.Uri
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import org.openbot.controller.StatusEventBus
import org.openbot.controller.databinding.ActivityFullscreenBinding
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout
import java.util.*


class VideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : org.videolan.libvlc.util.VLCVideoLayout(context, attrs, defStyleAttr) {

    private val TAG: String = "VideoView"
    private var ipAddress: String? = null
    private var ipPort: String? = null

    private var mLibVLC: LibVLC? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mVideoLayout: VLCVideoLayout? = null

    init {
        StatusEventBus.addSubject("IP_ADDRESS")
        StatusEventBus.getProcessor("IP_ADDRESS")?.subscribe ({
            gotIpAddress(it as String)
        }, {
            Log.i(null, "Failed to send...")
        })

        StatusEventBus.addSubject("PORT")
        StatusEventBus.getProcessor("PORT")?.subscribe ({
            gotPort(it as String)
        }, {
            Log.i(null, "Failed to add PORT...")
        })

        StatusEventBus.addSubject("CAMERA_RESOLUTION")
        StatusEventBus.getProcessor("CAMERA_RESOLUTION")?.subscribe {
            gotCameraResolution(it as String)
        }

        StatusEventBus.addSubject("VIDEO_COMMAND")
        StatusEventBus.getProcessor("VIDEO_COMMAND")?.subscribe {
            processVideoCommand(it as String)
        }
    }

    private fun processVideoCommand(command: String) {

        when (command) {
            "STOP" -> {
                stop()
            }
        }
    }

    private fun gotIpAddress(address: String) {
        if (ipPort != null) {
            start("rtsp://${address}:${ipPort}")
        } else ipAddress = address;
    }

    private fun gotPort(port: String) {
        if (ipAddress != null) {
            start("rtsp://${ipAddress}:${port}")
        } else ipPort = port;
    }

    private fun gotCameraResolution(data: String) {
        val dimensionJson = JSONObject(data)
        val width = dimensionJson.getInt("width")
        val height = dimensionJson.getInt("height")
        setMeasuredDimension(width, height)
    }

    init {
    }

    fun init(binding: ActivityFullscreenBinding) {
        val args = ArrayList<String>()
        args.add("-vvv")

        mLibVLC = LibVLC(context, args)
        mMediaPlayer = org.videolan.libvlc.MediaPlayer(mLibVLC)

        mVideoLayout = this
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

    private fun start(url: String) {

        mMediaPlayer!!.detachViews()
        mMediaPlayer!!.attachViews(mVideoLayout!!, null, true, true)

        val media = Media(mLibVLC, Uri.parse(url))
        media.setHWDecoderEnabled(true, true)

        mMediaPlayer!!.media = media
        media.release()

        mMediaPlayer!!.play()
    }

    fun stop() {
        mMediaPlayer!!.stop()
        mMediaPlayer!!.detachViews()
    }
}
