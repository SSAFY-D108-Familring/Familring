package com.familring.domain.model.gallery

data class OneAlbumResponse(
    val albumName: String = "",
    val photos: List<Photo> = emptyList(),
)
