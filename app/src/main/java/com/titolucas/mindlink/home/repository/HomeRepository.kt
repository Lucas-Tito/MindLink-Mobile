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
}
