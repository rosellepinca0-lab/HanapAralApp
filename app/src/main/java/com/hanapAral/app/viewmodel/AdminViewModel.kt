package com.hanapAral.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanapAral.app.data.model.User
import com.hanapAral.app.data.repository.RemoteConfigRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val remoteConfigRepository = RemoteConfigRepository()
    private val db = FirebaseFirestore.getInstance()

    private val _configLoaded = MutableLiveData<Boolean>()
    val configLoaded: LiveData<Boolean> = _configLoaded

    private val _isGroupCreationEnabled = MutableLiveData<Boolean>()
    val isGroupCreationEnabled: LiveData<Boolean> = _isGroupCreationEnabled

    private val _isJoinEnabled = MutableLiveData<Boolean>()
    val isJoinEnabled: LiveData<Boolean> = _isJoinEnabled

    private val _maxMembers = MutableLiveData<Long>()
    val maxMembers: LiveData<Long> = _maxMembers

    private val _announcementHeader = MutableLiveData<String>()
    val announcementHeader: LiveData<String> = _announcementHeader

    private val _newUsers = MutableLiveData<List<User>>()
    val newUsers: LiveData<List<User>> = _newUsers

    fun loadConfig() {
        viewModelScope.launch {
            remoteConfigRepository.fetchAndActivate()
            _isGroupCreationEnabled.value = remoteConfigRepository.isGroupCreationEnabled()
            _isJoinEnabled.value = remoteConfigRepository.isJoinGroupEnabled()
            _maxMembers.value = remoteConfigRepository.getMaxMembersPerGroup()
            _announcementHeader.value = remoteConfigRepository.getAnnouncementHeader()
            _configLoaded.value = true
        }
    }

    fun observeNewUsers() {
        db.collection("users")
            .limit(10)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                val users = snapshot?.documents?.mapNotNull { it.toObject(User::class.java) } ?: emptyList()
                _newUsers.value = users
            }
    }
}
