package com.kikemaya.myappforcert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kikemaya.myappforcert.databinding.ActivityUpdateUserBinding

class UpdateUserActivity : AppCompatActivity() {

    lateinit var updateUserBinding: ActivityUpdateUserBinding

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateUserBinding = ActivityUpdateUserBinding.inflate(layoutInflater)
        val view = updateUserBinding.root

        setContentView(view)

        supportActionBar?.title = "Update contact"

        getAndSetData()

        updateUserBinding.buttonUpdateUser.setOnClickListener {
            updateData()
        }
    }

    fun getAndSetData() {
        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age", 0).toString()
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")

        updateUserBinding.editTextUpdateName.setText(name)
        updateUserBinding.editTextUpdatePhone.setText(phone)
        updateUserBinding.editTextUpdateAge.setText(age)
        updateUserBinding.editTextUpdateEmail.setText(email)
    }

    fun updateData() {
        val updatedName = updateUserBinding.editTextUpdateName.text.toString()
        val updatedAge = updateUserBinding.editTextUpdateAge.text.toString().toInt()
        val updatedEmail = updateUserBinding.editTextUpdateEmail.text.toString()
        val updatedPhone = updateUserBinding.editTextUpdatePhone.text.toString()

        val userId = intent.getStringExtra("id").toString()

        val userMap = mutableMapOf<String, Any>()
        userMap["userId"] = userId
        userMap["userName"] = updatedName
        userMap["userPhone"] = updatedPhone
        userMap["userAge"] = updatedAge
        userMap["userEmail"] = updatedEmail

        myReference.child(userId).updateChildren(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "The user has been updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}