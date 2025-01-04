package com.titolucas.mindlink.login.repository

import com.google.firebase.auth.FirebaseAuth
import com.titolucas.mindlink.login.data.LoginResult
import kotlinx.coroutines.tasks.await

class LoginRepository(private val firebaseAuth: FirebaseAuth) {

    suspend fun login(email: String, password: String): LoginResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            LoginResult.Success
        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "Erro desconhecido")
        }
    }
}