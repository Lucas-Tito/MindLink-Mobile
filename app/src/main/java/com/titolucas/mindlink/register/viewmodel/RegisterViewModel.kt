package com.titolucas.mindlink.register.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titolucas.mindlink.register.data.RegisterResult
import com.titolucas.mindlink.register.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: RegisterRepository) : ViewModel() {

    private val _registerState = MutableLiveData<RegisterResult>()
    val registerState: LiveData<RegisterResult> get() = _registerState

    fun register(name: String, email: String, password: String, photoUri: Uri?) {
        viewModelScope.launch {
            val result = repository.registerUser(name, email, password, photoUri)
            _registerState.postValue(result)
        }
    }
}
