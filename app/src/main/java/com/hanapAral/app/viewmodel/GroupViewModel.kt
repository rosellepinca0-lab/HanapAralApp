package com.hanapAral.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanapAral.app.data.model.Announcement
import com.hanapAral.app.data.model.ChatMessage
import com.hanapAral.app.data.model.StudyGroup
import com.hanapAral.app.data.repository.GroupRepository
import com.hanapAral.app.data.repository.RemoteConfigRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GroupViewModel : ViewModel() {

    private val groupRepository = GroupRepository()
    private val remoteConfigRepository = RemoteConfigRepository()

    private val _groups = MutableLiveData<List<StudyGroup>>()
    val groups: LiveData<List<StudyGroup>> = _groups

    private val _currentGroup = MutableLiveData<StudyGroup?>()
    val currentGroup: LiveData<StudyGroup?> = _currentGroup

    private val _announcements = MutableLiveData<List<Announcement>>()
    val announcements: LiveData<List<Announcement>> = _announcements

    private val _chatMessages = MutableLiveData<List<ChatMessage>>()
    val chatMessages: LiveData<List<ChatMessage>> = _chatMessages

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _createSuccess = MutableLiveData<String?>()
    val createSuccess: LiveData<String?> = _createSuccess

    private val _joinSuccess = MutableLiveData<Boolean>()
    val joinSuccess: LiveData<Boolean> = _joinSuccess

    private val _isGroupCreationEnabled = MutableLiveData<Boolean>(true)
    val isGroupCreationEnabled: LiveData<Boolean> = _isGroupCreationEnabled

    private val _isJoinEnabled = MutableLiveData<Boolean>(true)
    val isJoinEnabled: LiveData<Boolean> = _isJoinEnabled

    private val _isAnnouncementPostingEnabled = MutableLiveData<Boolean>(true)
    val isAnnouncementPostingEnabled: LiveData<Boolean> = _isAnnouncementPostingEnabled

    private val _maxMembers = MutableLiveData<Long>(20L)
    val maxMembers: LiveData<Long> = _maxMembers

    private val _announcementHeader = MutableLiveData<String>()
    val announcementHeader: LiveData<String> = _announcementHeader

    init {
        observeGroups()
    }

    private fun observeGroups() {
        viewModelScope.launch {
            groupRepository.getGroupsFlow().collectLatest { groupList ->
                _groups.value = groupList
            }
        }
    }

    fun observeChatMessages(groupId: String) {
        viewModelScope.launch {
            groupRepository.getChatMessagesFlow(groupId).collectLatest { messageList ->
                _chatMessages.value = messageList
            }
        }
    }

    fun loadRemoteConfig() {
        viewModelScope.launch {
            try {
                remoteConfigRepository.fetchAndActivate()
                _isGroupCreationEnabled.value = remoteConfigRepository.isGroupCreationEnabled()
                _isJoinEnabled.value = remoteConfigRepository.isJoinGroupEnabled()
                _isAnnouncementPostingEnabled.value = remoteConfigRepository.isAnnouncementPostingEnabled()
                _maxMembers.value = remoteConfigRepository.getMaxMembersPerGroup()
                _announcementHeader.value = remoteConfigRepository.getAnnouncementHeader()
            } catch (e: Exception) {
                _isGroupCreationEnabled.value = true
                _isJoinEnabled.value = true
                _isAnnouncementPostingEnabled.value = true
                _maxMembers.value = 20L
            }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _groups.value = groupRepository.getAllGroups()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadGroupDetail(groupId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _currentGroup.value = groupRepository.getGroupById(groupId)
                _announcements.value = groupRepository.getAnnouncements(groupId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun createGroup(group: StudyGroup) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val id = groupRepository.createGroup(group)
                _createSuccess.value = id
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun joinGroup(groupId: String, userId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                groupRepository.joinGroup(groupId, userId)
                _joinSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun postAnnouncement(groupId: String, announcement: Announcement) {
        viewModelScope.launch {
            try {
                groupRepository.postAnnouncement(groupId, announcement)
                loadGroupDetail(groupId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun sendChatMessage(groupId: String, message: ChatMessage) {
        viewModelScope.launch {
            try {
                groupRepository.sendChatMessage(groupId, message)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
