package com.titolucas.mindlink.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.home.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<UserResponse>>()
    val searchResults: LiveData<List<UserResponse>> get() = _searchResults

    private val _professionalUsers = MutableLiveData<List<UserResponse>>()
    val professionalUsers: LiveData<List<UserResponse>> get() = _professionalUsers

    fun loadProfessionalUsers() {
        viewModelScope.launch {
            val results = repository.getProfessionalUsers()
            _professionalUsers.postValue(results)
        }
    }

    fun searchPsychologists(query: String) {
        viewModelScope.launch {
            val results = repository.searchPsychologists(query)
            _searchResults.postValue(results)
        }
    }
}
