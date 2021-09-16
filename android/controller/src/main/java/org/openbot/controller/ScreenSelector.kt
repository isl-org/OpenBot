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
import android.util.Log
import org.openbot.controller.databinding.ActivityFullscreenBinding
import org.openbot.controller.utils.LocalEventBus

data class ScreenSelector (val binding: ActivityFullscreenBinding) {

    init {
        binding.mainScreen.setupDoubleTap(::showButtons)
        monitorDriveMode()
    }

    private fun hideButtons() {
        binding.botSetupButtons?.hide()
    }

    private fun hideSliders() {
        binding.driveModeSlidersLayout.hide()
        binding.controlModeTiltLayout.hide()
    }

    private fun showButtons() {
        binding.controlModeTiltLayout.stop()
        binding.botSetupButtons?.show()
        hideSliders()
    }

    fun hideControls() {
        binding.mainScreen.hide()
        binding.splashScreen.show()
    }

    fun showControls() {
        binding.splashScreen.hide()
        binding.mainScreen.show()

        showButtons()
    }

    @SuppressLint("CheckResult")
    private fun subscribe(subject: String, onDataReceived: (String) -> Unit) {
        StatusEventBus.addSubject(subject)
        StatusEventBus.subscribe(this.javaClass.simpleName, subject, onNext = {
            onDataReceived(it as String)
        })
    }

    private fun monitorDriveMode() {

        LocalEventBus.subscriber.start(
            this.javaClass.simpleName,
            {
                when (it) {
                    LocalEventBus.ProgressEvents.TiltControl -> {
                        binding.driveModeSlidersLayout.hide()
                        binding.controlModeTiltLayout.start()
                        binding.doubleTapMessage?.start()
                    }
                    LocalEventBus.ProgressEvents.SlidersControl -> {
                        binding.driveModeSlidersLayout.show()
                        binding.controlModeTiltLayout.stop()
                    }
                }

                binding.botSetupButtons?.hide()
            },
            { throwable ->
                Log.d(
                    "EventsSubscription",
                    "Got error on subscribe: $throwable"
                )
            })
    }
}
