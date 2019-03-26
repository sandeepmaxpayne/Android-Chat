package com.sandeep.androidchat.view

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sandeep.androidchat.R
import com.sandeep.androidchat.model.ChatMessage
import com.sandeep.androidchat.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>(){
    var chatPartnerUser: User? = null
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_textView_latestMessage.text = chatMessage.text


        val chatPartnerID: String = if (chatMessage.fromID == FirebaseAuth.getInstance().uid){
            chatMessage.toID
        }else{
            chatMessage.fromID
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerID")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(User::class.java)
                viewHolder.itemView.userName_textView_latestMessage.text = chatPartnerUser?.userName

                val targetImageView = viewHolder.itemView.imageView_latestMessage
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}