package com.titolucas.mindlink

import com.google.firebase.Timestamp

data class Message(
    val text: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val createdAt: Timestamp? = null  // Alterado para Timestamp
)
