package com.titolucas.mindlink

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp


class ChatActivity : AppCompatActivity() {

    private lateinit var firestoreService: FirestoreService
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Inicializando FirestoreService
        firestoreService = FirestoreService()

        // Configurando RecyclerView e Adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messageList = mutableListOf()
        messageAdapter = MessageAdapter(messageList)
        recyclerView.adapter = messageAdapter

        // Configurando botão de envio de mensagens
        findViewById<Button>(R.id.buttonSend).setOnClickListener {
            val messageText = findViewById<EditText>(R.id.editTextMessage).text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(
                    text = messageText,
                    createdAt = Timestamp.now(),
                    senderId = "SeuSenderId",
                    receiverId = "SeuReceiverId"
                )
                firestoreService.sendMessage(
                    message = message,
                    onSuccess = {
                        findViewById<EditText>(R.id.editTextMessage).text.clear()
                    },
                    onFailure = { exception ->
                        println("Erro ao enviar mensagem: ${exception.message}")
                    }
                )
            }
        }

        // Lendo mensagens em tempo real
        firestoreService.fetchMessages(
            onMessagesFetched = { messages ->
                if (messages.isNotEmpty()) {
                    messageList.clear()
                    messageList.addAll(messages)
                    messageAdapter.notifyDataSetChanged()
                    println("Mensagens carregadas com sucesso: $messages")
                } else {
                    println("Nenhuma mensagem encontrada.")
                }
            },
            onError = { exception ->
                println("Erro ao carregar mensagens: ${exception.message}")
            }
        )

    }
}
