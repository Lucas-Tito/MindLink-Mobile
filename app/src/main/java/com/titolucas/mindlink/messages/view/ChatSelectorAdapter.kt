package com.titolucas.mindlink.messages.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.titolucas.mindlink.R
import com.titolucas.mindlink.messages.data.Chat
import com.titolucas.mindlink.messages.data.ConversationsRequest

class ChatSelectorAdapter(
    private val chatList: List<Chat>,
    private val conversationsList: List<ConversationsRequest>
) : RecyclerView.Adapter<ChatSelectorAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeChat: TextView = itemView.findViewById(R.id.psychologist_name)
        val lastMessageChat: TextView = itemView.findViewById(R.id.psychologist_specialty)
        val hora: TextView = itemView.findViewById(R.id.psychologist_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_selector, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.nomeChat.text = chat.nomeChat
        holder.lastMessageChat.text = chat.lastMessageChat
        holder.hora.text = chat.hora

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra("contactId", conversationsList[position].contactId)
                putExtra("contactName", conversationsList[position].contactName)
                putParcelableArrayListExtra("messages", ArrayList(conversationsList[position].messages))
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = chatList.size
}