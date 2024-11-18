package com.familring.presentation.screen.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datastore.TutorialDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.repository.InterestRepository
import com.familring.domain.request.InterestPeriodRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
        private val tutorialDataStore: TutorialDataStore,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(InterestUiState())
        val uiState = _uiState.asStateFlow()

        private val _uiEvent = MutableSharedFlow<InterestUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            getReadTutorial()
            getDataForInterestScreen()
        }

        private fun getReadTutorial() {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        isReadTutorial = tutorialDataStore.getInterestReadTutorial(),
                    )
                }
            }
        }

        fun setReadTutorial() {
            viewModelScope.launch {
                tutorialDataStore.setInterestReadTutorial(true)
            }
            _uiState.update {
                it.copy(
                    isReadTutorial = true,
                )
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
                                    getDataForShareDayScreen()
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
                            _uiEvent.emit(InterestUiEvent.CreateSuccess)
                            getAnswerStatus()
//                            _uiState.update {
//                                it.copy(
//                                    isWroteInterest = true,
//                                    myInterest = content,
//                                )
//                            }
                            getAnswersCount()
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
                            _uiEvent.emit(InterestUiEvent.EditSuccess)
                            getAnswerStatus()
//                            _uiState.update {
//                                it.copy(
//                                    isWroteInterest = true,
//                                    myInterest = content,
//                                )
//                            }
                            getAnswersCount()
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
                _uiState.update { it.copy(isWritingScreenLoading = true) }
                interestRepository.getAnswers().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            val count =
                                result.data.count { member ->
                                    member.interest.isNotEmpty() and member.interest.isNotBlank()
                                }
                            _uiState.update {
                                it.copy(
                                    isWritingScreenLoading = false,
                                    wroteFamilyCount = count,
                                )
                            }
                            if (count >= 2) {
                                _uiEvent.emit(
                                    InterestUiEvent.ShowDialog(count),
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

        private fun getSelectedInterest() {
            viewModelScope.launch {
                _uiState.update { it.copy(isResultScreenLoading = true) }
                interestRepository.getSelectedInterest().collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    isResultScreenLoading = false,
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

        private fun getDataForInterestScreen() {
            viewModelScope.launch {
                _uiState.update { it.copy(isInterestScreenLoading = true) }
                combine(
                    interestRepository.getInterestStatus(),
                    interestRepository.getAnswerStatus(),
                ) { statusResponse, answerResponse ->
                    var currentState = InterestUiState()
                    currentState =
                        when (statusResponse) {
                            is ApiResponse.Success -> {
                                when (statusResponse.data) {
                                    InterestState.WRITING -> {
                                        getAnswersCount()
                                    }

                                    InterestState.NO_PERIOD -> {
                                        getSelectedInterest()
                                    }

                                    InterestState.MISSION -> {
                                        getDataForShareDayScreen()
                                    }
                                }
                                currentState.copy(
                                    interestStatus = statusResponse.data,
                                )
                            }

                            is ApiResponse.Error -> {
                                currentState
                            }
                        }

                    currentState =
                        when (answerResponse) {
                            is ApiResponse.Success -> {
                                currentState.copy(
                                    isWroteInterest = answerResponse.data.isWroteInterest,
                                    myInterest = answerResponse.data.content,
                                    isFamilyWrote = answerResponse.data.isFamilyWrote,
                                )
                            }

                            is ApiResponse.Error -> {
                                currentState
                            }
                        }
                    currentState
                }.collect { updatedState ->
                    _uiState.update {
                        it.copy(
                            isInterestScreenLoading = false,
                            interestStatus = updatedState.interestStatus,
                            isWroteInterest = updatedState.isWroteInterest,
                            myInterest = updatedState.myInterest,
                            isFamilyWrote = updatedState.isFamilyWrote,
                        )
                    }
                }
            }
        }

        private fun getDataForShareDayScreen() {
            viewModelScope.launch {
                _uiState.update { it.copy(isShareScreenLoading = true) }
                combine(
                    interestRepository.checkUploadMission(),
                    interestRepository.getSelectedInterest(),
                    interestRepository.getMissionPeriod(),
                    interestRepository.getMissions(),
                ) { isUploadMissionResponse, selectedInterestResponse, leftMissionPeriodResponse, missionsResponse ->
                    var currentState = InterestUiState()
                    currentState =
                        when (isUploadMissionResponse) {
                            is ApiResponse.Success -> {
                                currentState.copy(
                                    isUploadMission = isUploadMissionResponse.data,
                                )
                            }

                            is ApiResponse.Error -> {
                                currentState
                            }
                        }
                    currentState =
                        when (selectedInterestResponse) {
                            is ApiResponse.Success -> {
                                currentState.copy(
                                    selectedInterest = selectedInterestResponse.data,
                                )
                            }

                            is ApiResponse.Error -> {
                                currentState
                            }
                        }
                    currentState =
                        when (leftMissionPeriodResponse) {
                            is ApiResponse.Success -> {
                                currentState.copy(
                                    leftMissionPeriod = leftMissionPeriodResponse.data,
                                )
                            }

                            is ApiResponse.Error -> {
                                currentState
                            }
                        }
                    currentState =
                        when (missionsResponse) {
                            is ApiResponse.Success -> {
                                currentState.copy(
                                    missions = missionsResponse.data,
                                )
                            }

                            is ApiResponse.Error -> {
                                currentState
                            }
                        }
                    currentState
                }.collect { updatedState ->
                    _uiState.update {
                        it.copy(
                            isShareScreenLoading = false,
                            isUploadMission = updatedState.isUploadMission,
                            selectedInterest = updatedState.selectedInterest,
                            leftMissionPeriod = updatedState.leftMissionPeriod,
                            missions = updatedState.missions,
                        )
                    }
                }
            }
        }
    }
