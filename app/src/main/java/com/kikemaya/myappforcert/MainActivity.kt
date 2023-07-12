package com.kikemaya.myappforcert

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kikemaya.myappforcert.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")

    val userList = ArrayList<Users>()
    lateinit var usersAdapter: UsersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = usersAdapter.getUserId(viewHolder.adapterPosition)

                myReference.child(auth.currentUser!!.uid).child(id).removeValue()

                Toast.makeText(applicationContext, "The user was deleted", Toast.LENGTH_SHORT)
                    .show()
            }
        }).attachToRecyclerView(mainBinding.recyclerView)

        retrieveDataFromDatabase()
    }


    fun retrieveDataFromDatabase() {
        myReference.child(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()

                for (eachUser in snapshot.children) {
                    if (eachUser.child("contactOwner").value == auth.currentUser?.uid) {
                        val user = eachUser.getValue(Users::class.java)

                        if (user != null) {
                            userList.add(user)
                        }

                        usersAdapter = UsersAdapter(this@MainActivity, userList)
                        mainBinding.recyclerView.layoutManager =
                            LinearLayoutManager(this@MainActivity)
                        mainBinding.recyclerView.adapter = usersAdapter

                        Log.e("USER", "${eachUser.child("contactOwner").value}")
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ON_CANCELLED", "onCancelled: ${error.toException()}")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_all, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll) {
            showDialogMessage()
        } else if (item.itemId == R.id.signOut) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDialogMessage() {
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete all users")
        dialogMessage.setMessage("If click yes, all users will be deleted, " + "If you want to delete a specific user, you can swipe the item you want to delete right or left")

        dialogMessage.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })

        dialogMessage.setPositiveButton(
            " Yes",
            DialogInterface.OnClickListener { dialogInterface, i ->
                myReference.child(auth.currentUser!!.uid).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        usersAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            applicationContext,
                            "All users were deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })

        dialogMessage.create().show()
    }
}