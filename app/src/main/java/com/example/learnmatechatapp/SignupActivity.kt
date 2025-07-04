package com.example.learnmatechatapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException
import com.cometchat.chat.models.User
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val appID = "277979d8436be7e0"
    private val region = "in"
    private val authKey = "db82801d68f0d16b5a44fa3eb2e86700a73d474b"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val rbStudent = findViewById<RadioButton>(R.id.rbStudent)
        val rbTeacher = findViewById<RadioButton>(R.id.rbTeacher)
        val btnSignup = findViewById<Button>(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pwd = etPassword.text.toString().trim()
            val role = if (rbStudent.isChecked) "student" else "teacher"
            registerUser(email, pwd, role)
        }
    }

    private fun registerUser(email: String, pwd: String, role: String) {
        auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser!!.uid
                createCometUser(uid, email, role)
            } else {
                Toast.makeText(this, "Firebase registration failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createCometUser(uid: String, email: String, role: String) {
        val user = User().apply {
            this.uid = uid
            this.name = email.substringBefore("@")
            this.metadata = JSONObject().put("role", role)
        }

        CometChat.createUser(user, authKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(p0: User?) {
                loginToCometChat(uid)
            }

            override fun onError(e: CometChatException?) {
                if (e?.code == "ERR_UID_ALREADY_EXISTS") loginToCometChat(uid)
                else Toast.makeText(this@SignupActivity, "CometChat user creation failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loginToCometChat(uid: String) {
        CometChat.login(uid, authKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User?) {
                startActivity(Intent(this@SignupActivity, ConversationActivity::class.java))
                finish()
            }

            override fun onError(e: CometChatException?) {
                Toast.makeText(this@SignupActivity, "CometChat login failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
