package com.titolucas.mindlink.profile.repository


import com.titolucas.mindlink.generalData.AvailabilityResponse
import com.titolucas.mindlink.generalData.TimeSlot
import com.titolucas.mindlink.profile.data.UserRequest
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.network.RetrofitInstance

class ProfileRepository {

    private val apiService = RetrofitInstance.apiService

    suspend fun getAllUsers(): List<UserResponse> {
        return apiService.getAllUsers()
    }

    suspend fun getUserById(userId: String): UserResponse {
        return apiService.getUserById(userId)
    }

    suspend fun checkIfUserIsProfessional(userId: String): Boolean {
        return apiService.checkIfUserIsProfessional(userId)
    }

    suspend fun getProfessionalUsers(): List<UserResponse> {
        return apiService.getProfessionalUsers()
    }

    suspend fun createUser(userRequest: UserRequest): Map<String, String> {
        return apiService.createUser(userRequest)
    }

    suspend fun getAvailability(professionalId: String): List<AvailabilityResponse> {
        return try {
            val response = RetrofitInstance.apiService.getAvailability(professionalId)
            response.map { item ->
                val startTimeMap = item["startTime"] as? Map<String, String> ?: emptyMap()
                val endTimeMap = item["endTime"] as? Map<String, String> ?: emptyMap()

                AvailabilityResponse(
                    professionalId = item["professionalId"] as? String ?: "",
                    dayOfWeek = item["dayOfWeek"] as? String ?: "",
                    startTime = TimeSlot(
                        hour = startTimeMap["startHour"] ?: "00",
                        minute = startTimeMap["startMinute"] ?: "00"
                    ),
                    endTime = TimeSlot(
                        hour = endTimeMap["endHour"] ?: "00",
                        minute = endTimeMap["endMinute"] ?: "00"
                    ),
                    availabilityId = item["availabilityId"] as? String ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

}
