package com.solidjuho.opengl_oamk.game.draw

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

var squareCoords = floatArrayOf(
    // in counterclockwise order:
    -0.1f, 0.1f, 0.0f,   // top left
    -0.1f, -0.1f, 0.0f,  // bottom left
    0.1f, 0.1f, 0.0f,   // top right
    -0.1f, -0.1f, 0.0f,  // bottom left
    0.1f, -0.1f, 0.0f,  // bottom right
    0.1f, 0.1f, 0.0f,   // top right
)

var squareTextureCoords = floatArrayOf(
    0.0f, 0.0f,
    0.0f, 1.0f,
    1.0f, 0.0f,
    0.0f, 1.0f,
    1.0f, 1.0f,
    1.0f, 0.0f,
)

class Square {

    private val vertexShaderCode =
        """
            #version 300 es
            uniform mat4 uMVPMatrix;
            uniform mat4 uRotationMatrix;
            uniform mat4 uTranslateMatrix;
            in vec4 vPosition;
            
            in vec2 aTexCoordinates;
            out vec2 vTexCoordinates; 
            
            void main() {
                gl_Position = uTranslateMatrix * uMVPMatrix * uRotationMatrix * vPosition;
                vTexCoordinates = aTexCoordinates;
            }
        """.trimIndent()

    private val fragmentShaderCode =
        """
            #version 300 es
            precision mediump float;
            uniform vec4 vColor;
            uniform sampler2D uTexture;
            
            in vec2 vTexCoordinates;
            layout(location=0) out vec4 fragColor;
            
            void main() {
                fragColor = texture(uTexture, vTexCoordinates) * vColor;
            }
        """.trimIndent()

    private fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        return GLES30.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
        }
    }

    fun loadImage(context: Context, id: Int) {
        val textureBuffer = IntArray(1)
        GLES30.glGenTextures(1, textureBuffer, 0)

        if (textureBuffer[0] != 0) {

            val bitmap = BitmapFactory.decodeResource(context.resources, id)

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureBuffer[0])

            GLES30.glTexParameteri(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MAG_FILTER,
                GLES30.GL_NEAREST
            )
            GLES30.glTexParameteri(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MIN_FILTER,
                GLES30.GL_NEAREST
            )

            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)

            bitmap.recycle()
        }

        this.textureBuffer = textureBuffer
    }

    private val mProgram: Int
    private lateinit var textureBuffer: IntArray

    init {
        val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES30.glCreateProgram().also {

            // add the vertex shader to program
            GLES30.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES30.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES30.glLinkProgram(it)
        }
    }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
    val color = floatArrayOf(1f, 1f, 1f, 1.0f)
    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(squareCoords.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(squareCoords)
                // set the buffer to read the first coordinate
                position(0)
            }
        }


    private var textureCordBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(squareTextureCoords.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(squareTextureCoords)
                position(0)
            }

        }

    private var uPMatrixHandle: Int = 0
    private var uRotationMatrixHandle: Int = 0
    private var uTranslateMatrixHandle: Int = 0
    private var uTextureHandle: Int = 0
    private var aTexCoordinatesHandle: Int = 0

    fun draw(mvpMatrix: FloatArray, rotationMatrix: FloatArray, translateMatrix: FloatArray) {
        rotMatrix = rotationMatrix
        translationMatrix = translateMatrix
        draw(mvpMatrix)
    }

    fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES30.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES30.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                GLES30.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Get shape's transform matrix handle
            uPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
            uRotationMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uRotationMatrix")
            uTranslateMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uTranslateMatrix")

            // pass the projection and view transformation to the shape
            GLES30.glUniformMatrix4fv(uPMatrixHandle, 0, false, mvpMatrix, 0)
            GLES30.glUniformMatrix4fv(uRotationMatrixHandle, 0, false, rotMatrix, 0)
            GLES30.glUniformMatrix4fv(uTranslateMatrixHandle, 0, false, translationMatrix, 0)

            // Set textures
            uTextureHandle = GLES30.glGetUniformLocation(mProgram, "uTexture")
            aTexCoordinatesHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoordinates")

            GLES30.glVertexAttribPointer(
                aTexCoordinatesHandle,
                2,
                GLES30.GL_FLOAT,
                false,
                0,
                textureCordBuffer
            )
            GLES30.glEnableVertexAttribArray(aTexCoordinatesHandle)

            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureBuffer[0])
            GLES30.glUniform1i(uTextureHandle, 0)

            // Draw the triangle
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES30.glDisableVertexAttribArray(it)
            GLES30.glDisableVertexAttribArray(aTexCoordinatesHandle)
        }
    }

    private var rotMatrix = FloatArray(16)
    private var translationMatrix = FloatArray(16)

    fun rotate(x: Float, y: Float, z: Float) {
        Matrix.setRotateEulerM(rotMatrix, 0, x, y, z)
    }

    fun move(x: Float, y: Float, z: Float) {
        Matrix.setIdentityM(translationMatrix, 0)
        Matrix.translateM(translationMatrix, 0, x, y, z)
    }

}

