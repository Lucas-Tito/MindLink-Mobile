package com.titolucas.mindlink.search.repository

import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.network.RetrofitInstance



class SearchRepository {

    private val apiService = RetrofitInstance.apiService

    suspend fun searchPsychologists(query: String): List<UserResponse> {
        return apiService.searchPsychologists(query)
    }
}