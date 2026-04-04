package com.hanapAral.app.data.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

class RemoteConfigRepository {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) // 0 for dev; use 3600 for production
            .build()
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.setDefaultsAsync(
            mapOf(
                "is_group_creation_enabled" to true,
                "is_join_group_enabled" to true,
                "is_announcement_posting_enabled" to true,
                "max_members_per_group" to 10L,
                "announcement_header" to "Study Group Announcements"
            )
        )
    }

    suspend fun fetchAndActivate(): Boolean {
        return remoteConfig.fetchAndActivate().await()
    }

    fun isGroupCreationEnabled(): Boolean =
        remoteConfig.getBoolean("is_group_creation_enabled")

    fun isJoinGroupEnabled(): Boolean =
        remoteConfig.getBoolean("is_join_group_enabled")

    fun isAnnouncementPostingEnabled(): Boolean =
        remoteConfig.getBoolean("is_announcement_posting_enabled")

    fun getMaxMembersPerGroup(): Long =
        remoteConfig.getLong("max_members_per_group")

    fun getAnnouncementHeader(): String =
        remoteConfig.getString("announcement_header")
