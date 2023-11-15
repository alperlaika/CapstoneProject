package com.laikaalper.florist_app.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.databinding.FragmentSplashBinding
import com.laikaalper.florist_app.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)

    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        viewModel.splashState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SplashState.GoToSignIn -> {
                    MainActivity.navigate(R.id.push_sign_in_fragment)
                }

                SplashState.GoToHome -> {
                    MainActivity.navigate(R.id.push_home_fragment)
                }
            }
        }
    }
}