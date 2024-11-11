package com.familring.presentation.screen.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.InterestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OtherInterestViewModel
    @Inject
    constructor(
        private val interestRepository: InterestRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(OtherInterestUiStatus())
        val uiState = _uiState.asStateFlow()

        private val _uiEvent = MutableSharedFlow<InterestUiEvent>()
        val uiEvent = _uiEvent

        init {
            getAnswers()
        }

        private fun getAnswers() {
            viewModelScope.launch {
                interestRepository.getAnswers().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    otherInterests = result.data,
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
    }
