package com.solidjuho.opengl_oamk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.solidjuho.opengl_oamk.fragments.HomeFragment
import com.solidjuho.opengl_oamk.fragments.SettingsFragment
import com.solidjuho.opengl_oamk.fragments.StoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.solidjuho.opengl_oamk.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val storeFragment = StoreFragment()
        val settingsFragment = SettingsFragment()

        makeCurrentFragment(homeFragment)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_store -> makeCurrentFragment(storeFragment)
                R.id.ic_settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }
    }
    private fun makeCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper,fragment)
            commit()
        }
}