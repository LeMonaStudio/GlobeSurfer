package com.nativecitizens.globesurfer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nativecitizens.globesurfer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class MainActivity @Inject constructor(): AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setTheme(R.style.Theme_GlobeSurfer)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}