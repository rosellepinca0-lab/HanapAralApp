package com.hanapAral.app.data.model

data class StudyGroup(
    val id: String = "",
    val name: String = "",
    val subject: String = "",
    val description: String = "",
    val creatorId: String = "",
    val adminName: String = "",
    val members: List<String> = emptyList(),
    val maxMembers: Int = 50,
    val createdAt: Long = 0
)