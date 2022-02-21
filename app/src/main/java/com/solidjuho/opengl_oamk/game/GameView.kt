package com.solidjuho.opengl_oamk.game

import android.content.Context
import android.opengl.GLSurfaceView

class GameView(context:Context) : GLSurfaceView(context){
    private val renderer : GameRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = GameRenderer()
        setRenderer(renderer)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}