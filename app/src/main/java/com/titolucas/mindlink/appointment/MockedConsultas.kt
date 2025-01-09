package com.titolucas.mindlink.appointment

import java.util.Calendar
import java.util.Date

// Classe de dados Consulta
data class Consulta(
    val nomePsico: String,
    val status: String, // Exemplo: "Agendada", "Concluída", "Cancelada"
    val dataHora: Date // Usa a classe Date para representar data e hora
)

// Função auxiliar para criar uma instância de Date
private fun createDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Janeiro é 0
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    return calendar.time
}

// Array de consultas mockadas
val consultasMockadas = arrayOf(
    Consulta("João Silva", "Agendada", createDate(2025, 1, 10, 14, 0)),
    Consulta("Maria Oliveira", "Concluída", createDate(2025, 1, 5, 9, 30)),
    Consulta("Carlos Santos", "Cancelada", createDate(2025, 1, 7, 16, 0)),
    Consulta("Ana Costa", "Agendada", createDate(2025, 1, 12, 11, 15))
)
