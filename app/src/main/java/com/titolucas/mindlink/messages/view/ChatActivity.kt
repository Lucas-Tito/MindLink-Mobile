package com.titolucas.mindlink.messages.view

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.titolucas.mindlink.R
import com.titolucas.mindlink.messages.data.MessageRequest
import com.titolucas.mindlink.network.RetrofitInstance
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: ImageButton
    private lateinit var messagesAdapter: ChatMessagesAdapter
    private var messages: MutableList<MessageRequest> = mutableListOf()
    private lateinit var messagesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        val photoURL = intent.getStringExtra("photoURL")
        val contactImage = findViewById<ImageView>(R.id.contact_image)

        Glide.with(this)
            .load(photoURL)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(contactImage)

        val returnButton = findViewById<ImageButton>(R.id.chat_return_button)
        returnButton.setOnClickListener {
            finish()
        }

        val contactId = intent.getStringExtra("contactId")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        messagesAdapter = ChatMessagesAdapter(messages)
        recyclerView.adapter = messagesAdapter

        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSendMessage = findViewById(R.id.sendMessage)

        buttonSendMessage.setOnClickListener {
            val messageText = editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                sendMessage(contactId, messageText)
            }
        }

        fetchMessages(contactId)
        setupRealtimeUpdates(contactId)
    }

    private fun fetchMessages(contactId: String?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        lifecycleScope.launch {
            try {
                val conversations = RetrofitInstance.apiService.getConversationsByUserId(userId, contactId ?: "")
                val conversation = conversations.firstOrNull()
                if (conversation != null) {
                    messages.clear()
                    messages.sortBy { it.createdAt }
                    messagesAdapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(messages.size - 1)
                    findViewById<TextView>(R.id.nomeChat).text = conversation.contactName
                }
            } catch (e: Exception) {
                Log.e("ChatActivity", "Erro ao recuperar mensagens: ${e.message}")
            }
        }
    }

    private fun sendMessage(contactId: String?, messageText: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().reference
        val messageId = database.child("messages").push().key ?: return

        val messageRequest = MessageRequest(
            senderId = senderId,
            receiverId = contactId ?: "",
            text = messageText,
            participants = listOf(senderId, contactId ?: ""),
            messageId = messageId,
            createdAt = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            val response = RetrofitInstance.apiService.createMessage(messageRequest)
            if (response.isSuccessful) {
                database.child("messages").child(messageId).setValue(messageRequest)
                editTextMessage.text.clear()
            } else {
                Log.e("ChatActivity", "Erro ao enviar mensagem: ${response.errorBody()}")
            }
        }
    }

    private fun setupRealtimeUpdates(contactId: String?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        messagesRef = FirebaseDatabase.getInstance().getReference("messages")

        Log.d("ChatActivity", "Configuração de atualizações em tempo real iniciada para userId: $userId e contactId: $contactId")

        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("ChatActivity", "onChildAdded chamado")

                val senderId = snapshot.child("senderId").getValue(String::class.java) ?: ""
                val receiverId = snapshot.child("receiverId").getValue(String::class.java) ?: ""
                val text = snapshot.child("text").getValue(String::class.java) ?: ""
                val messageId = snapshot.child("messageId").getValue(String::class.java) ?: ""
                val participants = snapshot.child("participants").children.mapNotNull { it.getValue(String::class.java) }

                // Conversão segura do createdAt
                val rawCreatedAt = snapshot.child("createdAt").value
                val createdAt: Long = when (rawCreatedAt) {
                    is Long -> rawCreatedAt // Se já for Long, usa diretamente
                    is String -> rawCreatedAt.toLongOrNull() ?: 0L // Se for String, converte para Long
                    else -> 0L // Se for nulo ou tipo inesperado, define como 0L
                }

                val message = MessageRequest(
                    senderId = senderId,
                    receiverId = receiverId,
                    text = text,
                    participants = participants,
                    messageId = messageId,
                    createdAt = createdAt
                )

                if (message.participants.contains(userId) && message.participants.contains(contactId)) {
                    Log.d("ChatActivity", "Nova mensagem adicionada: ${message.text}, Timestamp: $createdAt")

                    runOnUiThread {
                        messages.add(message)
                        messages.sortBy { it.createdAt }
                        messagesAdapter.notifyDataSetChanged()

                        recyclerView.postDelayed({
                            recyclerView.scrollToPosition(messages.size - 1)
                        }, 100)
                    }
                } else {
                    Log.d("ChatActivity", "Mensagem recebida é nula ou não pertence à conversa")
                }
            }



            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("ChatActivity", "onChildChanged chamado")
                val message = snapshot.getValue(MessageRequest::class.java)
                if (message != null) {
                    val index = messages.indexOfFirst { it.messageId == message.messageId }
                    if (index != -1) {
                        messages[index] = message
                        messages.sortBy { it.createdAt }
                        messagesAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("ChatActivity", "onChildRemoved chamado")
                val message = snapshot.getValue(MessageRequest::class.java)
                if (message != null) {
                    messages.removeAll { it.messageId == message.messageId }
                    messagesAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("ChatActivity", "onChildMoved chamado")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatActivity", "Erro ao configurar atualizações em tempo real: ${error.message}")
            }
        })
    }
}
