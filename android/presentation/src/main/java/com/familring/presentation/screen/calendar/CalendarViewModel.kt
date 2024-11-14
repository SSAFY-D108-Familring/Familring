package com.familring.presentation.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datastore.TutorialDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.gallery.AlbumType
import com.familring.domain.repository.DailyRepository
import com.familring.domain.repository.GalleryRepository
import com.familring.domain.repository.ScheduleRepository
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
        private val scheduleRepository: ScheduleRepository,
        private val dailyRepository: DailyRepository,
        private val galleryRepository: GalleryRepository,
        private val tutorialDataStore: TutorialDataStore,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CalendarUiState())
        val uiState = _uiState.asStateFlow()

        private val _event = MutableSharedFlow<CalendarUiEvent>()
        val event = _event.asSharedFlow()

        init {
            getReadTutorial()
        }

        private fun getReadTutorial() {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        isReadTutorial = tutorialDataStore.getCalendarReadTutorial(),
                    )
                }
            }
        }

        fun setReadTutorial() {
            viewModelScope.launch {
                tutorialDataStore.setCalendarReadTutorial(true)
                _uiState.update {
                    it.copy(
                        isReadTutorial = true,
                    )
                }
            }
        }

        fun setReadTutorialState(isRead: Boolean) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        isReadTutorial = isRead,
                    )
                }
            }
        }

        fun getMonthData(
            year: Int,
            month: Int,
        ) {
            viewModelScope.launch {
                scheduleRepository.getMonthData(year, month).collect { result ->
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
                            _event.emit(
                                CalendarUiEvent.Error(
                                    result.code,
                                    result.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun getDaySchedules(scheduleIds: List<Long>) {
            if (scheduleIds.isEmpty()) {
                _uiState.update {
                    it.copy(
                        detailedSchedule = emptyList(),
                    )
                }
                return
            }
            viewModelScope.launch {
                scheduleRepository.getDaySchedules(scheduleIds).collect { result ->
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
                            _event.emit(
                                CalendarUiEvent.Error(
                                    result.code,
                                    result.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun deleteSchedule(id: Long) {
            viewModelScope.launch {
                scheduleRepository.deleteSchedule(id).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _event.emit(CalendarUiEvent.DeleteSuccess)
                            _uiState.update {
                                it.copy(
                                    detailedSchedule =
                                        it.detailedSchedule.filterNot { schedule ->
                                            schedule.scheduleId == id
                                        },
                                )
                            }
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                            _event.emit(
                                CalendarUiEvent.Error(
                                    result.code,
                                    result.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun getDayDailies(dailyIds: List<Long>) {
            if (dailyIds.isEmpty()) {
                _uiState.update {
                    it.copy(
                        detailedDailies = emptyList(),
                    )
                }
                return
            }
            viewModelScope.launch {
                dailyRepository.getDayDailies(dailyIds).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    detailedDailies = result.data,
                                )
                            }
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                            _event.emit(
                                CalendarUiEvent.Error(
                                    result.code,
                                    result.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun deleteDaily(id: Long) {
            viewModelScope.launch {
                dailyRepository.deleteDaily(id).collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _event.emit(CalendarUiEvent.DeleteSuccess)
                            _uiState.update {
                                it.copy(
                                    detailedDailies =
                                        it.detailedDailies.filterNot { daily ->
                                            daily.dailyId == id
                                        },
                                )
                            }
                        }

                        is ApiResponse.Error -> {
                            Timber.d("code: ${result.code}, message: ${result.message}")
                            _event.emit(
                                CalendarUiEvent.Error(
                                    result.code,
                                    result.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun createAlbum(
            scheduleId: Long,
            albumName: String,
        ) {
            viewModelScope.launch {
                galleryRepository
                    .createAlbum(scheduleId, albumName, AlbumType.SCHEDULE.name)
                    .collect { result ->
                        when (result) {
                            is ApiResponse.Success -> {
                                _event.emit(CalendarUiEvent.CreateAlbumSuccess)
                            }

                            is ApiResponse.Error -> {
                                Timber.d("code: ${result.code}, message: ${result.message}")
                                _event.emit(
                                    CalendarUiEvent.Error(
                                        result.code,
                                        result.message,
                                    ),
                                )
                            }
                        }
                    }
            }
        }
    }
