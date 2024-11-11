package com.familring.domain.model.gallery

import okhttp3.MultipartBody

data class PhotoList(
    val photos: List<MultipartBody.Part> = emptyList()
)
