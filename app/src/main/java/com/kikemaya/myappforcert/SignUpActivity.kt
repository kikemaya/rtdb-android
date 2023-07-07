package com.kikemaya.myappforcert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kikemaya.myappforcert.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpBinding: ActivitySignUpBinding

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signUpBinding.root
        setContentView(view)

        signUpBinding.buttonSignUpUser.setOnClickListener {
            val userEmail = signUpBinding.editTextEmailSignUp.text.toString()
            val userPassword = signUpBinding.editTextPasswordSignUp.text.toString()

            signUpWithFirebase(userEmail, userPassword)
        }
    }

    fun signUpWithFirebase(userEmail: String, userPassword: String) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, task.exception?.toString(), Toast.LENGTH_SHORT).show()

            }
        }
    }
}