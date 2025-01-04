package com.titolucas.mindlink.login.data

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}
