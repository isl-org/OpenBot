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
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import org.openbot.controller.StatusEventBus
import org.openbot.controller.databinding.ActivityFullscreenBinding
import java.util.*

@SuppressLint("CheckResult")
class VideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    private val TAG: String = "VideoView"
    private val player = AndroidMediaPlayer(context, this)

    private var serverUrl: String? = ""

    init {
        holder.addCallback(this)
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
        player.setSurfaceChangedCallback { holder: SurfaceHolder, width: Int, height: Int ->
            setSurfaceDimensions(holder, width, height)
        }
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

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }

    private fun setSurfaceDimensions (holder: SurfaceHolder, width: Int, height: Int) {
        if (width == 0 || height == 0) {
            return
        }
        val aspectRatio: kotlin.Float = height.toFloat()/width.toFloat()
        val surfaceWidth = this.width
        val surfaceHeight = (surfaceWidth * aspectRatio).toInt()
        val params = FrameLayout.LayoutParams(surfaceWidth, surfaceHeight)
        this.layoutParams = params
        player.setDisplay(holder)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // setSurfaceDimensions(holder, width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        player.release()
    }
}
