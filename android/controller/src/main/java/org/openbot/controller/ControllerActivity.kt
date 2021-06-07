/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:59 p.m.
 */

package org.openbot.controller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.openbot.controller.customComponents.DualDriveSeekBar
import org.openbot.controller.customComponents.VideoViewVlc
import org.openbot.controller.customComponents.VideoViewWebRTC
import org.openbot.controller.databinding.ActivityFullscreenBinding
import org.openbot.controller.utils.EventProcessor
import org.openbot.controller.utils.Utils
import kotlin.system.exitProcess

class ControllerActivity : /*AppCompat*/ Activity() { // for some reason AppCompatActivity gives errors in the IDE, but it does compile,
    private val PERMISSION_REQUEST_LOCATION = 101
    private val TAG = "ControllerActivity"
    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var screenManager: ScreenManager

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermissions()

        screenManager = ScreenManager(binding)
        ConnectionSelector.init(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        createAppEventsSubscription()
        subscribeToStatusInfo()

        binding.leftDriveControl.setDirection(DualDriveSeekBar.LeftOrRight.LEFT)
        binding.rightDriveControl.setDirection(DualDriveSeekBar.LeftOrRight.RIGHT)

        screenManager.hideControls()
        hideSystemUI()

        BotDataListener.init()

        subscribe("VIDEO_PROTOCOL", ::onDataReceived)
    }

    @SuppressLint("CheckResult")
    private fun subscribe(subject: String, onDataReceived: (String) -> Unit) {
        StatusEventBus.addSubject(subject)
        StatusEventBus.getProcessor(subject)?.subscribe {
            onDataReceived(it as String)
        }
    }

    private fun onDataReceived(data: String) {
        // Create the type of video view programmatically based on info from the Bot app.
        when (data) {
            "WEBRTC" -> {
                val view: VideoViewWebRTC = createView(VideoViewWebRTC(this)) as VideoViewWebRTC
                view.init()
            }
            "RTSP" -> {
                val view: VideoViewVlc = createView(VideoViewVlc(this)) as VideoViewVlc
                view.init()
            }
        }
    }

    private fun createView(view: View): View {
        view.id = R.id.video_view

        val layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        view.layoutParams = layoutParams

        binding.video.addView(view)
        return view
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to get location denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_LOCATION
        )
    }

    @SuppressLint("CheckResult")
    private fun subscribeToStatusInfo() {
        StatusEventBus.addSubject("CONNECTION_ACTIVE")
        StatusEventBus.getProcessor("CONNECTION_ACTIVE")?.subscribe {
            if (it.toBoolean()) screenManager.showControlsImmediately() else screenManager.hideControls()
        }
    }

    private fun createAppEventsSubscription(): Disposable =
            EventProcessor.connectionEventFlowable
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        Log.i(TAG, "Got $it event")

                        when (it) {
                            EventProcessor.ProgressEvents.ConnectionSuccessful -> {
                                Utils.beep()
                                screenManager.showControls()
                            }
                            EventProcessor.ProgressEvents.ConnectionStarted -> {
                            }
                            EventProcessor.ProgressEvents.ConnectionFailed -> {
                                screenManager.hideControls()
                            }
                            EventProcessor.ProgressEvents.StartAdvertising -> {
                                screenManager.hideControls()
                            }
                            EventProcessor.ProgressEvents.Disconnected -> {
                                screenManager.hideControls()
                                ConnectionSelector.getConnection().connect(this)
                            }
                            EventProcessor.ProgressEvents.StopAdvertising -> {
                            }
                            EventProcessor.ProgressEvents.TemporaryConnectionProblem -> {
                                screenManager.hideControls()
                                ConnectionSelector.getConnection().connect(this)
                            }
                            EventProcessor.ProgressEvents.AdvertisingFailed -> {
                                screenManager.hideControls()
                            }
                        }
                    }
                    .subscribe(
                            { },
                            { throwable ->
                                Log.d(
                                        "EventsSubscription",
                                        "Got error on subscribe: $throwable"
                                )
                            })

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                window.navigationBarColor = getColor(R.color.colorPrimaryDark)
                it.hide(WindowInsets.Type.systemBars())
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    @Override
    override fun onPause() {
        super.onPause()
        ConnectionSelector.getConnection().disconnect()
    }

    @Override
    override fun onResume() {
        super.onResume()
        hideSystemUI()

        ConnectionSelector.getConnection().init(this)
        ConnectionSelector.getConnection().connect(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                    finish()
                    exitProcess(0)
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }

    companion object
}
