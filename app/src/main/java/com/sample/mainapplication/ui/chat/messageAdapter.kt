package com.sample.mainapplication.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.mainapplication.R

class MessageAdapter: RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    var messages = emptyList<String>()

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

    fun updateList(newList: List<String>) {
        messages = newList
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val messageTextView: TextView = itemView.findViewById(R.id.message)

        fun bind(message: String) {
            messageTextView.text = message
        }
    }
}