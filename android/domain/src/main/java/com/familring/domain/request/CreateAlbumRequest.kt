package com.familring.domain.request

data class CreateAlbumRequest(
    val scheduleId: Long? = null,
    val albumName: String,
    val albumType: String,
)
