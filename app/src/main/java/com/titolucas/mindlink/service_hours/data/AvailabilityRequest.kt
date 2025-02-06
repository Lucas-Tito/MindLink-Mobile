package com.titolucas.mindlink.service_hours.data

data class AvailabilityRequest(
    val dayOfWeek: String,
    val professionalId: String?,
    val startTime: StartTime,
    val endTime: EndTime
)

data class StartTime(
    val startHour: String,
    val startMinute: String,
)

data class EndTime(
    val endHour: String,
    val endMinute: String,
)