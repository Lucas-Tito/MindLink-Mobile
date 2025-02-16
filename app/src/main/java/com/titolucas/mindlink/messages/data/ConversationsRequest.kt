package com.titolucas.mindlink.messages.data

data class ConversationsRequest(
    val contactId: String,
    val contactName: String,
    val messages: List<MessageRequest>
)

