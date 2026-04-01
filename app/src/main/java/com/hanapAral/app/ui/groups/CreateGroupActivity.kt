package com.hanapAral.app.ui.groups

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.google.firebase.auth.FirebaseAuth
import com.hanapAral.app.data.model.StudyGroup
import com.hanapAral.app.ui.screens.CreateGroupScreen
import com.hanapAral.app.viewmodel.GroupViewModel

class CreateGroupActivity : AppCompatActivity() {

    private val groupViewModel: GroupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isCreationEnabled by groupViewModel.isGroupCreationEnabled.observeAsState(true)
            val isLoading by groupViewModel.loading.observeAsState(false)
            val createSuccess by groupViewModel.createSuccess.observeAsState()
            val error by groupViewModel.error.observeAsState()

            createSuccess?.let {
                Toast.makeText(this, "Study group created!", Toast.LENGTH_SHORT).show()
                finish()
            }

            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }

            CreateGroupScreen(
                isCreationEnabled = isCreationEnabled,
                isLoading = isLoading,
                onBackClick = { finish() },
                onCreateClick = { name, subject, description ->
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val group = StudyGroup(
                            name = name,
                            subject = subject,
                            description = description,
                            creatorId = currentUser.uid,
                            adminName = currentUser.displayName ?: "Unknown",
                            members = listOf(currentUser.uid),
                            createdAt = System.currentTimeMillis()
                        )
                        groupViewModel.createGroup(group)
                    }
                }
            )
        }

        groupViewModel.loadRemoteConfig()
    }
}
