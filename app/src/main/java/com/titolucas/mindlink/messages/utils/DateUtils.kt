package com.titolucas.mindlink.messages.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.text.ParseException

object DateUtils {
    fun formatDate(isoDateString: String): String {
        return try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = isoFormat.parse(isoDateString)
            val sdf = SimpleDateFormat("h:mma", Locale.getDefault())
            sdf.format(date)
        } catch (e: ParseException) {
            "Invalid date"
        }
    }
}