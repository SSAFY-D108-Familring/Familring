package com.familring.domain.datasource

interface AuthDataStore {
    suspend fun saveKakaoId(kakaoId: String)

    suspend fun getKakaoId(): String?

    suspend fun deleteAuthData()
}