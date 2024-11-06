package com.familring.presentation.screen.calendar

import com.familring.domain.model.Profile
import com.familring.domain.model.User

data class ScheduleUiState(
    val isLoading: Boolean = true,
    val familyProfiles: List<Profile> = listOf(),
    val familyMembers: List<User> = listOf(),
)
