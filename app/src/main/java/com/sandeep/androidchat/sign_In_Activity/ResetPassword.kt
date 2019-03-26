package com.sandeep.androidchat.sign_In_Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sandeep.androidchat.R
import kotlinx.android.synthetic.main.activity_reset__password.*

class ResetPassword : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset__password)

        sendReset.setOnClickListener{
            passwordResetEmail()
        }

    }
    private fun passwordResetEmail(){
        val auth = FirebaseAuth.getInstance()
        val emailAddress = resetEmail.text.toString()
        if (emailAddress.isEmpty()){
            Toast.makeText(this, "Please enter email to Reset Password", Toast.LENGTH_SHORT).show()
            return
        }
        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("Log", "Password reset")
                    Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show()
                    updateUI()
                }else{
                    Log.d("Log", "Error sending password:${it.exception!!.message}")
                    Toast.makeText(this, "No user found with this email", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun updateUI(){
        val intent = Intent(this@ResetPassword, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }

}
