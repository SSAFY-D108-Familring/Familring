package com.familring.presentation.screen.calendar

import com.familring.domain.model.Profile

data class ScheduleUiState(
    val familyMembers: List<Profile> = emptyList(),
)
