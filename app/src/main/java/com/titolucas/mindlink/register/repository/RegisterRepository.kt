package com.titolucas.mindlink.register.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.titolucas.mindlink.register.data.RegisterResult
import kotlinx.coroutines.tasks.await

class RegisterRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    suspend fun registerUser(name: String, email: String, password: String, photoUri: Uri?): RegisterResult {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
                ?: return RegisterResult.Error("Erro ao criar usuário")

            val uid = user.uid
            val photoUrl = photoUri?.let { uploadPhoto(uid, it) }

            val userMap = hashMapOf(
                "uid" to uid,
                "name" to name,
                "email" to email,
                "password" to password,
                "photoURL" to photoUrl
            )

            firestore.collection("Users").document(uid).set(userMap).await()
            RegisterResult.Success
        } catch (e: Exception) {
            RegisterResult.Error(e.message ?: "Erro desconhecido")
        }
    }

    private suspend fun uploadPhoto(uid: String, photoUri: Uri): String? {
        val storageRef = storage.reference.child("user-photos/$uid/${photoUri.lastPathSegment}")
        val uploadTask = storageRef.putFile(photoUri).await()
        return storageRef.downloadUrl.await().toString()
    }
}
