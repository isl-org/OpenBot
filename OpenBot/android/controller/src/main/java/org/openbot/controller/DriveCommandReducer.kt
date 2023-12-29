package org.openbot.controller

import android.util.Log
import org.openbot.controller.customComponents.DualDriveSeekBar
import kotlin.math.absoluteValue

object DriveCommandReducer {
    private var lastRight = 0f
    private var lastLeft = 0f
    private const val withinRange = .02f

    fun filter(rightValue: Float, leftValue: Float) {
        if (isDifferent(rightValue, leftValue)) {
            lastLeft = leftValue
            lastRight = rightValue
            val msg = "{driveCmd: {r:${rightValue}, l:${leftValue}}}"
            ConnectionSelector.getConnection().sendMessage(msg)
        }
    }

    private fun isDifferent (right: Float, left: Float): Boolean {
        if ((left - lastLeft).absoluteValue <= withinRange && (right - lastRight).absoluteValue <= withinRange) {
            return false
        }
        return true
    }
}
