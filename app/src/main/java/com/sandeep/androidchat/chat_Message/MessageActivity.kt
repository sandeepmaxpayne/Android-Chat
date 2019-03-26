package com.sandeep.androidchat.chat_Message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sandeep.androidchat.R
import com.sandeep.androidchat.model.ChatMessage
import com.sandeep.androidchat.model.User
import com.sandeep.androidchat.sign_In_Activity.RegisterActivity
import com.sandeep.androidchat.view.LatestMessageRow
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessage"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        recylerView_latestMessage.adapter = adapter
        recylerView_latestMessage.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        //set item click on listener adapter
        adapter.setOnItemClickListener { item, _ ->
            Log.d(TAG, "message 123")
            val intent = Intent(this, ChatLogActivity::class.java)
            //missing chat partner user
            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        //setUpDummyRows()
        listenForLatestMessage()
        fetchCurrentUser()
        verifyUserLoggedIn()
        mobileAd()
    }
    private fun mobileAd(){
        MobileAds.initialize(this, getString(R.string.Admob_appID))
        val adRequests = AdRequest.Builder().build()
        adView.loadAd(adRequests)
        adView2.loadAd(adRequests)
        adView3.loadAd(adRequests)
//        val adView = AdView(this)
//        adView.adSize = AdSize.SMART_BANNER
//        adView.adUnitId = R.string.Admob_bannerAd.toString()
        adView.visibility = View.GONE
        adView2.visibility = View.GONE
        adView3.visibility = View.GONE
        adView.adListener = object: AdListener(){
            override fun onAdOpened() {
                adView.visibility = View.VISIBLE
                adView.loadAd(adRequests)
                super.onAdOpened()
            }
            override fun onAdLoaded() {
                adView.visibility = View.VISIBLE
                Log.d("Ad", "adloaded")
                super.onAdLoaded()
            }
        }
        adView2.adListener = object: AdListener(){
            override fun onAdLoaded() {
                adView2.visibility = View.VISIBLE
                super.onAdLoaded()
            }
            override fun onAdClicked() {
                adView2.visibility = View.GONE
                super.onAdClicked()
            }
            override fun onAdOpened() {
                adView2.visibility = View.GONE
                super.onAdOpened()
            }
        }
        adView3.adListener = object : AdListener(){
            override fun onAdLoaded() {
                adView3.visibility = View.VISIBLE
                super.onAdLoaded()
            }

            override fun onAdOpened() {
                adView3.visibility = View.GONE
                super.onAdOpened()
            }
        }
        adView4.adListener = object : AdListener(){
            override fun onAdLoaded() {
                adView4.visibility = View.VISIBLE
                super.onAdLoaded()
            }

            override fun onAdClicked() {
                adView4.visibility = View.GONE
                super.onAdClicked()
            }
            override fun onAdOpened() {
                adView4.visibility = View.GONE
                super.onAdOpened()
            }
        }
        adView5.adListener = object : AdListener(){
            override fun onAdLoaded() {
                adView5.visibility = View.VISIBLE
                super.onAdLoaded()
            }
            override fun onAdClicked() {
                adView5.visibility = View.GONE
                super.onAdClicked()
            }
            override fun onAdOpened() {
                adView5.visibility = View.GONE
                super.onAdOpened()
            }
        }
        adView6.adListener = object : AdListener(){
            override fun onAdLoaded() {
                adView6.visibility = View.VISIBLE
                super.onAdLoaded()
            }
            override fun onAdClicked() {
                adView6.visibility = View.GONE
                super.onAdClicked()
            }
            override fun onAdOpened() {
                adView6.visibility = View.GONE
                super.onAdOpened()
            }
        }

    }

    val latestMessageMap = HashMap<String, ChatMessage>()
    private fun refreshRecycleViewMessage(){
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }
    private fun listenForLatestMessage(){
        val fromID = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromID")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)?: return

                latestMessageMap[p0.key!!] = chatMessage
                refreshRecycleViewMessage()
                //adapter.add(LatestMessageRow(chatMessage))
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)?: return
                adapter.add(LatestMessageRow(chatMessage))
                latestMessageMap[p0.key!!] = chatMessage
                refreshRecycleViewMessage()

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }
    val adapter = GroupAdapter<ViewHolder>()
    /*private fun setUpDummyRows(){
        adapter.add(LatestMessageRow())
        adapter.add(LatestMessageRow())
        adapter.add(LatestMessageRow())
    }*/
    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("Latest Message", "Current User: ${currentUser?.userName}")
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun verifyUserLoggedIn(){

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){

            R.id.newMessage -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.signout ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.delete_account -> {
                val user = FirebaseAuth.getInstance().currentUser
                user?.delete()
                    ?.addOnCompleteListener {
                        if (it.isSuccessful){
                            Log.d("delete", "Account Deleted")
                            Toast.makeText(this, "Current User Account Deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun deleteUser(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.delete()
            ?.addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("Delete", "User account deleted")
                }
            }
    }
}
