package com.familring.presentation.screen.interest

import com.familring.domain.model.interest.Mission
import com.familring.domain.model.interest.SelectedInterest

data class InterestUiState(
    val isInterestScreenLoading: Boolean = true,
    val interestStatus: Int = 0,
    val isWroteInterest: Boolean = true,
    val myInterest: String = "",
    val isFamilyWrote: Boolean = false,
    val wroteFamilyCount: Int = 0,
    val isUploadMission: Boolean = false,
    val selectedInterest: SelectedInterest = SelectedInterest(),
    val leftMissionPeriod: Int = 0,
    val missions: List<Mission> = listOf(),
    val isShareScreenLoading: Boolean = true,
)
