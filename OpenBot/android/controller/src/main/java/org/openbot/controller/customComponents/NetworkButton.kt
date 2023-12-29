/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:58 p.m.
 */

package org.openbot.controller.customComponents

import android.content.Context
import android.util.AttributeSet

class NetworkButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    init {
        setOnTouchListener(OnTouchListener("{command: NETWORK}"))
        subscribe("NETWORK", ::onDataReceived)
        offState()
    }

    private fun onDataReceived(data: String) {
        setOnOffStateConditions(data)
    }
}
