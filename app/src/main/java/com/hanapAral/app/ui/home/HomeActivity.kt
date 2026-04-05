package com.hanapAral.app.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.hanapAral.app.R
import com.hanapAral.app.data.model.Announcement
import com.hanapAral.app.data.model.StudyGroup
import com.hanapAral.app.data.model.User
import com.hanapAral.app.data.repository.AuthRepository
import com.hanapAral.app.ui.admin.AdminPanelActivity
import com.hanapAral.app.ui.auth.LoginActivity
import com.hanapAral.app.ui.groups.GroupDetailActivity
import com.hanapAral.app.ui.screens.GroupsScreen
import com.hanapAral.app.ui.screens.HomeScreen
import com.hanapAral.app.ui.screens.ProfileScreen
import com.hanapAral.app.util.BiometricHelper
import com.hanapAral.app.util.showToast
import com.hanapAral.app.viewmodel.AuthViewModel
import com.hanapAral.app.viewmodel.GroupViewModel
import com.hanapAral.app.viewmodel.ProfileViewModel

class HomeActivity : AppCompatActivity() {

    private var isShowingProfile by mutableStateOf(false)
    private var isEditingProfile by mutableStateOf(false)
    private var showCreateGroupDialog by mutableStateOf(false)

    private val authViewModel: AuthViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(AuthRepository(applicationContext)) as T
            }
        }
    }

    private val groupViewModel: GroupViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showToast("Notifications disabled. You might miss important updates.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        // Get FCM Token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM_TOKEN", "Token: $token")
        }

        setContent {
            val userProfile by profileViewModel.userProfile.observeAsState()
            val currentUser = FirebaseAuth.getInstance().currentUser

            val isAdmin = userProfile?.isAdmin == true || currentUser?.email == "roellepinca0@gmail.com"

            if (showCreateGroupDialog) {
                CreateGroupDialog(
                    onDismiss = { showCreateGroupDialog = false },
                    onCreate = { name, subject, description ->
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
                            showCreateGroupDialog = false
                        }
                    }
                )
            }

            HomeScreen(
                isAdmin = isAdmin,
                isProfileShowing = isShowingProfile,
                isEditingProfile = isEditingProfile,
                onAdminClick = {
                    BiometricHelper.showBiometricPrompt(
                        this,
                        onSuccess = {
                            startActivity(Intent(this, AdminPanelActivity::class.java))
                        },
                        onFailed = {
                            showToast("Biometric authentication failed")
                        }
                    )
                },
                onProfileClick = {
                    isShowingProfile = !isShowingProfile
                    if (!isShowingProfile) {
                        isEditingProfile = false
                    }
                },
                onEditClick = {
                    isEditingProfile = !isEditingProfile
                },
                onSignOutClick = {
                    authViewModel.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                },
                onCreateGroupClick = {
                    groupViewModel.isGroupCreationEnabled.value?.let { enabled ->
                        if (enabled) {
                            showCreateGroupDialog = true
                        } else {
                            showToast("Group creation is currently disabled by the administrator.")
                        }
                    }
                }
            ) {
                if (isShowingProfile) {
                    val profileLoading by profileViewModel.loading.observeAsState(false)

                    if (currentUser != null) {
                        ProfileScreen(
                            name = currentUser.displayName ?: "",
                            email = currentUser.email ?: "",
                            photoUrl = currentUser.photoUrl?.toString(),
                            initialCourse = userProfile?.course ?: "",
                            initialYearLevel = userProfile?.yearLevel ?: "",
                            yearLevels = resources.getStringArray(R.array.year_levels),
                            isLoading = profileLoading,
                            isReadOnly = !isEditingProfile,
                            onUpdateClick = { course, yearLevel ->
                                val updatedUser = User(
                                    uid = currentUser.uid,
                                    name = currentUser.displayName ?: "",
                                    email = currentUser.email ?: "",
                                    photoUrl = currentUser.photoUrl?.toString() ?: "",
                                    course = course,
                                    yearLevel = yearLevel,
                                    isAdmin = isAdmin
                                )
                                profileViewModel.saveProfile(updatedUser)
                                isEditingProfile = false
                            }
                        )
                    }
                } else {
                    val groups by groupViewModel.groups.observeAsState(emptyList())
                    val groupsLoading by groupViewModel.loading.observeAsState(false)
                    val currentUserId = currentUser?.uid ?: ""
                    val currentUserName = currentUser?.displayName ?: "A user"

                    GroupsScreen(
                        groups = groups,
                        currentUserId = currentUserId,
                        isLoading = groupsLoading,
                        onGroupClick = { group ->
                            val intent = Intent(this@HomeActivity, GroupDetailActivity::class.java)
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

        setupObservers()
        groupViewModel.loadRemoteConfig()
        groupViewModel.loadGroups()

        authViewModel.getCurrentUser()?.let { user ->
            profileViewModel.getUserProfile(user.uid)
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupObservers() {
        groupViewModel.joinSuccess.observe(this) { success ->
            if (success) {
                showToast("Joined group successfully!")
                groupViewModel.loadGroups()
            }
        }
        groupViewModel.createSuccess.observe(this) { groupId ->
            if (groupId != null) {
                showToast("Study group created!")
                groupViewModel.loadGroups()
            }
        }
        profileViewModel.saveSuccess.observe(this) { success ->
            if (success) {
                showToast("Profile updated successfully!")
            }
        }
        profileViewModel.error.observe(this) { errorMsg ->
            errorMsg?.let { showToast(it) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Study Group",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_primary)
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Group Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colorResource(id = R.color.accent_primary)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = subject,
                    onValueChange = { subject = it },
                    placeholder = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colorResource(id = R.color.accent_primary)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colorResource(id = R.color.accent_primary)
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onCreate(name, subject, description) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = name.isNotBlank() && subject.isNotBlank(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.accent_primary)
                    )
                ) {
                    Text("CREATE", fontWeight = FontWeight.Bold)
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        }
    }
}
