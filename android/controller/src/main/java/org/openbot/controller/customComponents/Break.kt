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

class Break @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ButtonWithBorder(context, attrs, defStyleAttr) {

    data class DrivingCharacteristics constructor(
        @IntRange(
            from = 0,
            to = 5000
        ) val decelerationTime: Long = 300, // how many milliseconds to decelerate to min speed.

        @IntRange(
            from = 1,
            to = 10
        ) val stepsToFullStop: Int = 3, // in haw many steps to get to min speed after releasing the accelerator
    )

    private val drivingCharacteristics: DrivingCharacteristics = DrivingCharacteristics()

    private lateinit var deceleratorStepTask: DeceleratorStepTask

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onState()

                val decelerationPeriod = drivingCharacteristics.decelerationTime / drivingCharacteristics.stepsToFullStop
                val decrementValue = if (ForwardSpeed.value <= 0) { .1f } else { (ForwardSpeed.value / drivingCharacteristics.stepsToFullStop).toFloat() }

                deceleratorStepTask = DeceleratorStepTask(decrementValue)
                deceleratorStepTask.schedule(decelerationPeriod)
            }

            MotionEvent.ACTION_UP -> {
                offState()
                deceleratorStepTask.cancel()
            }
        }
        return true
    }

    inner class DeceleratorStepTask(decrementValue: Float) {
        private var executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
        private lateinit var runningTask: ScheduledFuture<*>

        private val task = Runnable {
            ForwardSpeed.decrement(decrementValue)
        }

        fun schedule(delay: Long) {
            this.runningTask = executor.scheduleAtFixedRate(task, 0, delay, TimeUnit.MILLISECONDS)
        }

        fun cancel() {
            if (this::runningTask.isInitialized && !runningTask.isCancelled) {
                this.runningTask.cancel(false)
            }
        }
    }

}
