package com.sample.mainapplication.ui.chatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.mainapplication.R

class ChatroomAdapter: RecyclerView.Adapter<ChatroomAdapter.ChatroomViewHolder>() {

    private var chatroomList: List<String> = emptyList()
    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chatroom_item, parent, false)
        return ChatroomViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return chatroomList.size
    }

    override fun onBindViewHolder(holder: ChatroomViewHolder, position: Int) {
        holder.onBind(chatroomList[position])
    }

    fun updateList(chatrooms: List<String>) {
        chatroomList = chatrooms
        notifyDataSetChanged()
    }

    inner class ChatroomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val chatroomNameTextView: TextView = itemView.findViewById(R.id.chatroom_name_textview)

        fun onBind(chatroomName: String) {
            chatroomNameTextView.text = chatroomName
            chatroomNameTextView.setOnClickListener {
                onItemClick?.invoke(chatroomName)
            }
        }
    }
}