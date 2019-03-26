package com.sandeep.androidchat.chat_Message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sandeep.androidchat.R
import com.sandeep.androidchat.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"

//        val adapter = GroupAdapter<ViewHolder>()
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        recyleView_newMessage.adapter = adapter

        fetchUser()

    }
    companion object {
        val USER_KEY = "User key"
    }

    private fun fetchUser() {
        val ref=FirebaseDatabase.getInstance().getReference("/users")
        // Log.d("Message", "the msg : $ref")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user=it.getValue(User::class.java)
                    Log.d("NewMessage", "User : $user")
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }

                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent=Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY,userItem.user.userName)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                    finish()
                }

                recyleView_newMessage.adapter=adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}


class UserItem(val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //will be called for each layer list
        viewHolder.itemView.textView_Username_newMessage.text=user.userName
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_NewMessage)
    }

    override fun getLayout(): Int {
        return R.layout.user_new_message
    }
}

