package org.openbot.controller

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.FloatRange
import kotlin.math.roundToInt

object ForwardSpeed {
    const val max = 1f
    const val min = 0f
    const val minNegative = -1f

    @FloatRange(from=min.toDouble(),to=max.toDouble())
    var value = 0f

    fun increment(incrementValue: Float) {
        value = max.coerceAtMost(value + incrementValue).round(2)
    }

    fun decrement(decrementValue: Float) {
        value = min.coerceAtLeast(value - decrementValue).round(2)
    }

    fun decrementNegative(decrementValue: Float) {
        value = minNegative.coerceAtLeast(value - decrementValue).round(2)
    }

    fun reset() {
        value = 0f
    }

    fun setTo(minSpeed: Float) {
        if (minSpeed !in -1f..1f) {
            Log.e("ForwardSpeed", "setTo () got invalid parameter $minSpeed")
            return
        }
        value = minSpeed
    }

    fun isMax(): Boolean {
        return value >= max
    }

    fun isMin(): Boolean {
        return value <= min
    }

    private fun Float.round(decimals: Int): Float {
        var multiplier:Int = 1
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }
}