package com.sample.mainapplication.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.mainapplication.R
import com.sample.mainapplication.model.Message
import java.lang.IllegalArgumentException
import java.time.format.DateTimeFormatter
import java.util.Objects

class MessageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messages: List<Any> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return when (messages[position]) {
            is String -> R.layout.date_item
            is Message -> R.layout.message_item
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.message_item -> MessageViewHolder(inflater.inflate(viewType, parent, false))
            R.layout.date_item -> DateViewHolder(inflater.inflate(viewType, parent, false))
            else -> throw IllegalArgumentException("Unsupported layout")
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = messages[position]

        when (holder) {
            is MessageViewHolder -> {
                val message = element as Message
                holder.bind(message)
            }
            is DateViewHolder -> {
                val date = element as String
                holder.bind(date)
            }
        }
    }

    fun updateList(newList: List<Message>) {
        groupDataIntoMap(newList)
        notifyDataSetChanged()
    }

    private fun groupDataIntoMap(messages: List<Message>) {
        val messagesMap: MutableMap<String, MutableList<Message>> = mutableMapOf()

        messages.forEach { message ->
            message.date?.let {
                val mapKey = Message.convertLongToDate(message.date)

                if (messagesMap.containsKey(mapKey)) {
                    messagesMap[mapKey]!!.add(message)
                } else {
                    val newListByDate: MutableList<Message> = mutableListOf(message)
                    messagesMap[mapKey] = newListByDate
                }
            }
        }
        generateListFromMap(messagesMap)
    }

    private fun generateListFromMap(messagesMap: MutableMap<String, MutableList<Message>>) {
        val consolidatedList: MutableList<Any> = ArrayList()

        for (date in messagesMap.keys) {
            consolidatedList.add(date)
            messagesMap[date]?.let { messagesByDate ->
                messagesByDate.forEach { message ->
                    consolidatedList.add(message)
                }
            }
        }
        messages = consolidatedList
    }

    inner class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val messageTextView: TextView = itemView.findViewById(R.id.message)
        private val usernameTexView: TextView = itemView.findViewById(R.id.username)
        private val dateTextView: TextView = itemView.findViewById(R.id.date)

        fun bind(message: Message) {
            messageTextView.text = message.text
            usernameTexView.text = message.userName
            dateTextView.text = message.date?.let { Message.convertLongToTime(it) }
        }
    }

    inner class DateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.message_date_textview)

        fun bind(date: String) {
            dateTextView.text = date
        }
    }
}