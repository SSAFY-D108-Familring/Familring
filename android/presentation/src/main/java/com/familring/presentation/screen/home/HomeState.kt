package com.familring.presentation.screen.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.familring.domain.model.FamilyInfo
import com.familring.domain.model.User

@Stable
sealed interface HomeState {
    @Immutable
    data object Loading : HomeState

    @Immutable
    data class Success(
        val familyMembers: List<User> = listOf(),
        val familyInfo: FamilyInfo = FamilyInfo(),
    ) : HomeState

    @Immutable
    data class Error(
        val errorMessage: String,
    ) : HomeState
}
