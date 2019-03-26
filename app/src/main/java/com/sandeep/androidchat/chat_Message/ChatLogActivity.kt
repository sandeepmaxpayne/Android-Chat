package com.sandeep.androidchat.chat_Message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.sandeep.androidchat.R
import com.sandeep.androidchat.model.ChatMessage
import com.sandeep.androidchat.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_receiver_row.view.*
import kotlinx.android.synthetic.main.chat_sender_row.view.*

class ChatLogActivity : AppCompatActivity() {
    companion object {
        val Tag = "Chatlog"
    }
    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recycler_view_chatlog.adapter = adapter

       // supportActionBar?.title = "Chat Log"
       // val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.userName

        //startUpDummyData()
        listenForMessage()

        send_chat.setOnClickListener {
            Log.d(Tag, "Attempt to send")
            performSendMessage()
        }
    }
    private fun listenForMessage(){
        val fromID = FirebaseAuth.getInstance().uid
        val toID = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID")
        ref.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d(Tag, chatMessage.text)
                    if(chatMessage.fromID == FirebaseAuth.getInstance().uid) {
                        val currentUser = MessageActivity.currentUser ?: return
                        adapter.add(ChatReceiver(chatMessage.text,currentUser))
                        Log.d(Tag, "right")
                    }else{
                        adapter.add(ChatSender(chatMessage.text, toUser!!))
                        Log.d(Tag, "Left")
                    }
                }
                recycler_view_chatlog.scrollToPosition(adapter.itemCount - 1)
            }
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })

    }

    private fun performSendMessage(){
        //how do we send message to firebase
        val text = msg_chat_log.text.toString()  // edit text from activity_chat_log

        val fromID = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toID = user.uid

        if (fromID == null) return

        //val references = FirebaseDatabase.getInstance().getReference("/messages").push()
        val references = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID").push()
        val toReferences = FirebaseDatabase.getInstance().getReference("/user-messages/$toID/$fromID").push() //msg visible to user
        val chatMessage = ChatMessage(references.key!!, text, toID, fromID, System.currentTimeMillis() / 1000)
        references.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(Tag, "Message Saved: ${references.key}")
                msg_chat_log.text.clear()
                recycler_view_chatlog.scrollToPosition(adapter.itemCount - 1)
            }
            .addOnFailureListener {
                Log.d(Tag, "Unable to save message")
            }
        toReferences.setValue(chatMessage)

        // Fetch the latest data from the database

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromID/$toID")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toID/$fromID")
        latestMessageToRef.setValue(chatMessage)
    }


}
class ChatReceiver(val text: String,val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_From_Row.text = text
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageView_chat_From_Row
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {
        return R.layout.chat_receiver_row
    }
}

class ChatSender(val text: String, val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_Row.text = text
        // to load the images in lace of stars
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageView_Chat_To_Row
        Picasso.get().load(uri).into(targetImageView)


    }

    override fun getLayout(): Int {
        return R.layout.chat_sender_row
    }
}
