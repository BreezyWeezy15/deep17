package com.deep.seek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deep.seek.models.Message

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val messages = mutableListOf<Message>()

    fun setMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages.filter { it.content.isNotBlank() }) // ✅ Remove empty messages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userMessage: TextView = itemView.findViewById(R.id.userMessage)
        private val assistantMessage: TextView = itemView.findViewById(R.id.assistantMessage)

        fun bind(message: Message) {
            if (message.role == "user") {
                userMessage.text = message.content
                userMessage.visibility = if (message.content.isNotBlank()) View.VISIBLE else View.GONE
                assistantMessage.visibility = View.GONE
            } else {
                assistantMessage.text = message.content
                assistantMessage.visibility = if (message.content.isNotBlank()) View.VISIBLE else View.GONE
                userMessage.visibility = View.GONE
            }
        }
    }
}

