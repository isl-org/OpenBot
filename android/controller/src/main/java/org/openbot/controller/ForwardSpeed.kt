package org.openbot.controller

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.FloatRange
import kotlin.math.roundToInt

object ForwardSpeed {
    private const val max = 1.0f
    private const val min = 0.0f

    @FloatRange(from=min.toDouble(),to=max.toDouble())
    var value = 0f

    fun increment(incrementValue: Float) {
        value = max.coerceAtMost(value + incrementValue).round(2)
        //Log.i("ForwardSpeed", "^^^^^ increment: current value: $value")
    }

    fun decrement(decrementValue: Float) {
        value = min.coerceAtLeast(value - decrementValue).round(2)
        //Log.i("ForwardSpeed", "vvvvvv decrement: current value: $value")
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