package com.titolucas.mindlink.messages.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.titolucas.mindlink.R
import com.titolucas.mindlink.messages.data.MessageRequest

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        val contactId = intent.getStringExtra("contactId")
        val contactName = intent.getStringExtra("contactName")
        val messages = intent.getParcelableArrayListExtra<MessageRequest>("messages")

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messages?.let { ChatMessagesAdapter(it) }

        // Configurar o nome do chat
        findViewById<TextView>(R.id.nomeChat).text = contactName
    }
}