package com.familring.domain.datasource

interface AuthDataSource {
    suspend fun saveKakaoId(kakaoId: String)

    suspend fun getKakaoId(): String?

    suspend fun deleteAuthData()
}