package com.solidjuho.opengl_oamk.game

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
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
        // Log OpenGL version and GLSL version
        Log.i("OpenGL version", GLES30.glGetString(GLES30.GL_VERSION))
        Log.i("GLSL version", GLES30.glGetString(GLES30.GL_SHADING_LANGUAGE_VERSION))

        // Set the background frame color
        GLES30.glClearColor(.3f, 1.0f, .4f, 1.0f)

        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)
        GLES30.glEnable(GLES30.GL_BLEND)

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
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Calculate delta time
        val time = SystemClock.uptimeMillis() - lastFrameTime
        lastFrameTime = SystemClock.uptimeMillis()

        // Calculate current rotation and movement based on change in time
        angle += 0.045f * time.toInt()
        dy += 0.0003f * time.toInt()
        if (dy > 1.5f) dy = -dy

        // Add scaling to view projection matrix
        val scaleMatrix = FloatArray(16)
        Matrix.setIdentityM(scaleMatrix, 0)
        Matrix.scaleM(scaleMatrix, 0, 4f, 4f, 0f)
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, scaleMatrix, 0)

        // Apply all transforms for the first square
        val rotMatrix = FloatArray(16)
        val translateMatrix = FloatArray(16)
        Matrix.setIdentityM(translateMatrix, 0)
        Matrix.translateM(translateMatrix, 0, 0f, 0f, 0f)
        Matrix.setRotateM(rotMatrix, 0, angle, 0f, 0f, -1f)
        mSquare.draw(vPMatrix, rotMatrix, translateMatrix)

        // Apply all transforms for the second square
        mSquare2.rotate(0f, 0f, angle)
        mSquare2.move(0f, dy, 0f)
        mSquare2.draw(vPMatrix)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

}
