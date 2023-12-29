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
import android.view.MotionEvent
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import org.openbot.controller.ForwardSpeed
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class Accelerator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ButtonWithBorder(context, attrs, defStyleAttr) {

    data class DrivingCharacteristics constructor(
        @IntRange(
            from = 0,
            to = 20000
        ) val accelerationTime: Long = 1000, // time to full acceleration after the user has pressed teh button

        @IntRange(
            from = 1,
            to = 10
        ) val stepsToFullAcceleration: Int = 5, // in haw many steps we like to get to max acceleration

        val decelerateAfterReleasingTheButton: Boolean = true, // do we want to slow down when the button is released?
        @IntRange(
            from = 0,
            to = 5000
        ) val decelerationTime: Long = 1000, // how many milliseconds to decelerate to min speed.

        @FloatRange(
            from = 0.0,
            to = 1.0
        ) val minSpeed: Float = .0f, // what percentage of full speed is our final decelerated speed (.1 means 10%)
    )

    private val drivingCharacteristics: DrivingCharacteristics = DrivingCharacteristics()
    private lateinit var acceleratorStepTask: AcceleratorStepTask

    init {
        offState()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onState()

                ForwardSpeed.reset()

                val accelerationPeriod = drivingCharacteristics.accelerationTime / drivingCharacteristics.stepsToFullAcceleration
                val incrementValue = ((ForwardSpeed.max - ForwardSpeed.value) / drivingCharacteristics.stepsToFullAcceleration)

                acceleratorStepTask = AcceleratorStepTask(incrementValue)
                acceleratorStepTask.schedule(accelerationPeriod)
            }

            MotionEvent.ACTION_UP -> {
                offState()
                acceleratorStepTask.cancel()
                ForwardSpeed.setTo (drivingCharacteristics.minSpeed)
            }
        }
        return true
    }

    inner class AcceleratorStepTask(incrementValue: Float) {
        private var executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
        private lateinit var runningTask: ScheduledFuture<*>

        private val task = Runnable {
            ForwardSpeed.increment(incrementValue)
        }

        fun schedule(period: Long) {
            this.runningTask = executor.scheduleAtFixedRate(task, 0, period, TimeUnit.MILLISECONDS)
        }

        fun cancel() {
            if (this::runningTask.isInitialized && !runningTask.isCancelled) {
                this.runningTask.cancel(false)
            }
        }
    }
}
