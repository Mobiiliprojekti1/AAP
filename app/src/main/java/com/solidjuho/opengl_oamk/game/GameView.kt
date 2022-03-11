package com.solidjuho.opengl_oamk.game

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log

class GameView(context: Context, attrs: AttributeSet) : GLSurfaceView(context, attrs) {
    private val renderer: GameRenderer

    init {
        Log.println(Log.INFO,"TAG_TEST","GameView init")
        setEGLContextClientVersion(3)
        renderer = GameRenderer(context)
        setRenderer(renderer)
    }
}