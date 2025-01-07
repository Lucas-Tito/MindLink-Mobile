package com.titolucas.mindlink.profile.data

data class UserResponse(
    val uid: String,
    val email: String,
    val name: String,
    val professionalType: Boolean,
    val photoURL: String?,
    val password: String? = null,
    val bio: String?
)
