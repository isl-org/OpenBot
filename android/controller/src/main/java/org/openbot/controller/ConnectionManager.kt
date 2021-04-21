package org.openbot.controller

object ConnectionManager {
    fun get () :ILocalConnection {
        return NearbyConnection
        // return NetworkServiceConnection
    }
}