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
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import org.json.JSONObject
import org.openbot.controller.R
import org.openbot.controller.StatusEventBus

class Sound @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    init {
        setOnTouchListener(OnTouchListener("{command: TOGGLE_SOUND}"))
        subscribe("TOGGLE_SOUND", ::onDataReceived)
    }

    private fun onDataReceived(data: String) {
        setOnOffStateConditions(data)
    }

    override fun offState() {
        setCompoundDrawablesWithIntrinsicBounds( R.drawable.volume_off_24, 0, 0, 0)
    }

    override fun onState() {
        setCompoundDrawablesWithIntrinsicBounds( R.drawable.volume_up_24, 0, 0, 0)
    }
}
