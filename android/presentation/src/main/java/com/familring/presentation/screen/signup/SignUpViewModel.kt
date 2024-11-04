package com.familring.presentation.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datasource.AuthDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.FamilyRepository
import com.familring.domain.repository.UserRepository
import com.familring.domain.request.UserJoinRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val familyRepository: FamilyRepository,
        private val authDataStore: AuthDataStore,
    ) : ViewModel() {
        private val _state = MutableStateFlow(SignUpUiState())
        val state = _state.asStateFlow()

        private val _event = MutableSharedFlow<SignUpUiEvent>()
        val event = _event.asSharedFlow()

        init {
            updateKakaoId()
        }

        private fun updateKakaoId() {
            viewModelScope.launch {
                authDataStore.getKakaoId()?.let { kakaoId ->
                    _state.update { it.copy(userKakaoId = kakaoId) }
                }
            }
        }

        fun updateNickname(nickname: String) {
            _state.update { it.copy(userNickname = nickname) }
        }

        fun updateBirthDate(birthDate: String) {
            _state.update { it.copy(userBirthDate = birthDate) }
        }

        fun updateColor(color: String) {
            _state.update { it.copy(userColor = color) }
        }

        fun updateRole(role: String) {
            _state.update { it.copy(userRole = role) }
        }

        fun updateFace(face: File) {
            _state.update { it.copy(userFace = face) }
        }

        fun updateMakeThenNavigate(
            make: Boolean,
            code: String = "",
            onNavigate: () -> Unit,
        ) {
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        make = make,
                        familyCode = code,
                    )
                }
                // 상태 업데이트가 완료될 때까지 대기
                state.first {
                    it.make == make &&
                        (code.isEmpty() || it.familyCode == code)
                }
                onNavigate()
            }
        }

        fun join() {
            val request =
                UserJoinRequest(
                    userKakaoId = state.value.userKakaoId,
                    userNickname = state.value.userNickname,
                    userBirthDate = state.value.userBirthDate,
                    userRole = state.value.userRole,
                    userColor = state.value.userColor,
                )
            val file = state.value.userFace!!

            viewModelScope.launch {
                userRepository.join(request, file).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _event.emit(SignUpUiEvent.Success)
                        }

                        is ApiResponse.Error -> {
                            _event.emit(SignUpUiEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }

        fun makeFamily() {
            viewModelScope.launch {
                _event.emit(SignUpUiEvent.Loading)
                familyRepository.makeFamily().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _state.update { it.copy(familyCode = response.data.familyCode) }
                            _event.emit(SignUpUiEvent.MakeSuccess)
                        }

                        is ApiResponse.Error -> {
                            _event.emit(SignUpUiEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }

        fun joinFamily(code: String) {
            viewModelScope.launch {
                familyRepository.joinFamily(code).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _event.emit(SignUpUiEvent.Success)
                        }

                        is ApiResponse.Error -> {
                            _event.emit(SignUpUiEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }
    }
