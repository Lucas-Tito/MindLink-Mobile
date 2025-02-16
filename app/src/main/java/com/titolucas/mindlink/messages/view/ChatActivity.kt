package com.titolucas.mindlink.messages.view

import android.os.Bundle
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
    private var messages: ArrayList<MessageRequest> = arrayListOf()
    private lateinit var messagesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        val photoURL = intent.getStringExtra("photoURL")
        val contactImage = findViewById<ImageView>(R.id.contact_image)

        // Carregar a imagem usando Glide
        Glide.with(this)
            .load(photoURL)
            .placeholder(R.drawable.ic_profile) // Imagem de placeholder
            .error(R.drawable.ic_profile) // Imagem de erro
            .into(contactImage)

        val returnButton = findViewById<ImageButton>(R.id.chat_return_button)
        returnButton.setOnClickListener {
            finish()
        }

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

        // Configurar listener para atualizações em tempo real
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
                database.child("messages").child(messageId).setValue(messageRequest)
                editTextMessage.text.clear()
            } else {
                // Tratar erro de envio de mensagem
            }
        }
    }

    private fun setupRealtimeUpdates(contactId: String?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        messagesRef = FirebaseDatabase.getInstance().getReference("messages")

        messagesRef.orderByChild("participants").equalTo(listOf(userId, contactId).toString())
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(MessageRequest::class.java)
                    if (message != null) {
                        messages.add(message)
                        messages.sortBy { it.createdAt }
                        messagesAdapter.notifyDataSetChanged()
                        recyclerView.scrollToPosition(messages.size - 1)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // Implementar se necessário
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // Implementar se necessário
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // Implementar se necessário
                }

                override fun onCancelled(error: DatabaseError) {
                    // Tratar erro
                }
            })
    }
}