package com.nativecitizens.globesurfer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment @Inject constructor(): Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_splash, container, false)


        lifecycleScope.launchWhenStarted {
            delay(4_000L)
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToSearchFragment())
        }

        return binding.root
    }
}