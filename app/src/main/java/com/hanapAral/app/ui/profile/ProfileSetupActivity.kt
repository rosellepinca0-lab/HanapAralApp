package com.hanapAral.app.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.hanapAral.app.R
import com.hanapAral.app.data.model.User
import com.hanapAral.app.ui.home.HomeActivity
import com.hanapAral.app.ui.screens.ProfileSetupScreen
import com.hanapAral.app.util.showToast
import com.hanapAral.app.viewmodel.ProfileViewModel

class ProfileSetupActivity : AppCompatActivity() {

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uid = intent.getStringExtra("uid") ?: ""
        val name = intent.getStringExtra("name") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val photoUrl = intent.getStringExtra("photoUrl")

        setContent {
            val isLoading by profileViewModel.loading.observeAsState(false)

            ProfileSetupScreen(
                initialName = name,
                initialEmail = email,
                yearLevels = resources.getStringArray(R.array.year_levels),
                isLoading = isLoading,
                onSaveClick = { _, _, course, yearLevel, isAdmin ->
                    val user = User(
                        uid = uid,
                        name = name,
                        email = email,
                        course = course,
                        yearLevel = yearLevel,
                        photoUrl = photoUrl ?: "",
                        isAdmin = isAdmin
                    )
                    profileViewModel.saveProfile(user)
                }
            )
        }

        observeViewModel()
    }
