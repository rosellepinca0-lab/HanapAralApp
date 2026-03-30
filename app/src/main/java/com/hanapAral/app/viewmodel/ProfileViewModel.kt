package com.hanapAral.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanapAral.app.data.model.User
import com.hanapAral.app.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> = _userProfile

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _profileExists = MutableLiveData<Boolean>()
    val profileExists: LiveData<Boolean> = _profileExists

    fun getUserProfile(uid: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val user = userRepository.getUser(uid)
                _userProfile.value = user
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun saveProfile(user: User) {
        viewModelScope.launch {
            _loading.value = true
            try {
                userRepository.saveUser(user)
                _userProfile.value = user
                _saveSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun checkProfileExists(uid: String) {
        viewModelScope.launch {
            try {
                val exists = userRepository.profileExists(uid)
                _profileExists.value = exists
            } catch (e: Exception) {
                _error.value = e.message
                _profileExists.value = false
            }
        }
    }
}
