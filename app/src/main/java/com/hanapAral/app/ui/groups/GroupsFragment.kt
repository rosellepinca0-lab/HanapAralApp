package com.hanapAral.app.ui.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.hanapAral.app.data.model.Announcement
import com.hanapAral.app.ui.screens.GroupsScreen
import com.hanapAral.app.util.showToast
import com.hanapAral.app.viewmodel.GroupViewModel

class GroupsFragment : Fragment() {

    private val groupViewModel: GroupViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val groups by groupViewModel.groups.observeAsState(emptyList())
                val isLoading by groupViewModel.loading.observeAsState(false)

                val currentUser = FirebaseAuth.getInstance().currentUser
                val currentUserId = currentUser?.uid ?: ""
                val currentUserName = currentUser?.displayName ?: "A user"

                GroupsScreen(
                    groups = groups,
                    isLoading = isLoading,
                    onGroupClick = { group ->
                        val intent = Intent(requireContext(), GroupDetailActivity::class.java)
                        intent.putExtra("EXTRA_GROUP_ID", group.id)
                        startActivity(intent)
                    },
                    onJoinClick = { group ->
                        groupViewModel.isJoinEnabled.value?.let { enabled ->
                            if (enabled) {
                                if (group.members.size >= (groupViewModel.maxMembers.value ?: 20)) {
                                    showToast("This group is full!")
                                } else if (group.members.contains(currentUserId)) {
                                    showToast("You are already a member!")
                                } else {
                                    groupViewModel.joinGroup(group.id, currentUserId)

                                    val notification = Announcement(
                                        content = "$currentUserName has joined the group!",
                                        authorName = "System",
                                        timestamp = System.currentTimeMillis()
                                    )
                                    groupViewModel.postAnnouncement(group.id, notification)
                                }
                            } else {
                                showToast("Joining groups is currently disabled by the administrator.")
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupViewModel.joinSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                showToast("Joined group successfully!")
                groupViewModel.loadGroups()
            }
        }

        groupViewModel.loadGroups()
        groupViewModel.loadRemoteConfig()
    }
}
