package com.solidjuho.opengl_oamk.game

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.solidjuho.opengl_oamk.databinding.ActivityGameBinding

class GameActivity : Activity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var fullscreenContent: GLSurfaceView
    private lateinit var fullscreenContentControls: LinearLayout
    private lateinit var gameView: GameView

    private var isFullscreen: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent
        fullscreenContent.setOnClickListener { toggle() }

        fullscreenContentControls = binding.fullscreenContentControls

        //OpenGL stuff
        gameView = GameView(this)


    }


    private fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
//        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = false
    }

    private fun show() {
        // Show the system bar
        isFullscreen = true

    }


    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

    }
}