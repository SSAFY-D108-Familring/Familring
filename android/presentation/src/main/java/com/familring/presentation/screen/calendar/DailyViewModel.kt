package com.familring.presentation.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.DailyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DailyViewModel
    @Inject
    constructor(
        private val dailyRepository: DailyRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(DailyUiState())
        val uiState = _uiState.asStateFlow()

        private val _event = MutableSharedFlow<DailyUiEvent>()
        val event = _event.asSharedFlow()

        fun createDaily(
            content: String,
            image: MultipartBody.Part?,
        ) {
            viewModelScope.launch {
                dailyRepository.createDaily(content, image).collect { result ->

                    when (result) {
                        is ApiResponse.Success -> {
                            _event.emit(DailyUiEvent.Success)
                        }

                        is ApiResponse.Error -> {
                            _event.emit(DailyUiEvent.Error(result.code, result.message))
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        fun updateDaily(
            dailyId: Long,
            content: String,
            image: MultipartBody.Part?,
        ) {
            viewModelScope.launch {
                dailyRepository.modifyDaily(dailyId, content, image).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _event.emit(DailyUiEvent.Success)
                        }

                        is ApiResponse.Error -> {
                            _event.emit(DailyUiEvent.Error(result.code, result.message))
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }
    }
