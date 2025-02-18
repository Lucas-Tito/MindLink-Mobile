package com.titolucas.mindlink.generalData

data class UserResponse(
    val uid: String,
    val email: String,
    val name: String,
    val lastname: String?,
    val title: String?,
    val professionalType: Boolean,
    val photoURL: String?,
    val password: String? = null,
    val education: String?= null,
    val bio: String?,
    val rating:Double? = null

)

data class Appointment(
    val appointmentId: String,
    val patientName: String,
    val patientId: String,
    val professionalName: String,
    val professionalId: String,
    val appointmentDate: AppointmentDate,
    val status: String
)

data class AppointmentDate(
    val seconds: Int,
    val hour: String,
    val minutes: String,
    val day: Int,
    val year: Int,
    val month: Int
)

data class ServerResponse(
    val status: String,  // Exemplo: "Consulta criada com sucesso!"
    val message: String?, // Se a consulta for criada, retorna o ID
    val appointmentId: String?
)

