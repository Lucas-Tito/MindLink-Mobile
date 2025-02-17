package com.titolucas.mindlink.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titolucas.mindlink.generalData.AvailabilityResponse
import com.titolucas.mindlink.generalData.UpdateUserResponse
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.profile.data.UserRequest
import com.titolucas.mindlink.profile.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val _userDetails = MutableLiveData<UserResponse>()
    private val _availability = MutableLiveData<List<AvailabilityResponse>>()
    val userDetails: LiveData<UserResponse> get() = _userDetails
    val availability: LiveData<List<AvailabilityResponse>> get() = _availability

    fun fetchAvailability(professionalId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAvailability(professionalId)
                _availability.postValue(response)
            } catch (e: Exception) {
                _availability.postValue(emptyList()) // Retorna uma lista vazia em caso de erro
            }
        }
    }

    private val _updateResponse = MutableLiveData<UpdateUserResponse>()
    val updateResponse: LiveData<UpdateUserResponse> get() = _updateResponse

    fun fetchUserById(userId: String) {
        viewModelScope.launch {
            val user = repository.getUserById(userId)
            _userDetails.postValue(user)
        }
    }

    fun updateUser(userId: String, updates: Map<String, Any>) {
        viewModelScope.launch {
            try {
                val response = repository.updateUser(userId, updates)
                _updateResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Erro ao atualizar usuário: ${e.message}", e)
            }
        }
    }
}