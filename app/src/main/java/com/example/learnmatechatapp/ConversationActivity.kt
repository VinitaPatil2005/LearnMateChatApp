package com.example.learnmatechatapp

//import android.content.Intent
//import com.cometchat.chat.constants.CometChatConstants
//import com.cometchat.chat.models.Conversation
//import com.cometchat.chat.models.Group
//import com.cometchat.chat.models.User
//import com.cometchat.chatuikit.conversations.CometChatConversations
//
//class ConversationActivity : AppCompatActivity() {
//
//    private lateinit var conversationsView: CometChatConversations
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_conversation)
//
//        conversationsView = findViewById(R.id.conversation_view)
//
//        conversationsView.setOnItemClick { _, _, conversation ->
//            startMessageActivity(conversation)
//        }
//    }
//
//    private fun startMessageActivity(conversation: Conversation) {
//        val intent = Intent(this, MessageActivity::class.java)
//        when (conversation.conversationType) {
//            CometChatConstants.CONVERSATION_TYPE_GROUP -> {
//                val group = conversation.conversationWith as Group
//                intent.putExtra("guid", group.guid)
//            }
//            CometChatConstants.CONVERSATION_TYPE_USER -> {
//                val user = conversation.conversationWith as User
//                intent.putExtra("uid", user.uid)
//            }
//        }
//        startActivity(intent)
//    }
//}


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cometchat.chat.constants.CometChatConstants
import com.cometchat.chat.models.Conversation
import com.cometchat.chat.models.Group
import com.cometchat.chat.models.User
import com.cometchat.chatuikit.conversations.CometChatConversations
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ConversationActivity : AppCompatActivity() {

    private lateinit var fabNewChat: FloatingActionButton
    private lateinit var conversationsView: CometChatConversations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation) // ✅ Load XML

        conversationsView = findViewById(R.id.conversation_view)
        fabNewChat = findViewById(R.id.fab_new_chat)



        fabNewChat.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java)) // ➕ Open user list
        }

        conversationsView.setOnItemClick { _, _, conversation ->
            startMessageActivity(conversation)
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_conversations -> true // Already here
                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }


    }

    private fun startMessageActivity(conversation: Conversation) {
        val intent = Intent(this, MessageActivity::class.java)
        when (conversation.conversationType) {
            CometChatConstants.CONVERSATION_TYPE_GROUP -> {
                val group = conversation.conversationWith as Group
                intent.putExtra("guid", group.guid)
                intent.putExtra("conversationType", CometChatConstants.RECEIVER_TYPE_GROUP) // ✅ Add this
            }
            CometChatConstants.CONVERSATION_TYPE_USER -> {
                val user = conversation.conversationWith as User
                intent.putExtra("uid", user.uid)
                intent.putExtra("conversationType", CometChatConstants.RECEIVER_TYPE_USER) // ✅ Add this
            }
        }
        startActivity(intent)
    }
}








