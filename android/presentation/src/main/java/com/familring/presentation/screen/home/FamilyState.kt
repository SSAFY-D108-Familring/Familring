package com.familring.presentation.screen.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.familring.domain.model.FamilyInfo

@Stable
interface FamilyState {
    @Immutable
    data object Loading : FamilyState

    @Immutable
    data class Success(
        val familyMembers: FamilyInfo,
    ) : FamilyState

    @Immutable
    data class Error(
        val errorMessage: String,
    ) : FamilyState
}
