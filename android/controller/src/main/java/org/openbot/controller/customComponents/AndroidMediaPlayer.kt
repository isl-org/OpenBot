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
import android.media.AudioAttributes.*
import android.media.MediaPlayer
import android.util.Log
import android.view.SurfaceHolder
import java.io.IOException

data class AndroidMediaPlayer(
        val context: Context, val videoView: VideoView
) : IVideoPlayer {
    private lateinit var surfaceSizeChangeCallback: (SurfaceHolder, Int, Int) -> Unit
    private val TAG: String = "AndroidMediaPlayer"

    private var mPlayer: MediaPlayer? = null

    init {
    }

    override fun init() {
    }

    fun startMediaPlayer(url: String?) {

        mPlayer = MediaPlayer()
        val builder = Builder().setUsage(USAGE_MEDIA).setContentType(CONTENT_TYPE_MOVIE)
        mPlayer?.setAudioAttributes(builder.build())

        try {
            mPlayer?.reset()
            mPlayer?.setDataSource(url)

            mPlayer?.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
                if (extra == MediaPlayer.MEDIA_ERROR_SERVER_DIED
                        || extra == MediaPlayer.MEDIA_ERROR_MALFORMED) {
                } else if (extra == MediaPlayer.MEDIA_ERROR_IO) {
                    return@OnErrorListener false
                }
                false
            })
            mPlayer?.setOnVideoSizeChangedListener { player, width, height ->
                Log.d(TAG, "width: $width, height: $height")
                surfaceSizeChangeCallback(videoView.holder, width, height)
            }

            mPlayer?.setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener { mp, percent -> Log.e("*** onBufferingUpdate", "" + percent) })
            mPlayer?.setOnPreparedListener(MediaPlayer.OnPreparedListener {
                mPlayer?.start()
            })
            mPlayer?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                Log.e("onCompletion", "Yes")
                it.release()
            })
            mPlayer?.setOnInfoListener(MediaPlayer.OnInfoListener { mp, what, extra -> false })
            try {
                mPlayer?.prepareAsync()
            } catch (e: java.lang.IllegalStateException) {
                Log.d(TAG, "Got: $e")
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun start(url: String) {
        // this.startMediaPlayer("rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov")
        this.startMediaPlayer(url)
    }

    override fun stop() {
        try {
            mPlayer?.stop()
        } catch (e: Exception) {
            Log.d(TAG, "Got exception $e trying to stop player.")
        }
    }

    override fun release() {
        try {
            mPlayer?.reset()
            mPlayer?.release()
        } catch (e: Exception) {
            Log.d(TAG, "Got exception $e trying to release player.")
        }
    }

    override fun setDisplay(holder: SurfaceHolder?) {
        this.mPlayer?.setDisplay(holder)
    }

    override fun setSurfaceChangedCallback(surfaceChangedCallback: (SurfaceHolder, Int, Int) -> Unit) {
        this.surfaceSizeChangeCallback = surfaceChangedCallback
    }
}
