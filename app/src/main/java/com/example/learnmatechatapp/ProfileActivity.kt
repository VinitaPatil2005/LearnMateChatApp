package com.example.learnmatechatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cometchat.chat.core.CometChat
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.log

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profileImage: ImageView = findViewById(R.id.profileImage)
        val profileName: TextView = findViewById(R.id.profileName)
        val profileUID: TextView = findViewById(R.id.profilerole)
        val btnLogout: Button = findViewById(R.id.btnLogout)

        val loggedInUser = CometChat.getLoggedInUser()
        profileName.text = loggedInUser?.name ?: "Unknown"
        profileUID.text = loggedInUser?.role ?: "Unknown role"

        btnLogout.setOnClickListener {
            CometChat.logout(object : CometChat.CallbackListener<String>() {
                override fun onSuccess(p0: String?) {

                    // Firebase logout
                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut()

                    // Redirect to login
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                override fun onError(e: com.cometchat.chat.exceptions.CometChatException?) {
                    e?.printStackTrace()
                }
            })
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationpro)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_profile -> true // Already here
                R.id.menu_conversations -> {
                    startActivity(Intent(this, ConversationActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
