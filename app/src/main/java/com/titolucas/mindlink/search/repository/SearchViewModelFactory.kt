package com.titolucas.mindlink.search.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.titolucas.mindlink.search.viewmodel.SearchViewModel

class SearchViewModelFactory(
    private val repository: SearchRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}