package com.familring.data.network.interceptor

import android.util.Log
import com.familring.data.exception.ApiException
import com.familring.data.exception.RefreshTokenExpiredException
import com.familring.domain.model.ErrorResponse
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorHandlingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            if (response.isSuccessful) return response
            val errorBody = response.body?.string() ?: return response

            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)

            errorResponse?.let {
                if (it.errorCode == "T0004") {
                    throw RefreshTokenExpiredException(it)
                }
            }
            throw ApiException(error = errorResponse)
        } catch (e: Throwable) {
            when (e) {
                is ApiException -> throw e
                is RefreshTokenExpiredException -> throw e
                is IOException -> throw IOException(e)
                else -> throw e
            }
        }
    }
}
