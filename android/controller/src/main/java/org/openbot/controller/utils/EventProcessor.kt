/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:58 p.m.
 */

package org.openbot.controller.utils

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor

object EventProcessor {
    private val eventProcessor: PublishProcessor<ProgressEvents> =
        PublishProcessor.create()

    val connectionEventFlowable = eventProcessor as Flowable<ProgressEvents>

    fun onNext(e: ProgressEvents) {
        if (eventProcessor.hasSubscribers()) {
            return eventProcessor.onNext(e)
        } else {
            Log.d("EventProcessor:onNext", "----------- No subscribers")
        }
    }

    open class ProgressEvents(var payload: String = "") {

        object Init : ProgressEvents()

        object ConnectionStarted : ProgressEvents()
        object ConnectionSuccessful : ProgressEvents()
        object ConnectionFailed : ProgressEvents()
        object StartAdvertising : ProgressEvents()
        object Disconnecting : ProgressEvents()
        object Disconnected : ProgressEvents()
        object StopAdvertising : ProgressEvents()
        object AdvertisingFailed : ProgressEvents()
    }
}