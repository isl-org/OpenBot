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
import org.openbot.controller.StatusEventBus
import org.openbot.controller.utils.LocalEventBus
import org.videolan.libvlc.interfaces.IVLCVout

@SuppressLint("CheckResult")
class VideoViewVlc @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
        org.videolan.libvlc.util.VLCVideoLayout(context, attrs, defStyleAttr), IVLCVout.Callback {

    private val TAG: String = "VideoView"
    private val player = VlcPlayer(context, this)

    private var serverUrl: String? = ""

    init {
        hide()

        StatusEventBus.addSubject("VIDEO_SERVER_URL")
        StatusEventBus.subscribe(this.javaClass.simpleName,"VIDEO_SERVER_URL", {
            this.serverUrl = it
        }, {
            Log.i(null, "Failed to send...")
        })

        StatusEventBus.addSubject("VIDEO_COMMAND")
        StatusEventBus.subscribe (this.javaClass.simpleName, "VIDEO_COMMAND",
            onNext = { processVideoCommand(it as String) })

        StatusEventBus.addSubject("TOGGLE_MIRROR")
        StatusEventBus.subscribe(this.javaClass.simpleName, "TOGGLE_MIRROR", onNext = {
            scaleX = if (it as String == "true") 1f else -1f // RTSP is already mirrored by default.
        })

        monitorConnection()
    }

    fun init() {
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
        show()

        if (this.serverUrl == null) {
            Log.d(TAG, "Cannot start video, serverUrl: {serverUrl}")
            return
        }
        player.start(this.serverUrl!!)
    }

    fun stop() {
        player.stop()
        hide()
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = INVISIBLE
    }

    override fun onSurfacesCreated(vlcVout: IVLCVout?) {
        Log.i(TAG, "onSurfacesCreated")
    }

    override fun onSurfacesDestroyed(vlcVout: IVLCVout?) {
        Log.i(TAG, "onSurfacesDestroyed")
    }

    private fun monitorConnection() {

        LocalEventBus.subscriber.start(
            this.javaClass.simpleName,
            {
                when (it) {
                    LocalEventBus.ProgressEvents.Disconnected -> {
                        stop()
                    }
                }
            },
            { throwable ->
                Log.d(
                    "EventsSubscription",
                    "Got error on subscribe: $throwable"
                )
            })
    }
}
