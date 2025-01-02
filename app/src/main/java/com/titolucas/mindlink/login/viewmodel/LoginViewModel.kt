package com.titolucas.mindlink.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titolucas.mindlink.login.repository.LoginRepository
import com.titolucas.mindlink.login.data.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _loginState = MutableLiveData<LoginResult>()
    val loginState: LiveData<LoginResult> get() = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginState.postValue(result)
        }
    }
}
