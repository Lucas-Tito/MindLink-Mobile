package com.titolucas.mindlink.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titolucas.mindlink.profile.data.UserResponse
import com.titolucas.mindlink.profile.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val _userDetails = MutableLiveData<UserResponse>()
    val userDetails: LiveData<UserResponse> get() = _userDetails

    fun fetchUserById(userId: String) {
        viewModelScope.launch {
            val user = repository.getUserById(userId)
            _userDetails.postValue(user)
        }
    }
}
