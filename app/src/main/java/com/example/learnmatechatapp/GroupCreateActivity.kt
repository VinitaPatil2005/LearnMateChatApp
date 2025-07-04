package com.example.learnmatechatapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.chat.constants.CometChatConstants
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.core.UsersRequest
import com.cometchat.chat.exceptions.CometChatException
import com.cometchat.chat.models.Group
import com.cometchat.chat.models.GroupMember
import com.cometchat.chat.models.User
import com.google.android.material.button.MaterialButton

class GroupCreateActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCreate: MaterialButton
    private lateinit var adapter: UserSelectionAdapter
    private lateinit var users: MutableList<User>
    private val selectedUsers = mutableSetOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation)

        recyclerView = findViewById(R.id.recyclerUsers)
        btnCreate = findViewById(R.id.btnCreateGroup)

        recyclerView.layoutManager = LinearLayoutManager(this)

        users = mutableListOf()
        adapter = UserSelectionAdapter(users) { user, isSelected ->
            if (isSelected) {
                selectedUsers.add(user)
            } else {
                selectedUsers.remove(user)
            }
            updateCreateButton()
        }
        recyclerView.adapter = adapter

        fetchUsersFromCometChat()

        btnCreate.setOnClickListener {
            showGroupNameDialog()
        }

        updateCreateButton()
    }

    private fun fetchUsersFromCometChat() {
        val usersRequest = UsersRequest.UsersRequestBuilder()
            .setLimit(50)
            .build()

        usersRequest.fetchNext(object : CometChat.CallbackListener<List<User>>() {
            override fun onSuccess(userList: List<User>) {
                runOnUiThread {
                    users.clear()
                    // Filter out the current logged-in user
                    val currentUser = CometChat.getLoggedInUser()
                    users.addAll(userList.filter { it.uid != currentUser?.uid })
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onError(e: CometChatException) {
                runOnUiThread {
                    Toast.makeText(this@GroupCreateActivity, "Failed to fetch users: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateCreateButton() {
        btnCreate.text = if (selectedUsers.isEmpty()) {
            "Create Group"
        } else {
            "Create Group (${selectedUsers.size} selected)"
        }
    }

    private fun showGroupNameDialog() {
        if (selectedUsers.isEmpty()) {
            Toast.makeText(this, "Please select at least one user", Toast.LENGTH_SHORT).show()
            return
        }

        val input = EditText(this)
        input.hint = "Enter Group Name"

        AlertDialog.Builder(this)
            .setTitle("Create Group")
            .setMessage("Creating group with ${selectedUsers.size} members")
            .setView(input)
            .setPositiveButton("Create") { _, _ ->
                val groupName = input.text.toString().trim()
                if (groupName.isNotEmpty()) {
                    createGroup(groupName)
                } else {
                    Toast.makeText(this, "Group name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun createGroup(groupName: String) {
        val guid = "group_${System.currentTimeMillis()}"
        val group = Group(guid, groupName, CometChatConstants.GROUP_TYPE_PUBLIC, null)

        val members = selectedUsers.map { user ->
            GroupMember(user.uid, CometChatConstants.SCOPE_PARTICIPANT)
        }

        // Show loading state
        btnCreate.isEnabled = false
        btnCreate.text = "Creating..."

        CometChat.createGroupWithMembers(group, members, mutableListOf(),
            object : CometChat.CreateGroupWithMembersListener() {
                override fun onSuccess(createdGroup: Group, resultMap: HashMap<String, String>) {
                    runOnUiThread {
                        Toast.makeText(this@GroupCreateActivity, "Group created successfully!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@GroupCreateActivity, MessageActivity::class.java)
                        intent.putExtra("guid", createdGroup.guid)
                        intent.putExtra("conversationType", CometChatConstants.RECEIVER_TYPE_GROUP)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onError(e: CometChatException) {
                    runOnUiThread {
                        btnCreate.isEnabled = true
                        updateCreateButton()
                        Toast.makeText(this@GroupCreateActivity, "Group creation failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}