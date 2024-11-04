package com.familring.domain.model

data class FamilyInfo(
    val familyId: Long = 0,
    val familyCode: String = "",
    val familyCount: Int = 0,
    val familyCommunicationStatus: Int = 1,
)
