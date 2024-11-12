package com.familring.presentation.screen.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.InterestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InterestViewModel
    @Inject
    constructor(
        private val interestRepository: InterestRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(InterestUiState())
        val uiState = _uiState.asStateFlow()

        private val _uiEvent = MutableSharedFlow<InterestUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            getInterestStatus()
            getAnswerStatus()
        }

        fun getInterestStatus() {
            viewModelScope.launch {
                interestRepository.getInterestStatus().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    interestStatus = result.data,
                                )
                            }
                        }

                        is ApiResponse.Error -> {
                            _uiEvent.emit(InterestUiEvent.Error(result.code, result.message))
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        fun getAnswerStatus() {
            viewModelScope.launch {
                interestRepository.getAnswerStatus().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    isWroteInterest = result.data.isWroteInterest,
                                    interest = result.data.content,
                                    isFamilyWrote = result.data.isFamilyWrote,
                                )
                            }
                        }

                        is ApiResponse.Error -> {
                            _uiEvent.emit(InterestUiEvent.Error(result.code, result.message))
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        fun createAnswer(content: String) {
            viewModelScope.launch {
                interestRepository.createAnswer(content).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            getAnswerStatus()
                        }

                        is ApiResponse.Error -> {
                            _uiEvent.emit(InterestUiEvent.Error(result.code, result.message))
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        fun updateAnswer(content: String) {
            viewModelScope.launch {
                interestRepository.updateAnswer(content).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            getAnswerStatus()
                        }

                        is ApiResponse.Error -> {
                            _uiEvent.emit(InterestUiEvent.Error(result.code, result.message))
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }
    }
