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
import androidx.annotation.IntRange
import org.openbot.controller.ForwardSpeed
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

class Brake @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ButtonWithBorder(context, attrs, defStyleAttr) {

    data class DrivingCharacteristics constructor(
        @IntRange(
            from = 0,
            to = 5000
        ) val timeToReverse: Long = 1000, // time before going into reverse.

        @IntRange(
            from = 1,
            to = 10
        ) val stepsToFinalValue: Int = 3, // in haw many steps do we get to min value
    )

    init {
        offState()
    }

    private val drivingCharacteristics: DrivingCharacteristics = DrivingCharacteristics()
    private lateinit var deceleratorStepTask: DeceleratorStepTask

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onState()
                ForwardSpeed.reset()
                val decelerationPeriod =
                    drivingCharacteristics.timeToReverse / drivingCharacteristics.stepsToFinalValue
                val decrementValue =
                    (ForwardSpeed.minNegative / drivingCharacteristics.stepsToFinalValue).absoluteValue

                deceleratorStepTask = DeceleratorStepTask(decrementValue)
                deceleratorStepTask.schedule(
                    drivingCharacteristics.timeToReverse,
                    decelerationPeriod
                )
            }

            MotionEvent.ACTION_UP -> {
                offState()
                deceleratorStepTask.cancel()
                ForwardSpeed.reset()
            }
        }
        return true
    }

    inner class DeceleratorStepTask(decrementValue: Float) {
        private var executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
        private lateinit var runningTask: ScheduledFuture<*>

        private val task = Runnable {
            ForwardSpeed.decrementNegative(decrementValue)
        }

        fun schedule(delay: Long, period: Long) {
            this.runningTask =
                executor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS)
        }

        fun cancel() {
            if (this::runningTask.isInitialized && !runningTask.isCancelled) {
                this.runningTask.cancel(false)
            }
        }
    }
}
