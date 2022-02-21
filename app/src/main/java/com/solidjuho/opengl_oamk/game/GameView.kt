package com.solidjuho.opengl_oamk.game

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log

class GameView(context:Context) : GLSurfaceView(context){
    private val renderer : GameRenderer

    init {
        Log.println(Log.INFO,"TAG_TEST","GameView init")
        setEGLContextClientVersion(2)
        renderer = GameRenderer()
        setRenderer(renderer)
    }
}