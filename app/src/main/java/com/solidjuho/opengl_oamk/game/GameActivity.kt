package com.solidjuho.opengl_oamk.game

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.solidjuho.opengl_oamk.R
import com.solidjuho.opengl_oamk.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var fullscreenContent: GameView
    private lateinit var fullscreenContentControls: LinearLayout

    private var isFullscreen: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFullscreen = true
        hideSystemBars()

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.openGlCanvas
        fullscreenContent.setOnClickListener {
            Log.d("GLScreen", "Touch")
            toggle()
        }

        binding.wireframeBtn.setOnClickListener {
            fullscreenContent.toggleWireframe()
            binding.wireframeBtn.text = String.format(
                resources.getString(R.string.wireframe_button),
                if (fullscreenContent.getWireframeState()) "On" else "Off",
            )
        }
        binding.wireframeBtn.text = String.format(
            resources.getString(R.string.wireframe_button),
            if (fullscreenContent.getWireframeState()) "On" else "Off",
        )

        binding.transparencyBtn.setOnClickListener {
            fullscreenContent.toggleTransparency()
            binding.transparencyBtn.text = String.format(
                resources.getString(R.string.transparency_button),
                if (fullscreenContent.getTransparencyState()) "On" else "Off",
            )
        }
        binding.transparencyBtn.text = String.format(
            resources.getString(R.string.transparency_button),
            if (fullscreenContent.getTransparencyState()) "On" else "Off",
        )

        fullscreenContentControls = binding.fullscreenContentControls
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
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = false
    }

    private fun show() {
        // Show the system bar
        fullscreenContentControls.visibility = View.VISIBLE
        isFullscreen = true
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun showSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }
}