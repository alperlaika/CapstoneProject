package com.laikaalper.florist_app.ui.signIn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.databinding.FragmentSignInBinding
import com.laikaalper.florist_app.ui.MainActivity

class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private val binding by viewBinding(FragmentSignInBinding::bind)

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        auth.currentUser?.let {
            MainActivity.navigate(R.id.push_home_fragment)
        }

        with(binding) {
            btnSignIn.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (checkFields(email, password)) {
                    signIn(email, password)
                } else {
                    Snackbar.make(it, "Lütfen boşlukları doldurun", 1000).show()
                }
            }
            signInToSignUp.setOnClickListener {
                MainActivity.navigate(R.id.push_sign_up_fragment)
            }

        }

    }

    private fun checkFields(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> false
            password.isEmpty() -> false
            else -> true
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            MainActivity.navigate(R.id.push_home_fragment)
        }.addOnFailureListener {
            Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()
        }
    }


}