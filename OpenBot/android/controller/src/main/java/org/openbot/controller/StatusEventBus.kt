/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: 2020-12-27, 10:59 p.m.
 */

package org.openbot.controller

import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

object StatusEventBus {
    private val subjects = HashMap<String, PublishSubject<String?>>()
    private val subscribers = HashMap<String, LinkedHashSet<String>> ()

    fun addSubject(name: String) {
        if (subjects[name] != null) {
            return
        }
        val subject: PublishSubject<String?> = PublishSubject.create()
        subjects[name] = subject
    }

    private fun addSubscriberAndSubject(subscriber: String, subject: String) {
        if (!subscribers.containsKey(subscriber)) {
            val subjectsForThisSubscriber = LinkedHashSet<String>()
            subscribers[subscriber] = subjectsForThisSubscriber
        }

        val subjectsForThisSubscriber =  subscribers[subscriber]
        if (!subjectsForThisSubscriber?.contains(subject)!!) {
            subjectsForThisSubscriber?.add(subject)
        }
    }

    private fun subscriberAlreadySubscribed(subscriber: String, subject: String): Boolean {
        if (!subscribers.containsKey(subscriber)) {
            return false
        }

        val subjectsForThisSubscriber = subscribers[subscriber]
        if (subjectsForThisSubscriber == null || !subjectsForThisSubscriber.contains(subject)) {
            return false
        }

        return true
    }

    fun subscribe(subscriberName: String, subject: String, onNext: Consumer<in String?>) {
        if (!subscriberAlreadySubscribed(subscriberName, subject)) {
            getProcessor(subject)?.subscribe(onNext)
            addSubscriberAndSubject(subscriberName, subject)
        }
    }

    fun subscribe(subscriberName: String, subject: String, onNext: Consumer<in String?>, onError: Consumer<in Throwable>) {
        if (!subscriberAlreadySubscribed(subscriberName, subject)) {
            getProcessor(subject)?.subscribe(onNext, onError)
            addSubscriberAndSubject(subscriberName, subject)
        }
    }

    private fun getProcessor(name: String): PublishSubject<String?>? {
        return subjects[name]
    }

    fun emitEvent(name: String, event: String) {
        subjects[name]?.onNext(event)
    }
}