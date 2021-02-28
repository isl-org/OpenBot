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
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import org.openbot.controller.R
import org.openbot.controller.StatusEventBus

class ConnectionActiveIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : com.google.android.material.imageview.ShapeableImageView (context, attrs, defStyleAttr) {

    init {
        offState()
        subscribe("CONNECTION_ACTIVE", ::onDataReceived)
    }

    private fun onDataReceived(data: String) {
        setOnOffStateConditions(data)
    }

    protected open fun offState() {
        setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    protected open fun onState() {
        setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    protected fun subscribe(subject: String, onDataReceived: (String) -> Unit) {
        StatusEventBus.addSubject(subject)
        StatusEventBus.getProcessor(subject)?.subscribe {
            onDataReceived(it as String)
        }
    }

    protected fun setOnOffStateConditions(value: String) {
        if (value == "true") onState() else offState()
    }
}
