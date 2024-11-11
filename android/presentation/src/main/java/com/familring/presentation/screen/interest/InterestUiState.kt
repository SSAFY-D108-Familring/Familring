package com.familring.presentation.screen.interest

data class InterestUiState(
    val interestStatus: Int = 0,
    val isWroteInterest: Boolean = true,
    val interest: String = "",
    val isUploadMission: Boolean = false,
    val isFamilyWrote: Boolean = false,
    val wroteFamilyCount: Int = 0,
    val selectedInterest: String = "",
)
