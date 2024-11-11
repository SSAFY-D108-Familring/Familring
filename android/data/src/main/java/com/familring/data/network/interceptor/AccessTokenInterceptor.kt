package com.familring.data.network.interceptor

import com.familring.domain.datastore.TokenDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor
    @Inject
    constructor(
        private val tokenDataStore: TokenDataStore,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()
            val accessToken: String =
                runBlocking {
                    val token: String =
                        tokenDataStore.getAccessToken() ?: run {
                            ""
                        }
                    token
                }
            val request =
                requestBuilder
                    .header(
                        "Authorization",
                        "Bearer $accessToken",
                    ).build()
            return chain.proceed(request)
        }
    }
