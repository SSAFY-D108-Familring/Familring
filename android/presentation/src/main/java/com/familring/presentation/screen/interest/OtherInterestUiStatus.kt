package com.familring.presentation.screen.interest

import com.familring.domain.model.interest.InterestCard

data class OtherInterestUiStatus(
    val otherInterests: List<InterestCard> = emptyList(),
)
