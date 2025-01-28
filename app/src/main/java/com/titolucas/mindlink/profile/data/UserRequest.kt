package com.titolucas.mindlink.profile.data

data class UserRequest(
    val email: String,
    val name: String,
    val password: String,
    val professionalType: Boolean,
    val photoURL: String?
)


