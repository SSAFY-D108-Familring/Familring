package com.familring.domain.model.gallery

data class Album(
    val id: Long = 0,
    val albumName: String = "",
    val thumbnailUrl: String = "",
    val photoCount: Int = 0,
)
