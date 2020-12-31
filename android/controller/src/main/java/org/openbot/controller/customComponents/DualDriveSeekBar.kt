/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:55 p.m.
 */
package org.openbot.controller.customComponents

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import org.openbot.controller.NearbyConnection

class DualDriveSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatSeekBar(context, attrs, defStyleAttr) {

    interface IDriveValue : (Float) -> Float {
        override operator fun invoke(x: Float): Float
    }

    private lateinit var driveValue: IDriveValue

    enum class LeftOrRight { LEFT, RIGHT }

    fun setDirection(direction: LeftOrRight) {
        setOnValueChangedListener(DriveValue(direction))
    }

    private fun setOnValueChangedListener(l: IDriveValue) {
        this.driveValue = l
    }

    inner class DriveValue(private val direction: LeftOrRight) : IDriveValue {
        override operator fun invoke(x: Float): Float {

            DriveCommandEmitter.controlInput(x, direction)
            return x
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(c: Canvas) {
        c.rotate(-90f)
        c.translate((-height).toFloat(), 0f)

        super.onDraw(c)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                this.progress = max - (max * event.y / height).toInt()
                val safeValue = ((progress - 50) / 50f).coerceIn(-1f, 1f)
                driveValue.invoke(safeValue)

                onSizeChanged(width, height, 0, 0)
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

    private fun resetToHomePosition() {
        this.progress = 50
    }

    // This object combines and throttles drive commands to the bot
    object DriveCommandEmitter {

        private var lastTransmitted: Long = System.currentTimeMillis()
        private val MIN_TIME_BETWEEN_TRANSMISSIONS = 50 // ms
        private var lastRightValue = 0f
        private var lastLeftValue = 0f

        fun controlInput(value: Float, leftOrRight: LeftOrRight) {
            if (leftOrRight == LeftOrRight.LEFT) lastLeftValue = value else lastRightValue = value

            if ((System.currentTimeMillis() - lastTransmitted) >= MIN_TIME_BETWEEN_TRANSMISSIONS) {
                val msg = "{driveCmd: {r:$lastRightValue, l:$lastLeftValue}}"
                NearbyConnection.sendMessage(msg)
                lastTransmitted = System.currentTimeMillis()
            }
        }
    }
}
