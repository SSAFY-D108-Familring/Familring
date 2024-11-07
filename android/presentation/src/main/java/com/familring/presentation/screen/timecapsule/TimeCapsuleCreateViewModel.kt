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

@HiltViewModel
class TimeCapsuleCreateViewModel
    @Inject
    constructor(
        private val timeCapsuleRepository: TimeCapsuleRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(TimeCapsuleCreateUiState())
        val uiState = _uiState.asStateFlow()

        private val _event = MutableSharedFlow<TimeCapsuleCreateUiEvent>()
        val event = _event.asSharedFlow()

        fun createTimeCapsule(openDate: String) {
            _uiState.update { it.copy(loading = true) }
            viewModelScope.launch {
                timeCapsuleRepository.createTimeCapsule(openDate).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update { it.copy(loading = false) }
                            _event.emit(TimeCapsuleCreateUiEvent.Success)
                        }

                        is ApiResponse.Error -> {
                            _uiState.update { it.copy(loading = false) }
                            _event.emit(TimeCapsuleCreateUiEvent.Error(result.code, result.message))
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }
    }
