package com.familring.presentation.screen.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.InterestRepository
import com.familring.domain.request.InterestPeriodRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber
import java.time.LocalDate
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

        private fun getInterestStatus() {
            viewModelScope.launch {
                interestRepository.getInterestStatus().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    interestStatus = result.data,
                                )
                            }
                            when (result.data) {
                                InterestState.WRITING -> {
                                    getAnswersCount()
                                }

                                InterestState.NO_PERIOD -> {
                                    getSelectedInterest()
                                }

                                InterestState.MISSION -> {
                                    checkUploadMission()
                                    getSelectedInterest()
                                    getLeftMissionPeriod()
                                    getMissions()
                                }
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

        private fun getAnswerStatus() {
            viewModelScope.launch {
                interestRepository.getAnswerStatus().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    isWroteInterest = result.data.isWroteInterest,
                                    myInterest = result.data.content,
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

        private fun getAnswersCount() {
            viewModelScope.launch {
                interestRepository.getAnswers().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    wroteFamilyCount =
                                        result.data.count { member ->
                                            member.interest.isNotEmpty() and member.interest.isNotBlank()
                                        },
                                )
                            }
                        }

                        is ApiResponse.Error -> {
                            if (result.code == "I0001") {
                                _uiState.update {
                                    it.copy(
                                        wroteFamilyCount = 0,
                                    )
                                }
                            } else {
                                Timber.d("code: ${result.code}, message: ${result.message}")
                                _uiEvent.emit(InterestUiEvent.Error(result.code, result.message))
                            }
                        }
                    }
                }
            }
        }

        fun selectInterest() {
            viewModelScope.launch {
                interestRepository.selectInterest().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            //
                        }

                        is ApiResponse.Error -> {
                            _uiEvent.emit(InterestUiEvent.Error(result.code, result.message))
                            Timber.d("code: ${result.code}, message: ${result.message}")
                        }
                    }
                }
            }
        }

        private fun getSelectedInterest() {
            viewModelScope.launch {
                interestRepository.getSelectedInterest().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    selectedInterest = result.data,
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

        fun setMissionPeriod(endDate: LocalDate) {
            viewModelScope.launch {
                interestRepository
                    .setMissionPeriod(InterestPeriodRequest(endDate))
                    .collect { result ->
                        when (result) {
                            is ApiResponse.Success -> {
                                getInterestStatus()
                            }

                            is ApiResponse.Error -> {
                                _uiEvent.emit(InterestUiEvent.Error(result.code, result.message))
                                Timber.d("code: ${result.code}, message: ${result.message}")
                            }
                        }
                    }
            }
        }

        private fun getLeftMissionPeriod() {
            viewModelScope.launch {
                interestRepository.getMissionPeriod().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    leftMissionPeriod = result.data,
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

        fun postMission(imageMultiPart: MultipartBody.Part?) {
            viewModelScope.launch {
                interestRepository
                    .uploadMission(imageMultiPart)
                    .collect { result ->
                        when (result) {
                            is ApiResponse.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isUploadMission = true,
                                    )
                                }
                                getMissions()
                            }

                            is ApiResponse.Error -> {
                                _uiEvent.emit(InterestUiEvent.Error(result.code, result.message))
                                Timber.d("code: ${result.code}, message: ${result.message}")
                            }
                        }
                    }
            }
        }

        private fun getMissions() {
            viewModelScope.launch {
                interestRepository.getMissions().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    missions = result.data,
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

        private fun checkUploadMission() {
            viewModelScope.launch {
                interestRepository.checkUploadMission().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    isUploadMission = result.data,
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
