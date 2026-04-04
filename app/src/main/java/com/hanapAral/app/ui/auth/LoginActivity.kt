package com.hanapAral.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hanapAral.app.data.repository.AuthRepository
import com.hanapAral.app.ui.home.HomeActivity
import com.hanapAral.app.ui.profile.ProfileSetupActivity
import com.hanapAral.app.ui.screens.LoginScreen
import com.hanapAral.app.util.showToast
import com.hanapAral.app.viewmodel.AuthViewModel
import com.hanapAral.app.viewmodel.ProfileViewModel

class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(AuthRepository(applicationContext)) as T
            }
        }
    }

    private val profileViewModel: ProfileViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        authViewModel.handleGoogleSignInResult(result.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isLoading by authViewModel.loading.observeAsState(false)
            LoginScreen(
                isLoading = isLoading,
                onGoogleSignInClick = {
                    signInLauncher.launch(authViewModel.getSignInIntent())
                }
            )
        }

        authViewModel.getCurrentUser()?.let { user ->
            profileViewModel.checkProfileExists(user.uid)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        authViewModel.user.observe(this) { user ->
            user?.let {
                profileViewModel.checkProfileExists(it.uid)
            }
        }

        authViewModel.error.observe(this) { errorMsg ->
            errorMsg?.let { showToast(it) }
        }

        profileViewModel.profileExists.observe(this) { exists ->
            if (exists) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                val intent = Intent(this, ProfileSetupActivity::class.java)
                val user = authViewModel.getCurrentUser()
                intent.putExtra("uid", user?.uid)
                intent.putExtra("name", user?.displayName)
                intent.putExtra("email", user?.email)
                intent.putExtra("photoUrl", user?.photoUrl?.toString())
                startActivity(intent)
                finish()
            }
        }
    }
}
