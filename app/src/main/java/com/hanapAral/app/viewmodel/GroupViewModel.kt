
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