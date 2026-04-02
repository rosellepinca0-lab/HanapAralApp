package com.hanapAral.app.ui.groups

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.google.firebase.auth.FirebaseAuth
import com.hanapAral.app.data.model.Announcement
import com.hanapAral.app.data.model.ChatMessage
import com.hanapAral.app.ui.screens.GroupDetailScreen
import com.hanapAral.app.viewmodel.GroupViewModel
import com.hanapAral.app.viewmodel.ProfileViewModel

class GroupDetailActivity : AppCompatActivity() {

    private val groupViewModel: GroupViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private var groupId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        groupId = intent.getStringExtra("EXTRA_GROUP_ID") ?: ""

        setContent {
            val group by groupViewModel.currentGroup.observeAsState()
            val announcements by groupViewModel.announcements.observeAsState(emptyList())
            val chatMessages by groupViewModel.chatMessages.observeAsState(emptyList())
            val isLoading by groupViewModel.loading.observeAsState(false)
            val userProfile by profileViewModel.userProfile.observeAsState()
            val isAnnouncementPostingEnabled by groupViewModel.isAnnouncementPostingEnabled.observeAsState(true)

            val currentUser = FirebaseAuth.getInstance().currentUser
            val currentUserId = currentUser?.uid ?: ""

            // Priority check for Roselle's email to ensure she can always post
            val isGlobalAdmin = userProfile?.isAdmin == true || currentUser?.email == "roellepinca0@gmail.com"
            val isGroupCreator = currentUser?.uid == group?.creatorId

            // Allow posting if feature is enabled in Remote Config AND user is an admin/creator
            val canPostAnnouncements = isAnnouncementPostingEnabled && (isGlobalAdmin || isGroupCreator)