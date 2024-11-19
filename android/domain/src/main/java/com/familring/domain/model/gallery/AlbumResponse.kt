package com.familring.domain.model.gallery

data class AlbumResponse(
    val NORMAL: List<Album>? = null,
    val PERSON: List<Album>? = null,
    val SCHEDULE: List<Album>? = null,
)
