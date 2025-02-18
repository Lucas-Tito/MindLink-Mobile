    package com.titolucas.mindlink.messages.view
    
    import android.content.Intent
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.titolucas.mindlink.R
    import com.bumptech.glide.Glide
    import com.titolucas.mindlink.messages.data.LastMessageRequest
    import com.titolucas.mindlink.messages.utils.DateUtils
    
    class ChatSelectorAdapter(
        private val chatList: List<LastMessageRequest>,
    ) : RecyclerView.Adapter<ChatSelectorAdapter.ChatViewHolder>() {
    
        class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nomeChat: TextView = itemView.findViewById(R.id.psychologist_name)
            val lastMessageChat: TextView = itemView.findViewById(R.id.psychologist_specialty)
            val hora: TextView = itemView.findViewById(R.id.psychologist_rating)
            val photo: ImageView = itemView.findViewById(R.id.chat_selector_image)
        }
    
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_selector, parent, false)
            return ChatViewHolder(view)
        }
    
        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            val chat = chatList[position]
            holder.nomeChat.text = chat.contactName
            holder.lastMessageChat.text = chat.lastMessage
            holder.hora.text = DateUtils.formatDate(chat.createdAt.toLongOrNull() ?: 0L)
    
            // Carregar a imagem usando Glide
            Glide.with(holder.itemView.context)
                .load(chat.photoURL)
                .placeholder(R.drawable.ic_profile) // Imagem de placeholder
                .error(R.drawable.ic_profile) // Imagem de erro
                .into(holder.photo)

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, ChatActivity::class.java).apply {
                    putExtra("contactId", chatList[position].contactUserId)
                    putExtra("photoURL", chatList[position].photoURL)
                    putExtra("contactName", chatList[position].contactName) // Adiciona o nome do contato
                }
                context.startActivity(intent)
            }
        }
    
        override fun getItemCount() = chatList.size
    }