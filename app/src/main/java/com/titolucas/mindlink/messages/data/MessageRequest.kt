package com.titolucas.mindlink.messages.data

data class MessageRequest(
    val senderId: String,
    val receiverId: String,
    val text: String,
    val participants: List<String>,
    val messageId: String,
    val createdAt: String
)