package com.titolucas.mindlink.service_hours.data

import java.util.Calendar
import java.util.Date

// Classe de dados Consulta
data class Horario(
    val dia: String,
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

// Função para obter o dia da semana a partir de uma data
fun getDiaDaSemana(data: Date): String {
    val calendar = Calendar.getInstance()
    calendar.time = data
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "Domingo"
        Calendar.MONDAY -> "Segunda"
        Calendar.TUESDAY -> "Terça"
        Calendar.WEDNESDAY -> "Quarta"
        Calendar.THURSDAY -> "Quinta"
        Calendar.FRIDAY -> "Sexta"
        Calendar.SATURDAY -> "Sábado"
        else -> "Desconhecido"
    }
}

// Array de consultas mockadas
val horariosMockados = arrayOf(
    Horario(getDiaDaSemana(createDate(2025, 1, 10, 14, 0)), createDate(2025, 1, 10, 14, 0)),
    Horario(getDiaDaSemana(createDate(2025, 1, 5, 9, 30)), createDate(2025, 1, 5, 9, 30)),
    Horario(getDiaDaSemana(createDate(2025, 1, 7, 16, 0)), createDate(2025, 1, 7, 16, 0)),
    Horario(getDiaDaSemana(createDate(2025, 1, 12, 11, 15)), createDate(2025, 1, 12, 11, 15))
)
