package com.familring.presentation.screen.timecapsule

import com.familring.domain.model.TimeCapsule

sealed interface TimeCapsuleUiState {
    data object Loading : TimeCapsuleUiState

    data class NoTimeCapsule(
        val timeCapsule: TimeCapsule,
    ) : TimeCapsuleUiState

    data class WritingTimeCapsule(
        val timeCapsule: TimeCapsule,
    ) : TimeCapsuleUiState

    data class FinishedTimeCapsule(
        val timeCapsule: TimeCapsule,
    ) : TimeCapsuleUiState

    data class TimeCapsuleList(
        val timeCapsule: TimeCapsule,
    ) : TimeCapsuleUiState

    data class Error(
        val errorMessage: String,
    ) : TimeCapsuleUiState
}
