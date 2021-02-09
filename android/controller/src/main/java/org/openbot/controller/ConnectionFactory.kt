package org.openbot.controller

object ConnectionFactory {
    public fun get () :ILocalConnection {
        return NearbyConnection
        // return WiFiDirectConnection
    }
}