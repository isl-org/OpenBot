package org.openbot.controller.customComponents

import android.view.SurfaceHolder

interface IVideoPlayer {
    open fun init()

    open fun start(url: String)

    open fun stop()
    open fun release()
    open fun setDisplay(holder: SurfaceHolder?)
    open fun setSurfaceChangedCallback(surfaceChangedCallback: (SurfaceHolder, Int, Int) -> Unit)
}

