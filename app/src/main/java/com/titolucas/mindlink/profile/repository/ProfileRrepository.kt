package com.titolucas.mindlink.profile.repository


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
}
