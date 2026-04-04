package com.hanapAral.app.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val course: String = "",
    val yearLevel: String = "",
    val photoUrl: String = "",
    val fcmToken: String = "",
    val isAdmin: Boolean = false
)