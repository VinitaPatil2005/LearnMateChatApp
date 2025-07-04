package com.example.learnmatechatapp

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.cometchat.chat.constants.CometChatConstants
import com.cometchat.chat.models.User
import com.cometchat.chatuikit.users.CometChatUsers
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserActivity : AppCompatActivity() {

    private lateinit var usersView: CometChatUsers
    private lateinit var fabCreateGroup: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        usersView = CometChatUsers(this).apply {
            setBackIconVisibility(View.VISIBLE)
            setSearchBoxVisibility(View.VISIBLE)

            setOnItemClick { _, _, user ->
                if (user is User) {
                    val intent = Intent(this@UserActivity, MessageActivity::class.java)
                    intent.putExtra("uid", user.uid)
                    intent.putExtra("conversationType", CometChatConstants.RECEIVER_TYPE_USER)
                    startActivity(intent)
                }
            }
        }

        val fabCreateGroup = FloatingActionButton(this).apply {
            setImageResource(R.drawable.ic_group)
            visibility = View.VISIBLE
            backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF6200EE.toInt())
            setColorFilter(0xFFFFFFFF.toInt()) // White icon tint
            contentDescription = "Create new group"
            size = FloatingActionButton.SIZE_NORMAL // 56dp default size
            setOnClickListener {
                startActivity(Intent(this@UserActivity, GroupCreateActivity::class.java))
            }
        }

        rootLayout.addView(usersView)
        rootLayout.addView(fabCreateGroup, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM or Gravity.END
            marginEnd = 46
            bottomMargin = 46
        })

        setContentView(rootLayout)
    }
}
