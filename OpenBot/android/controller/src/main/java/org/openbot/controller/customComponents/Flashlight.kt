/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:57 p.m.
 */

package org.openbot.controller.customComponents

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import org.openbot.controller.R
import org.openbot.controller.StatusEventBus

class Flashlight @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    init {
        setOnTouchListener(OnTouchListener("{command: FLASHLIGHT}"))
        subscribe("FLASHLIGHT", ::onDataReceived)
    }

    private fun onDataReceived(data: String) {
        setOnOffStateConditions(data)
    }

    override fun offState() {
        clearAnimation()
        setIconTintResource(R.color.colorPrimary)
    }

    override fun onState() {
        setIconTintResource(R.color.green)
        startAnimation(BlinkerAnimation().animation)
    }
}
