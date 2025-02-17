package com.titolucas.mindlink.profile.repository


import com.titolucas.mindlink.generalData.AvailabilityResponse
import com.titolucas.mindlink.generalData.EndTime
import com.titolucas.mindlink.generalData.StartTime

//import com.titolucas.mindlink.generalData.TimeSlot
import android.util.Log
import com.titolucas.mindlink.profile.data.UserRequest
import com.titolucas.mindlink.generalData.UpdateUserResponse
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
                val startTimeMap = item.startTime as? Map<String, String> ?: emptyMap()
                val endTimeMap = item.endTime as? Map<String, String> ?: emptyMap()

                AvailabilityResponse(
                    professionalId = item.professionalId.toString(),
                    dayOfWeek = item.dayOfWeek,
                    startTime = StartTime(
                        startHour = item.startTime.startHour  as? String ?: "00",
                        startMinute = item.startTime.startMinute as? String ?: "00"
                    ),
                    endTime = EndTime(
                        endHour = item.endTime.endHour  as? String ?: "00",
                        endMinute = item.endTime.endMinute as? String ?: "00"
                    ),
                    availabilityId = item.availabilityId as? String ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateUser(userId: String, updates: Map<String, Any>): UpdateUserResponse {
        Log.d("ProfileRepository", "Chamando updateUser com userId: $userId, updates: $updates")
        val response = apiService.updateUser(userId, updates)
        Log.d("ProfileRepository", "Resposta do updateUser: $response")
        return response
    }
}
