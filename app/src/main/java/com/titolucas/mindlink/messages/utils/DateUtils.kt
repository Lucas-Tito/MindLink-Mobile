package com.titolucas.mindlink.messages.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.text.ParseException

object DateUtils {
    fun formatDate(timestamp: Long): String {
        return try {
            val date = Date(timestamp) // Converte o timestamp em uma Data
            val sdf = SimpleDateFormat("h:mma", Locale.getDefault()) // Exemplo: "10:30PM"
            sdf.format(date)
        } catch (e: Exception) {
            "Invalid date"
        }
    }

}