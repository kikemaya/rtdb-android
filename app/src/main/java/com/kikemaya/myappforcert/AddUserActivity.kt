package com.kikemaya.myappforcert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kikemaya.myappforcert.databinding.ActivityAddUserBinding

class AddUserActivity : AppCompatActivity() {

    lateinit var addUserBinding: ActivityAddUserBinding

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addUserBinding = ActivityAddUserBinding.inflate(layoutInflater)
        val view = addUserBinding.root

        setContentView(view)

        supportActionBar?.title = "Add contact"

        addUserBinding.buttonAddUser.setOnClickListener {
            addUserToDatabase()
        }

    }

    fun addUserToDatabase() {
        val name: String = addUserBinding.editTextName.text.toString()
        val age: Int = addUserBinding.editTextAge.text.toString().toInt()
        val email: String = addUserBinding.editTextEmail.text.toString()
        val phone: String = addUserBinding.editTextPhone.text.toString()

        val id: String = myReference.push().key.toString()

        val user = Users(id, name, phone, age, email)

        myReference.child(id).setValue(user).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "The new user has been added to the database",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            } else {
                Log.i("USER", "$user")

                Toast.makeText(
                    applicationContext,
                    task.exception.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}