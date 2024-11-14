package com.familring.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datastore.AuthDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.FamilyRepository
import com.familring.domain.repository.UserRepository
import com.familring.domain.request.UserEmotionRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val familyRepository: FamilyRepository,
        private val userRepository: UserRepository,
        private val authDataStore: AuthDataStore,
    ) : ViewModel() {
        private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
        val homeState = _homeState.asStateFlow()

        private val _homeEvent = MutableSharedFlow<HomeEvent>()
        val homeEvent = _homeEvent.asSharedFlow()

        private val _myUserId = MutableStateFlow<Long?>(null)
        val myUserId = _myUserId.asStateFlow()

        private val refreshTrigger = MutableStateFlow(0) // 실시간으로 받아오기 위해 필요하고

        init {
            viewModelScope.launch {
                refreshTrigger.collectLatest {
                    getFamilyMembers()
                }
            }
        }

        fun refresh() {
            refreshTrigger.value += 1 // 트리거가 필요함
        }

        private fun getFamilyMembers() {
            viewModelScope.launch {
                combine(
                    familyRepository.getFamilyMembers(),
                    familyRepository.getFamilyInfo(),
                ) { memberResponse, infoResponse ->
                    var currentState = HomeState.Success()
                    currentState =
                        when (memberResponse) {
                            is ApiResponse.Success -> {
                                currentState.copy(familyMembers = memberResponse.data)
                            }

                            is ApiResponse.Error -> {
                                currentState
                            }
                        }
                    currentState =
                        when (infoResponse) {
                            is ApiResponse.Success -> {
                                currentState.copy(familyInfo = infoResponse.data)
                            }

                            is ApiResponse.Error -> {
                                currentState
                            }
                        }
                    currentState
                }.collectLatest { updateState ->
                    val state = _homeState.value
                    if (state is HomeState.Loading) {
                        _homeState.value = updateState
                    } else if (state is HomeState.Success) {
                        _homeState.value =
                            state.copy(
                                familyMembers = updateState.familyMembers,
                                familyInfo = updateState.familyInfo,
                            )
                    }
                }
            }
        }

        fun getUserId() {
            viewModelScope.launch {
                authDataStore.getUserId()?.let {
                    _myUserId.value = it
                }
            }
        }

        fun sendMentionNotification(
            receiverId: Long,
            mention: String,
        ) {
            viewModelScope.launch {
                Timber.d("알림 전송 시도: receiverId=$receiverId, mention=$mention")
                userRepository
                    .sendMentionNotification(receiverId, mention)
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                Timber.d("알림 전송 성공 $receiverId, $mention")
                                _homeEvent.emit(HomeEvent.Success)
                            }

                            is ApiResponse.Error -> {
                                _homeEvent.emit(
                                    HomeEvent.Error(
                                        code = response.code,
                                        message = response.message,
                                    ),
                                )
                            }
                        }
                    }
            }
        }

        fun updateEmotion(emotion: UserEmotionRequest) {
            viewModelScope.launch {
                userRepository.updateEmotion(emotion).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _homeEvent.emit(HomeEvent.UpdateSuccess)
                            getFamilyMembers()
                        }

                        is ApiResponse.Error -> {
                            _homeEvent.emit(HomeEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }
    }
