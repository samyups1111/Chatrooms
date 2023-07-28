package com.sample.mainapplication.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.mainapplication.R
import com.sample.mainapplication.model.Message

class MessageAdapter: RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages = emptyList<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentItem = messages[position]
        holder.bind(currentItem)
    }

    fun updateList(newList: List<Message>) {
        messages = newList
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val messageTextView: TextView = itemView.findViewById(R.id.message)
        private val usernameTexView: TextView = itemView.findViewById(R.id.username)

        fun bind(message: Message) {
            messageTextView.text = message.text
            usernameTexView.text = message.userName
        }
    }
}