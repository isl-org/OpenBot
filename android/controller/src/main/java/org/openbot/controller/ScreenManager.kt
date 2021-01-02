/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:59 p.m.
 */

package org.openbot.controller

import android.os.Handler
import android.os.Looper
import org.openbot.controller.databinding.ActivityFullscreenBinding

data class ScreenManager (val binding: ActivityFullscreenBinding) {

    private var buttonsVisible: Boolean = false

    init {
        binding.mainScreen.setupDoubleTap(::toggleButtons)
    }

    private fun toggleButtons() {
        if (buttonsVisible) {
            hideButtons()
        } else {
            showButtons()
        }
    }

    private fun hideButtons() {
        binding.botSetupButtons.hide()
        showSliders()
        buttonsVisible = false
    }

    private fun hideSliders() {
        binding.driveModeControls.hide()
    }

    private fun showSliders() {
        binding.driveModeControls.show()
    }

    private fun showButtons(milliseconds: Long) {
        showButtons()

        Handler(Looper.getMainLooper()).postDelayed({
            hideButtons()
        }, milliseconds)
    }

    private fun showButtons() {
        binding.botSetupButtons.show()

        hideSliders()
        buttonsVisible = true
    }

    fun hideControls() {
        binding.mainScreen.hide()
        binding.splashScreen.show()
    }

    fun showControls() {
        binding.splashScreen.hide()
        binding.mainScreen.show()

        showButtons(3000)
    }
}
