package com.titolucas.mindlink.generalData

data class UserResponse(
    val uid: String,
    val email: String,
    val name: String,
    val lastname: String?,
    val title: String?,
    val professionalType: Boolean,
    val photoURL: String?,
    val password: String? = null,
    val education: String?= null,
    val bio: String?,
    val rating:Double? = null

)
