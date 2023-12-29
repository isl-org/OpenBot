/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:57 p.m.
 */

package org.openbot.controller.customComponents

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import org.json.JSONObject
import org.openbot.controller.ConnectionSelector
import org.openbot.controller.R
import org.openbot.controller.StatusEventBus
import org.openbot.controller.utils.LocalEventBus
import kotlin.system.exitProcess

class Sound @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    enum class State { ON, OFF }
    var state: State = State.OFF

    init {
        setOnTouchListener(OnTouchListener())
        offState()
    }

    inner class OnTouchListener : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event?.action == MotionEvent.ACTION_DOWN) {
                if (state == State.OFF) {
                    state = State.ON
                    onState()
                } else {
                    state = State.OFF
                    offState()
                }
            }
            return true
        }
    }

    override fun offState() {
        setCompoundDrawablesWithIntrinsicBounds( R.drawable.volume_off_24, 0, 0, 0)

        val event: LocalEventBus.ProgressEvents = LocalEventBus.ProgressEvents.Mute
        LocalEventBus.onNext(event)
    }

    override fun onState() {
        setCompoundDrawablesWithIntrinsicBounds( R.drawable.volume_up_24, 0, 0, 0)
        val event: LocalEventBus.ProgressEvents = LocalEventBus.ProgressEvents.Unmute
        LocalEventBus.onNext(event)
    }
}
