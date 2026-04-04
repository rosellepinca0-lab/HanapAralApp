package com.hanapAral.app.data.model

data class Announcement(
    val id: String = "",
    val content: String = "",
    val authorName: String = "",
    val timestamp: Long = 0,
    val type: String = "Group Announcement"
)
