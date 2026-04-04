package com.hanapAral.app.ui.admin

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.hanapAral.app.ui.screens.AdminPanelScreen
import com.hanapAral.app.viewmodel.AdminViewModel

class AdminPanelActivity : AppCompatActivity() {

    private val adminViewModel: AdminViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isLoaded by adminViewModel.configLoaded.observeAsState(false)
            val groupCreationEnabled by adminViewModel.isGroupCreationEnabled.observeAsState(true)
            val joinEnabled by adminViewModel.isJoinEnabled.observeAsState(true)
            val maxMembers by adminViewModel.maxMembers.observeAsState(20L)
            val announcementHeader by adminViewModel.announcementHeader.observeAsState("")
            val users by adminViewModel.newUsers.observeAsState(emptyList())

            AdminPanelScreen(
                groupCreationEnabled = groupCreationEnabled,
                joinEnabled = joinEnabled,
                maxMembers = maxMembers,
                announcementHeader = announcementHeader,
                notifications = users.map { "New user joined: ${it.name}" },
                isLoading = !isLoaded,
                onBackClick = { finish() }
            )
        }

        adminViewModel.loadConfig()
        adminViewModel.observeNewUsers()
    }
}
