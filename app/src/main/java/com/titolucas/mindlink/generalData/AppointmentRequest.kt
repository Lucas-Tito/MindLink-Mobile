package com.titolucas.mindlink.generalData

data class AppointmentRequest(
    val patientId: String,
    val patientName: String,
    val professionalId: String,
    val professionalName: String,
    val appointmentDate: AppointmentDateTime
)

data class AppointmentDateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minutes: Int,
    val seconds: Int = 0
)
