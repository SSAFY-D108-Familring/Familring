package com.familring.presentation.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.CalendarRepository
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
class CalendarViewModel
    @Inject
    constructor(
        private val calendarRepository: CalendarRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CalendarUiState())
        val uiState = _uiState.asStateFlow()

        private val _event = MutableSharedFlow<CalendarUiEvent>()
        val event = _event.asSharedFlow()

        fun getMonthData(
            year: Int,
            month: Int,
        ) {
            viewModelScope.launch {
                calendarRepository.getMonthData(year, month).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    previewSchedules = result.data.previewSchedules,
                                    previewDailies = result.data.previewDailies,
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

        fun getDaySchedules(scheduleIds: List<Long>) {
            viewModelScope.launch {
                calendarRepository.getDaySchedules(scheduleIds).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    detailedSchedule = result.data,
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

        fun deleteSchedule(id: Long) {
            viewModelScope.launch {
                calendarRepository.deleteSchedule(id).collect { result ->
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
