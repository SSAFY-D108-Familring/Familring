package com.familring.presentation.screen.interest

import com.familring.domain.model.interest.Mission
import com.familring.domain.model.interest.SelectedInterest

data class InterestListUiState(
    val selectedInterests: List<SelectedInterest> = listOf(),
    val detailInterests: List<Mission> = listOf()
)
