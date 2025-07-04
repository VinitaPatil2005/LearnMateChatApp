package com.example.learnmatechatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException
import com.cometchat.chat.models.User
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoToSignup = findViewById<TextView>(R.id.btnGoToSignup)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pwd = etPassword.text.toString().trim()
            loginUser(email, pwd)
        }

        btnGoToSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        logincheck()
    }

    private fun loginUser(email: String, pwd: String) {
        auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser!!.uid
                loginToCometChat(uid)
            } else {
                Toast.makeText(this, "Firebase login failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logincheck() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            loginToCometChat(uid)
        }
    }

    private fun loginToCometChat(uid: String) {
        CometChatUIKit.login(uid, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(p0: User?) {
                startActivity(Intent(this@LoginActivity, ConversationActivity::class.java))
                finish()
            }

            override fun onError(e: CometChatException?) {
                Toast.makeText(this@LoginActivity, "CometChat login failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
