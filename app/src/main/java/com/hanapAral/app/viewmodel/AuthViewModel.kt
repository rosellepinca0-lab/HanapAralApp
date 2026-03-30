package com.hanapAral.app.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.hanapAral.app.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getSignInIntent(): Intent = authRepository.getSignInIntent()

    fun getCurrentUser(): FirebaseUser? = authRepository.getCurrentUser()

    fun handleGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val firebaseUser = authRepository.signInWithGoogle(account.idToken!!)
                _user.value = firebaseUser
            } catch (e: Exception) {
                _error.value = e.message ?: "Sign-in failed"
            } finally {
                _loading.value = false
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
    }
}