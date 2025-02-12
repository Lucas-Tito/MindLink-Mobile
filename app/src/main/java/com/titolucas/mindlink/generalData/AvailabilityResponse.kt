package com.titolucas.mindlink.generalData

data class AvailabilityResponse(
    val professionalId: String,
    val dayOfWeek: String,
    val startTime: TimeSlot,
    val endTime: TimeSlot,
    val availabilityId: String
)


data class TimeSlot(
    val hour: String,
    val minute: String
)
