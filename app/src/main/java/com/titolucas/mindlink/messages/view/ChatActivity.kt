package com.titolucas.mindlink.messages.view

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.titolucas.mindlink.R
import com.titolucas.mindlink.messages.data.MessageRequest
import com.titolucas.mindlink.network.RetrofitInstance
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: ImageButton
    private lateinit var messagesAdapter: ChatMessagesAdapter
    private var messages: ArrayList<MessageRequest> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        val contactId = intent.getStringExtra("contactId")

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messagesAdapter = ChatMessagesAdapter(messages)
        recyclerView.adapter = messagesAdapter

        // Configurar campo de entrada e botão de envio
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSendMessage = findViewById(R.id.sendMessage)

        buttonSendMessage.setOnClickListener {
            val messageText = editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                sendMessage(contactId, messageText)
            }
        }

        // Recuperar mensagens do servidor
        fetchMessages(contactId)
    }

    private fun fetchMessages(contactId: String?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        lifecycleScope.launch {
            try {
                val conversations = RetrofitInstance.apiService.getConversationsByUserId(userId, contactId ?: "")
                val conversation = conversations.firstOrNull()
                if (conversation != null) {
                    messages.clear()
                    messages.addAll(conversation.messages.sortedBy { it.createdAt })
                    messagesAdapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(messages.size - 1)
                    findViewById<TextView>(R.id.nomeChat).text = conversation.contactName
                }
            } catch (e: Exception) {
                // Tratar erro de recuperação de mensagens
            }
        }
    }

    private fun sendMessage(contactId: String?, messageText: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().reference
        val messageId = database.child("messages").push().key ?: return // Gera um ID único

        val messageRequest = MessageRequest(
            senderId = senderId,
            receiverId = contactId ?: "",
            text = messageText,
            participants = listOf(senderId, contactId ?: ""),
            messageId = messageId,
            createdAt = System.currentTimeMillis().toString() // Pode ser formatado conforme necessário
        )

        lifecycleScope.launch {
            val response = RetrofitInstance.apiService.createMessage(messageRequest)
            if (response.isSuccessful) {
                messages.add(messageRequest)
                messagesAdapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
                editTextMessage.text.clear()
            } else {
                // Tratar erro de envio de mensagem
            }
        }
    }
}