package com.familring.presentation.screen.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val familyRepository: FamilyRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
        val state = _state.asStateFlow()

        private val _event = MutableSharedFlow<MyPageUiEvent>()
        val event = _event.asSharedFlow()

        init {
            getMyInfo()
        }

        fun signOut() {
            viewModelScope.launch {
                userRepository.signOut().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _event.emit(MyPageUiEvent.SignOutSuccess)
                        }

                        is ApiResponse.Error -> {
                            _event.emit(MyPageUiEvent.Error(response.code, response.message))
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
                            _event.emit(MyPageUiEvent.EmotionUpdateSuccess)
                        }

                        is ApiResponse.Error -> {
                            _event.emit(MyPageUiEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }

        fun updateName(name: String) {
            viewModelScope.launch {
            }
        }

        private fun getMyInfo() {
            viewModelScope.launch {
                combine(
                    familyRepository.getFamilyCode(),
                    userRepository.getUser(),
                ) { familyResponse, userResponse ->
                    var currentState = MyPageUiState.Success()
                    currentState =
                        when (userResponse) {
                            is ApiResponse.Success -> {
                                val data = userResponse.data
                                currentState.copy(
                                    userNickname = data.userNickname,
                                    userColor = data.userColor,
                                    userRole = data.userRole,
                                    userBirthDate = data.userBirthDate.toString(),
                                    profileImage = data.userZodiacSign,
                                )
                            }

                            is ApiResponse.Error -> {
                                _event.emit(
                                    MyPageUiEvent.Error(
                                        userResponse.code,
                                        userResponse.message,
                                    ),
                                )
                                currentState
                            }
                        }
                    currentState =
                        when (familyResponse) {
                            is ApiResponse.Success -> {
                                currentState.copy(code = familyResponse.data)
                            }

                            is ApiResponse.Error -> {
                                _event.emit(
                                    MyPageUiEvent.Error(
                                        familyResponse.code,
                                        familyResponse.message,
                                    ),
                                )
                                currentState
                            }
                        }
                    currentState
                }.collectLatest { updateState ->
                    val state = _state.value
                    if (state is MyPageUiState.Loading) {
                        _state.value = updateState
                    } else if (state is MyPageUiState.Success) {
                        _state.value =
                            state.copy(
                                userNickname = updateState.userNickname,
                                userColor = updateState.userColor,
                                userRole = updateState.userRole,
                                userBirthDate = updateState.userBirthDate,
                                profileImage = updateState.profileImage,
                                code = updateState.code,
                            )
                    }
                }
            }
        }
    }
