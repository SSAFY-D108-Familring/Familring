package com.familring.presentation.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datasource.AuthDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.FaceRepository
import com.familring.domain.repository.FamilyRepository
import com.familring.domain.repository.UserRepository
import com.familring.domain.request.UserJoinRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val familyRepository: FamilyRepository,
        private val faceRepository: FaceRepository,
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

        fun updateBirthDate(birthDate: LocalDate) {
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

        fun updateCode(code: String) {
            _state.update { it.copy(familyCode = code) }
        }

        fun updateMake(make: Boolean) {
            _state.update { it.copy(make = make) }
        }

        fun updateLunar(lunar: Boolean) {
            _state.update { it.copy(userIsLunar = lunar) }
        }

        fun join() {
            val request =
                UserJoinRequest(
                    userKakaoId = state.value.userKakaoId,
                    userNickname = state.value.userNickname,
                    userBirthDate = state.value.userBirthDate,
                    userRole = state.value.userRole,
                    userColor = state.value.userColor,
                    userIsLunar = state.value.userIsLunar,
                )
            val file = state.value.userFace!!

            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                userRepository.join(request, file).collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _event.emit(SignUpUiEvent.Success)
                        }

                        is ApiResponse.Error -> {
                            _state.update { it.copy(isLoading = false) }
                            _event.emit(
                                SignUpUiEvent.Error(
                                    response.code,
                                    "회원가입 오류: " + response.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun makeFamily() {
            viewModelScope.launch {
                familyRepository.makeFamily().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _state.update {
                                it.copy(
                                    familyCode = response.data.familyCode,
                                    isLoading = false,
                                )
                            }
                            _event.emit(SignUpUiEvent.MakeSuccess)
                        }

                        is ApiResponse.Error -> {
                            _state.update { it.copy(isLoading = false) }
                            _event.emit(SignUpUiEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }

        fun joinFamily(code: String) {
            viewModelScope.launch {
                familyRepository.joinFamily(code).collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            _event.emit(SignUpUiEvent.JoinSuccess)
                        }

                        is ApiResponse.Error -> {
                            _state.update { it.copy(isLoading = false) }
                            _event.emit(
                                SignUpUiEvent.Error(
                                    response.code,
                                    "가족 가입 오류: " + response.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun isAvailableCode(code: String) {
            viewModelScope.launch {
                familyRepository.isAvailableCode(code).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            if (response.data) {
                                _event.emit(SignUpUiEvent.Available)
                            } else {
                                _event.emit(SignUpUiEvent.NotAvailable)
                            }
                        }

                        is ApiResponse.Error -> {
                            _event.emit(SignUpUiEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }

        fun getParentAvailable(code: String) {
            viewModelScope.launch {
                familyRepository.getParentAvailable(code).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _state.update { it.copy(parents = response.data) }
                        }

                        is ApiResponse.Error -> {
                            _event.emit(SignUpUiEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }

        fun getFaceCount(face: File) {
            _state.update { it.copy(isLoading = true) }
            viewModelScope.launch {
                faceRepository.getFaceCount(face).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _state.update { it.copy(isLoading = false) }
                            if (response.data.faceCount == 1) {
                                _event.emit(SignUpUiEvent.FaceSuccess)
                            } else {
                                _event.emit(SignUpUiEvent.FaceFail)
                            }
                        }

                        is ApiResponse.Error -> {
                            _state.update { it.copy(isLoading = false) }
                            _event.emit(SignUpUiEvent.Error(response.code, response.message))
                        }
                    }
                }
            }
        }
    }
