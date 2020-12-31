/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:59 p.m.
 */

package org.openbot.controller

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_fullscreen.*
import org.openbot.controller.customComponents.DualDriveSeekBar
import org.openbot.controller.utils.EventProcessor
import org.openbot.controller.utils.Utils

@Suppress("DEPRECATION")
class ControllerActivity : /*AppCompat*/ Activity() { // for some reason AppCompatActivity gives errors in the IDE, but it does compile,
    private val TAG = "OpenbotControllerActivity"
    private var buttonsVisible: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        createAppEventsSubscription()

        leftDriveControl.setDirection(DualDriveSeekBar.LeftOrRight.LEFT)
        rightDriveControl.setDirection(DualDriveSeekBar.LeftOrRight.RIGHT)

        hideControls()
        hideSystemUI()

        mainScreen.setupDoubleTap(::toggleButtons)
        BotDataListener.init()
    }

    private fun toggleButtons() {
        if (buttonsVisible) {
            hideButtons()
        } else {
            showButtons()
        }
    }

    private fun hideButtons() {
        botSetupButtons.hide()
        showSliders()
        buttonsVisible = false
    }

    private fun hideSliders() {
        driveModeControls.hide()
    }

    private fun showSliders() {
        driveModeControls.show()
    }

    private fun showButtons(milliseconds: Long) {
        showButtons()

        Handler().postDelayed({
            hideButtons()
        }, milliseconds)
    }

    private fun showButtons() {
        botSetupButtons.show()

        hideSliders()
        buttonsVisible = true
    }

    private fun hideControls() {
        mainScreen.hide()
        splashScreen.show()
    }

    private fun showControls() {
        splashScreen.hide()
        mainScreen.show()

        showButtons(3000)
    }

    private fun createAppEventsSubscription(): Disposable =
            EventProcessor.connectionEventFlowable
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        Log.i(TAG, "Got ${it} event")

                        when (it) {
                            EventProcessor.ProgressEvents.ConnectionSuccessful -> {
                                Utils.beep()
                                showControls()
                            }
                            EventProcessor.ProgressEvents.ConnectionStarted -> {
                            }
                            EventProcessor.ProgressEvents.ConnectionFailed -> {
                                hideControls()
                            }
                            EventProcessor.ProgressEvents.StartAdvertising -> {
                                hideControls()
                            }
                            EventProcessor.ProgressEvents.Disconnected -> {
                                hideControls()
                                NearbyConnection.connect(this)
                            }
                            EventProcessor.ProgressEvents.StopAdvertising -> {
                            }
                            EventProcessor.ProgressEvents.AdvertisingFailed -> {
                                hideControls()
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
        NearbyConnection.disconnect()
    }

    @Override
    override fun onResume() {
        super.onResume()
        hideSystemUI()
        NearbyConnection.connect(this)
    }

    companion object
}
