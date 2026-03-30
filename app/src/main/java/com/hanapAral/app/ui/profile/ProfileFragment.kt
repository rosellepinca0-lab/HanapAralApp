package com.hanapAral.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.hanapAral.app.R
import com.hanapAral.app.data.model.User
import com.hanapAral.app.ui.screens.ProfileScreen
import com.hanapAral.app.util.showToast
import com.hanapAral.app.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val userProfile by profileViewModel.userProfile.observeAsState()
                val isLoading by profileViewModel.loading.observeAsState(false)
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    ProfileScreen(
                        name = currentUser.displayName ?: "",
                        email = currentUser.email ?: "",
                        photoUrl = currentUser.photoUrl?.toString(),
                        initialCourse = userProfile?.course ?: "",
                        initialYearLevel = userProfile?.yearLevel ?: "",
                        yearLevels = resources.getStringArray(R.array.year_levels),
                        isLoading = isLoading,
                        onUpdateClick = { course, yearLevel ->
                            val updatedUser = User(
                                uid = currentUser.uid,
                                name = currentUser.displayName ?: "",
                                email = currentUser.email ?: "",
                                photoUrl = currentUser.photoUrl?.toString() ?: "",
                                course = course,
                                yearLevel = yearLevel,
                                isAdmin = userProfile?.isAdmin ?: false
                            )
                            profileViewModel.saveProfile(updatedUser)
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        if (currentUser == null) {
            showToast("User not logged in")
            return
        }

        profileViewModel.getUserProfile(currentUser.uid)

        profileViewModel.saveSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                showToast("Profile updated successfully!")
            }
        }

        profileViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let { showToast(it) }
        }
    }
}
