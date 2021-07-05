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
import android.widget.RelativeLayout
import org.openbot.controller.ConnectionSelector
import org.openbot.controller.utils.SensorReader
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.round

class DriveModeTiltLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val tag: String = "DriveModeTiltLayout"
    private val sensorSampler: SensorSampler = SensorSampler()
    private val g = 9.81f
    private val phoneOnTableChecker = PhoneOnTableChecker()

    init {
    }

    fun start() {
        SensorReader.start()
        sensorSampler.start()
        show()
    }

    fun stop() {
        sensorSampler.stop()
        SensorReader.stop(context)
        hide()
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = INVISIBLE
    }

    inner class PhoneOnTableChecker {

        private var lastTransmitted = 0L
        private var lastAzimuth: Float? = null
        private var lastPitch: Float? = null
        private var lastRoll: Float? = null
        private val minTimeOnTableMs = 1000L

        fun phoneOnTable(azimuth: Float?, pitch: Float?, roll: Float?): Boolean {
            if (isWithin(roll?.absoluteValue, g, 1.0f)
                && isWithin(pitch, lastPitch, 0.2f)
                && isWithin(azimuth, lastAzimuth, 0.2f)
            ) {
                if (lastTransmitted == 0L) {
                    lastTransmitted = System.currentTimeMillis()
                }
                if (System.currentTimeMillis() > (lastTransmitted + minTimeOnTableMs)) {
                    return true
                }
            } else {
                lastTransmitted = 0L
            }

            lastPitch = pitch
            lastRoll = roll
            lastAzimuth = azimuth

            return false
        }
    }

    private fun isWithin(value: Float?, desiredValue: Float?, tolerance: Float?): Boolean {
        return value!! in (desiredValue!! - tolerance!!)..(desiredValue + tolerance)
    }

    inner class SensorSampler {
        private lateinit var timer: Timer

        fun start() {
            timer = Timer()
            var isRunning = false

            val task = object : TimerTask() {
                override fun run() {
                    val azimuth = SensorReader.values.azimuth
                    val pitch = SensorReader.values.pitch
                    val roll = SensorReader.values.roll

                    if (phoneOnTableChecker.phoneOnTable(azimuth, pitch, roll)) {
                        // stop()

                        if (isRunning) {
                            ConnectionSelector.getConnection().sendMessage("{driveCmd: {l:0, r:0}}")
                            isRunning = false
                        }
                        return
                    }

                    isRunning = true
                    val sliderValues = phoneAccelerometerToDualDriveValues(azimuth, pitch, roll)

                    val msg = "{driveCmd: {l:${sliderValues.left}, r:${sliderValues.right}}}"
                    ConnectionSelector.getConnection().sendMessage(msg)
                }
            }

            timer.schedule(task, 0, 100 /*MS*/)
        }

        fun stop() {
            if (this::timer.isInitialized) {
                timer.cancel()
                ConnectionSelector.getConnection().sendMessage("{driveCmd: {l:0, r:0}}")
            }
        }

        inner class DualDriveValues(var left: Float, var right: Float) {
            private val MAX = 1.0f
            private val MIN = -1.0f

            init {
                left = clean(left)
                right = clean(right)
            }

            private fun clean(value: Float): Float {
                var ret = value

                if (value > MAX) {
                    ret = MAX
                }
                if (value < MIN) {
                    ret = MIN
                }
                return ret.round(3)
            }

            private fun Float.round(decimals: Int): Float {
                var multiplier = 1.0f
                repeat(decimals) { multiplier *= 10 }
                return round(this * multiplier) / multiplier
            }

            fun reset() {
                left = 0f
                right = 0f
            }
        }

        fun phoneAccelerometerToDualDriveValues(
            azimuth: Float?,
            pitch: Float?,
            roll: Float?
        ): DualDriveValues {
            var left = 0f
            var right = 0f
            var forwardSpeed = 0f

            if (inDeadZone(roll!!)) {
                return DualDriveValues(0f, 0f)
            }

            // get forward speed
            forwardSpeed = roll.div(g) // could be negative

            // adjust for turning
            left = forwardSpeed + (pitch?.div(g) ?: 0f) * forwardSpeed
            right = forwardSpeed - (pitch?.div(g) ?: 0f) * forwardSpeed

            var values = DualDriveValues(left, right)

            Log.i(
                tag,
                "azimuth: $azimuth, pitch: $pitch, roll: $roll -> left: ${values.left}, right: ${values.right}"
            )

            return values
        }

        private fun inDeadZone(roll: Float): Boolean {
            return isWithin(roll, 0f, 1f)
        }
    }
}
