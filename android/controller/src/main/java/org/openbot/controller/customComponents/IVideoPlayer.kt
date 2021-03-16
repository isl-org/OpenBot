package org.openbot.controller.customComponents

interface IVideoPlayer {
    open fun init()

    open fun start(url: String)

    open fun stop()
}

