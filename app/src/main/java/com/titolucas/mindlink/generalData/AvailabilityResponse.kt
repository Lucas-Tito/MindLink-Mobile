package com.titolucas.mindlink.generalData

data class AvailabilityResponse(
    val professionalId: String,
    val dayOfWeek: String,
    val startTime: StartTime,
    val endTime: EndTime,
    val availabilityId: String
)


data class StartTime(
    val startHour: String,
    val startMinute: String,
)

data class EndTime(
    val endHour: String,
    val endMinute: String,
)
