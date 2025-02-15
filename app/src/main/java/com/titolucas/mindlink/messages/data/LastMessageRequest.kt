package com.titolucas.mindlink.messages.data

data class LastMessageRequest (
    val contactUserId: String,
    val lastMessage: String,
    val createdAt: String,
    val contactName: String
)
