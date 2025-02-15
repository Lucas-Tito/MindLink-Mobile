//package com.titolucas.mindlink.messages.data
//
//import java.util.Date
//
//// Classe de dados Consulta
//data class Chat(
//    val nomeChat: String,
//    val lastMessageChat: String,
//    val hora: String
//)
//
//// Função para limitar a mensagem a 29 caracteres
//fun limitarMensagem(mensagem: String, limite: Int = 29): String {
//    return if (mensagem.length > limite) mensagem.take(limite) + "..." else mensagem
//}
//
//// Array de consultas mockadas
//val consultasMockadas = arrayOf(
//    Chat(
//        nomeChat = "Ayrton Senna",
//        lastMessageChat = limitarMensagem("Não esqueçam do almoço amanhã, todos confirmados?"),
//        hora = "2:14 PM"
//    ),
//    Chat(
//        nomeChat = "Michèle Mouton",
//        lastMessageChat = limitarMensagem("Reunião marcada para 14h, preparem os slides."),
//        hora = "10:30 AM"
//    )
//)