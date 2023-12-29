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
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import org.openbot.controller.R
import org.openbot.controller.utils.LocalEventBus

class SwitchCamera @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {
    init {
        setOnTouchListener(OnTouchListener("{command: SWITCH_CAMERA}"))
        subscribe("SWITCH_CAMERA", ::onDataReceived)
    }

    private fun onDataReceived(data: String) {
        Log.i("SwitchCamera", "Got SWITCH_CAMERA status...")
    }
}
