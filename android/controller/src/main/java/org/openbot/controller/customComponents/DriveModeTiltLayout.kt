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
import android.widget.RelativeLayout
import org.openbot.controller.ConnectionSelector
import org.openbot.controller.PhoneSensorToDualDriveConverter
import org.openbot.controller.utils.EventProcessor
import org.openbot.controller.utils.SensorReader
import org.openbot.controller.utils.Utils
import java.util.*
import kotlin.math.absoluteValue

class DriveModeTiltLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val tag: String = "DriveModeTiltLayout"
    private val sensorSampler: SensorSampler = SensorSampler()
    private val g = 9.81f
    private val phoneAccelerometerToDualDriveConverted =
        PhoneSensorToDualDriveConverter()

    init {
    }

    fun start() {
        SensorReader.start()
        sensorSampler.start()
        show()
    }

    fun stop() {
        // ConnectionSelector.getConnection().sendMessage("{driveCmd: {l:0, r:0}}")
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

    inner class SensorSampler {
        private lateinit var timer: Timer

        fun start() {
            if (this::timer.isInitialized) {
                timer.cancel()
            }

            timer = Timer()
            var isRunning = false

            val task = object : TimerTask() {
                override fun run() {
                    val azimuth = SensorReader.values.azimuth
                    val pitch = SensorReader.values.pitch
                    val roll = SensorReader.values.roll

                    isRunning = true
                    val sliderValues = phoneAccelerometerToDualDriveConverted.convert(azimuth, pitch, roll)

                    val msg = "{driveCmd: {l:${sliderValues.left}, r:${sliderValues.right}}}"
                    ConnectionSelector.getConnection().sendMessage(msg)
                }
            }

            timer.schedule(task, 0, 50 /*MS*/)
        }

        fun stop() {
            if (this::timer.isInitialized) {
                timer.cancel()
            }
        }
    }
}
