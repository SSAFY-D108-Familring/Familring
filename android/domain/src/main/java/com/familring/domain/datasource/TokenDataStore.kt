package com.familring.domain.datasource

import com.familring.domain.model.JwtToken

interface TokenDataStore {
    suspend fun saveJwtToken(jwtToken: JwtToken)

    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun getJwtToken(): Pair<String, String>

    suspend fun deleteJwtToken()
}
