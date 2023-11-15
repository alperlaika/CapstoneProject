package com.laikaalper.florist_app.ui.success

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.databinding.FragmentSuccessBinding
import com.laikaalper.florist_app.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessFragment : Fragment(R.layout.fragment_success) {

    private val binding by viewBinding(FragmentSuccessBinding::bind)
    private val viewModel by viewModels<SuccessViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAnim()
        setupAnim2()
        with(binding) {
            btnHome.setOnClickListener {
                val firebaseAuth = FirebaseAuth.getInstance()
                val userId = firebaseAuth.currentUser?.uid.toString()
                viewModel.clearCart(userId)
                MainActivity.navigate(R.id.push_home_fragment)

            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.successState.observe(viewLifecycleOwner) { state ->
            when (state) {

                is SucccessPageState.ShowPopUp -> {
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }

                else -> {
                }

            }
        }
    }

    private fun setupAnim() {
        binding.lottieAnimationView.setAnimation(R.raw.flower)
        binding.lottieAnimationView.speed = 0.8f
        binding.lottieAnimationView.playAnimation()
    }

    private fun setupAnim2() {
        binding.lottieAnimationView2.setAnimation(R.raw.surprise_box)
        binding.lottieAnimationView2.speed = 0.8f
        binding.lottieAnimationView2.playAnimation()
    }
}

