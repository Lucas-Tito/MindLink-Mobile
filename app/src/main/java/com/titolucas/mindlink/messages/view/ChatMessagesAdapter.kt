package com.titolucas.mindlink.messages.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.R
import com.titolucas.mindlink.messages.data.MessageRequest
import com.titolucas.mindlink.messages.utils.DateUtils

class ChatMessagesAdapter(private val messages: List<MessageRequest>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            holder.messageText.text = message.text
            holder.messageCreatedAt.text = DateUtils.formatDate(message.createdAt)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.messageText.text = message.text
            holder.messageCreatedAt.text = DateUtils.formatDate(message.createdAt)
        }
    }

    override fun getItemCount() = messages.size

    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.message_received)
        val messageCreatedAt: TextView = itemView.findViewById(R.id.messageCreatedAt)
    }

    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.message_received)
        val messageCreatedAt: TextView = itemView.findViewById(R.id.messageCreatedAt)
    }
}