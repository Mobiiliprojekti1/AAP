package com.solidjuho.opengl_oamk.game

import android.content.Context
import android.opengl.GLES32
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import com.solidjuho.opengl_oamk.R
import com.solidjuho.opengl_oamk.game.draw.Square
import com.solidjuho.opengl_oamk.game.draw.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GameRenderer(context: Context) : GLSurfaceView.Renderer {
    private val mContext: Context = context
    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square
    private lateinit var mSquare2: Square

    private var vPMatrix = FloatArray(16)
    private var projectionMatrix = FloatArray(16)
    private var viewMatrix = FloatArray(16)

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES32.glClearColor(.3f, 1.0f, .4f, 1.0f)

        GLES32.glBlendFunc(GLES32.GL_SRC_ALPHA, GLES32.GL_ONE_MINUS_SRC_ALPHA)
        GLES32.glEnable(GLES32.GL_BLEND)

        // initialize a triangle
        mTriangle = Triangle()

        mSquare = Square()
        mSquare.loadImage(mContext, R.raw.space_ship)

        mSquare2 = Square()
        mSquare2.loadImage(mContext, R.raw.space_ship)
    }

    private var lastFrameTime = SystemClock.uptimeMillis()
    private var angle: Float = 0f
    private var dy: Float = 0f

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT)

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        val time = SystemClock.uptimeMillis() - lastFrameTime
        lastFrameTime = SystemClock.uptimeMillis()
        angle += 0.045f * time.toInt()
        dy += 0.0003f * time.toInt()
        if (dy > 1.5f) dy = -dy
        val rotMatrix = FloatArray(16)
        val translateMatrix = FloatArray(16)
        Matrix.setIdentityM(translateMatrix, 0)
        Matrix.translateM(translateMatrix, 0, 0f, 0f, 0f)
        Matrix.setRotateM(rotMatrix, 0, angle, 0f, 0f, -1f)

        val scaleMatrix = FloatArray(16)
        Matrix.setIdentityM(scaleMatrix, 0)
        Matrix.scaleM(scaleMatrix, 0, 4f, 4f, 0f)
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, scaleMatrix, 0)

        mSquare.draw(vPMatrix, rotMatrix, translateMatrix)

        Matrix.setRotateM(rotMatrix, 0, 180f, 0f, 0f, 1f)
        Matrix.translateM(translateMatrix, 0, 0f, 0.5f, 0f)

        mSquare2.draw(vPMatrix, rotMatrix, translateMatrix)

        Matrix.setRotateM(rotMatrix, 0, 0f, 0f, 0f, 1f)
        Matrix.translateM(translateMatrix, 0, 0f, -1f, 0f)

        Matrix.setIdentityM(scaleMatrix, 0)
        Matrix.scaleM(scaleMatrix, 0, 0.1f, 0.1f, 0f)
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, scaleMatrix, 0)

        mTriangle.draw(vPMatrix, rotMatrix, translateMatrix)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

}
