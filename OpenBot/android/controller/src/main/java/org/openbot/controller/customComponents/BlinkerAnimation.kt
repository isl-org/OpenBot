/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:55 p.m.
 */

package org.openbot.controller.customComponents

import android.view.animation.AlphaAnimation
import android.view.animation.Animation

class BlinkerAnimation(
    private val duration: Long = 500,
    private val startAlpha: Float = 0.0f,
    private val endAlpha: Float = 1.0f
) {
    val animation: Animation

    init {
        animation = AlphaAnimation(this.startAlpha, this.endAlpha)
        animation.duration = this.duration
        animation.startOffset = 20
        animation.repeatMode = Animation.REVERSE
        animation.repeatCount = Animation.INFINITE
    }
}