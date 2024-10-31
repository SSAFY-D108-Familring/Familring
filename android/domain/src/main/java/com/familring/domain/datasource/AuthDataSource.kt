package com.familring.domain.datasource

interface AuthDataSource {
    suspend fun saveKakaoId(kakaoID: String)

    suspend fun getKakaoId(): String?

    suspend fun deleteAuthData()
}
