package com.familring.presentation.screen.timecapsule

import com.familring.domain.model.Profile
import com.familring.domain.model.timecapsule.TimeCapsule

data class TimeCapsuleUiState(
    val loading: Boolean = false,
    val writingStatus: Int = 0,
    val leftDays: Int = 0,
    val timeCapsuleCount: Int = 0,
    val writers: List<Profile> = listOf(),
    val currentPageNo: Int = 0,
    val timeCapsules: List<TimeCapsule> = listOf(),
    val isReadTutorial: Boolean = true,
)
