package com.familring.presentation.screen.timecapsule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.TimeCapsuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        private val _uiState = MutableStateFlow(TimeCapsuleUiState())
        val uiState = _uiState.asStateFlow()

        private val _event = MutableSharedFlow<TimeCapsuleUiEvent>()
        val event = _event.asSharedFlow()

        fun getTimeCapsuleStatus() {
            viewModelScope.launch {
                timeCapsuleRepository.getTimeCapsuleStatus().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    writingStatus = result.data.status,
                                    leftDays = result.data.leftDays ?: 0,
                                    timeCapsuleCount = result.data.timeCapsuleCount ?: 0,
                                    writers = result.data.writers ?: listOf(),
                                )
                            }
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        fun createTimeCapsule(openDate: String) {
            viewModelScope.launch {
                timeCapsuleRepository.createTimeCapsule(openDate).collect { result ->
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

        fun createTimeCapsuleAnswer(content: String) {
            viewModelScope.launch {
                timeCapsuleRepository.createTimeCapsuleAnswer(content).collect { result ->
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
                            _uiState.update {
                                it.copy(
                                    timeCapsules = result.data,
                                )
                            }
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }
    }
