package com.familring.domain.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun String.toRequestBody() = this.toRequestBody("application/json".toMediaType())

fun File?.toMultiPart(filename: String) =
    run {
        this?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(filename, it.name, requestFile)
        } ?: run {
            null
        }
    }

fun File?.toVoiceMultiPart(filename: String) =
    run {
        this?.let {
            val requestFile = it.asRequestBody("audio/mp4".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(filename, it.name, requestFile)
        } ?: run {
            null
        }
    }
