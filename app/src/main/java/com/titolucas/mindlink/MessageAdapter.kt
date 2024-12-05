package com.titolucas.mindlink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.titolucas.mindlink.databinding.ItemMessageBinding

import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textMessage.text = message.text
            binding.textSender.text = "De: ${message.senderId}"
            binding.textReceiver.text = "Para: ${message.receiverId}"

            // Formatar a data
            message.createdAt?.let {
                val createdAtLong = it.seconds
                val date = Date(createdAtLong * 1000)  // Converte segundos para milissegundos
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                binding.textDate.text = dateFormat.format(date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size
}
