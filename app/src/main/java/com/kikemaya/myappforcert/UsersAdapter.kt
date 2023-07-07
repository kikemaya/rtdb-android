package com.kikemaya.myappforcert

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kikemaya.myappforcert.databinding.UsersItemBinding

class UsersAdapter(var context: Context, var userList: ArrayList<Users>) :
    RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {
    inner class UsersViewHolder(val adapterBinding: UsersItemBinding) :
        RecyclerView.ViewHolder(adapterBinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = UsersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding)
    }

    //TRANSFER DE DATA IN THE ARRAY TO THE COMPONENTS
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.adapterBinding.textViewName.text = "Name: ${userList[position].userName}"
        holder.adapterBinding.textViewPhone.text = "Phone: ${userList[position].userPhone}"
        holder.adapterBinding.textViewAge.text = "Age: ${userList[position].userAge.toString()}"
        holder.adapterBinding.textViewEmail.text = "Email: ${userList[position].userEmail}"

        holder.adapterBinding.linearLayout.setOnClickListener {
            val intent = Intent(context, UpdateUserActivity::class.java)
            intent.putExtra("id", userList[position].userId)
            intent.putExtra("name", userList[position].userName)
            intent.putExtra("phone", userList[position].userPhone)
            intent.putExtra("age", userList[position].userAge)
            intent.putExtra("email", userList[position].userEmail)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun getUserId(position: Int) : String {
        return userList[position].userId
    }
}