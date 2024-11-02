package com.familring.presentation.screen.timecapsule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.TimeCapsule
import com.familring.domain.repository.TimeCapsuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "TimeCapsuleViewModel"

@HiltViewModel
class TimeCapsuleViewModel
    @Inject
    constructor(
        private val timeCapsuleRepository: TimeCapsuleRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<TimeCapsuleUiState>(TimeCapsuleUiState.Loading)
        val uiState = _uiState.asStateFlow()

        private val _event = MutableSharedFlow<TimeCapsuleUiEvent>()
        val event = _event.asSharedFlow()

        fun getTimeCapsuleStatus() {
            viewModelScope.launch {
                timeCapsuleRepository.getTimeCapsuleStatus().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            // 상태에 따라 분기 처리
//                            _uiState.value = TimeCapsuleUiState.NoTimeCapsule()
//                            _uiState.value = TimeCapsuleUiState.FinishedTimeCapsule()
//                            _uiState.value = TimeCapsuleUiState.WritingTimeCapsule()
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
//                            _uiState.value = TimeCapsuleUiState.Error(result.message)
                        }
                    }
                }
            }
        }

        fun createTimeCapsule(timeCapsule: TimeCapsule) {
            viewModelScope.launch {
                timeCapsuleRepository.createTimeCapsule(timeCapsule).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        fun createTimeCapsuleAnswer() {
            viewModelScope.launch {
                timeCapsuleRepository.createTimeCapsuleAnswer().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        fun getTimeCapsuleWriters() {
            viewModelScope.launch {
                timeCapsuleRepository.getTimeCapsuleWriters().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        fun getTimeCapsules() {
            viewModelScope.launch {
                timeCapsuleRepository.getTimeCapsules().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        fun getTimeCapsuleAnswers() {
            viewModelScope.launch {
                timeCapsuleRepository.getTimeCapsuleAnswers().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }
    }
