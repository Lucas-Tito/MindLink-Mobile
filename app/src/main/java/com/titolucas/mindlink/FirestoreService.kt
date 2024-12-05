package com.titolucas.mindlink

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirestoreService {

    private val firestore = FirebaseFirestore.getInstance()
    private val messagesRef = firestore.collection("messages")

    // Função para enviar uma mensagem
    fun sendMessage(message: Message, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        messagesRef.add(message)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Função para buscar mensagens
    fun fetchMessages(onMessagesFetched: (List<Message>) -> Unit, onError: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Referência à coleção "messages"
        val messagesRef = db.collection("messages")

        messagesRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val messages = mutableListOf<Message>()

                for (document in result) {
                    val message = document.toObject(Message::class.java)
                    messages.add(message)
                }

                // Chama o callback com a lista de mensagens
                onMessagesFetched(messages)
            }
            .addOnFailureListener { exception ->
                // Chama o callback com erro
                onError(exception)
            }
    }

}
