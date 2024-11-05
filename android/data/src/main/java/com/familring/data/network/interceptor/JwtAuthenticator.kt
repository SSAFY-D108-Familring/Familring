package com.familring.data.network.interceptor

import com.familring.data.exception.ApiException
import com.familring.data.exception.RefreshTokenExpiredException
import com.familring.data.network.api.AuthApi
import com.familring.domain.datasource.TokenDataStore
import com.familring.domain.model.JwtToken
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject

class JwtAuthenticator
    @Inject
    constructor(
        private val tokenDataStore: TokenDataStore,
        private val authApi: AuthApi,
    ) : Authenticator {
        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            val request = response.request
            if (request.header("Authorization").isNullOrEmpty()) {
                return null
            }
            val refreshToken =
                runBlocking {
                    tokenDataStore.getRefreshToken()
                }
            return try {
                val newJwtToken: JwtToken? =
                    runBlocking {
                        refreshToken?.let {
                            val result = authApi.getNewToken(refreshToken)
                            result.data
                        }
                    }
                if (newJwtToken == null) return null
                runBlocking { tokenDataStore.saveJwtToken(newJwtToken) }

                request
                    .newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${newJwtToken.accessToken}")
                    .build()
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
