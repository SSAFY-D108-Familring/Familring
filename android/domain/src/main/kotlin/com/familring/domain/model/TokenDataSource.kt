package com.familring.domain.model

interface TokenDataSource {
    suspend fun saveJwtToken(jwtToken: JwtToken): Unit
}