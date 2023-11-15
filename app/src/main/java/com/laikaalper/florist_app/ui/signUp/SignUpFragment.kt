package com.laikaalper.florist_app.ui.signUp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.data.model.response.User
import com.laikaalper.florist_app.databinding.FragmentSignUpBinding
import com.laikaalper.florist_app.ui.MainActivity


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val userList = mutableListOf<User>()

    private val usersText = StringBuilder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        listenUsers()

        with(binding) {
            btnSignUp.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (checkFields(email, password)) {
                    addUser(email, password)
                    createUser(email, password)
                } else {
                    Snackbar.make(it, "Lütfen boşlukları doldurun", 1000).show()
                }
            }
            signUpToSignIn.setOnClickListener {
                MainActivity.navigate(R.id.push_sign_in_fragment)
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

    private fun addUser(email: String, password: String) {
        val user = User(
            docId = null,
            email = email,
            password = password,

            )

        db.collection("users").add(user).addOnSuccessListener {
            Snackbar.make(requireView(), "Başarılı Bir Şekilde Kayıt oldunuz", 1000).show()
            MainActivity.navigate(R.id.push_home_fragment)
        }.addOnFailureListener {
            Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()
        }

    }

    private fun listenUsers() {
        db.collection("users").addSnapshotListener { snapshot, error ->

            userList.clear()
            snapshot?.forEach {
                userList.add(
                    User(
                        docId = it.id,
                        email = it.get("email") as String,
                        password = it.get("password") as String
                    )
                )
            }


        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                }
            }
    }

}