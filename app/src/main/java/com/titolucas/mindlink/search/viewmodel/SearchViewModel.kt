package com.titolucas.mindlink.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titolucas.mindlink.generalData.UserResponse
import com.titolucas.mindlink.search.repository.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<UserResponse>>()
    val searchResults: LiveData<List<UserResponse>> get() = _searchResults

    fun searchPsychologists(query: String) {
        viewModelScope.launch {
            try {
                val results = repository.searchPsychologists(query)
                _searchResults.postValue(results)
            } catch (e: Exception) {
                _searchResults.postValue(emptyList())
            }
        }
    }
}