package com.titolucas.mindlink.messages.data

data class ConversationsRequest(
    val contactId: String,
    val contactName: String,
    val messages: List<MessageRequest>
)

data class Chat(
    val nomeChat: String,
    val lastMessageChat: String,
    val hora: String
)