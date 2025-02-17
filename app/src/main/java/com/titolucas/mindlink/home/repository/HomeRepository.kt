package com.titolucas.mindlink.home.repository

import com.titolucas.mindlink.generalData.Appointment
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.network.RetrofitInstance

class HomeRepository {

    private val apiService = RetrofitInstance.apiService

    suspend fun getProfessionalUsers(): List<UserResponse> {
        return apiService.getProfessionalUsers()
    }

    suspend fun searchPsychologists(query: String): List<UserResponse> {
        return apiService.searchPsychologists(query)
    }
    suspend fun getAppointmentsByProfessionalIdInCurrentMonth(professionalId: String): List<Appointment> {
        return apiService.getAppointmentsByProfessionalIdInCurrentMonth(professionalId)
    }
    suspend fun getAppointmentsByPatientIdInCurrentMonth(professionalId: String): List<Appointment> {
        return apiService.getAppointmentsByPatientIdInCurrentMonth(professionalId)
    }

    suspend fun getAppointmentsByUserIdInMonth(userId: String,selectedMonth: Int , selectedYear: Int): List<Appointment> {
        return apiService.getAppointmentsByUserIdInMonth(userId,selectedMonth,selectedYear)
    }

    suspend fun updateAppointmentStatus(appointmentId: String, newStatus: Map<String, String>): Boolean {
        return try {
            val response = RetrofitInstance.apiService.updateAppointmentStatus(appointmentId, newStatus)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
