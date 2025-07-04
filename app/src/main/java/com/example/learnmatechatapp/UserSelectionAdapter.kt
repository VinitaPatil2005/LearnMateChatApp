package com.example.learnmatechatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.chat.models.User

class UserSelectionAdapter(
    private val users: List<User>,
    private val onUserSelected: (User, Boolean) -> Unit
) : RecyclerView.Adapter<UserSelectionAdapter.UserViewHolder>() {

    private val selectedUids = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_selection, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtUserName: TextView = itemView.findViewById(R.id.txtUserName)
        private val txtUserStatus: TextView = itemView.findViewById(R.id.txtUserStatus)

        fun bind(user: User) {
            txtUserName.text = user.name ?: user.uid
            txtUserStatus.text = if (user.status == "online") "Online" else "Offline"

            val isSelected = selectedUids.contains(user.uid)

            // Update UI based on selection state
            if (isSelected) {
                itemView.setBackgroundColor(itemView.context.getColor(R.color.selected_user_background))
                txtUserName.setTextColor(itemView.context.getColor(R.color.selected_user_text))
            } else {
                itemView.setBackgroundColor(itemView.context.getColor(android.R.color.transparent))
                txtUserName.setTextColor(itemView.context.getColor(android.R.color.black))
            }

            itemView.setOnClickListener {
                val wasSelected = selectedUids.contains(user.uid)
                if (wasSelected) {
                    selectedUids.remove(user.uid)
                } else {
                    selectedUids.add(user.uid)
                }
                onUserSelected(user, !wasSelected)
                notifyItemChanged(adapterPosition)
            }
        }
    }

    fun getSelectedUsers(): List<User> {
        return users.filter { selectedUids.contains(it.uid) }
    }
}