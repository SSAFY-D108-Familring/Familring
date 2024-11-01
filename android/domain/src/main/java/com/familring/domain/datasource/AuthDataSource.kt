package com.familring.domain.datasource

import java.util.Date

interface AuthDataSource {
    suspend fun saveKakaoId(kakaoId: String)

    suspend fun getKakaoId(): String?

    suspend fun deleteAuthData()

    suspend fun saveNickname(nickname: String): Unit
    suspend fun getNickname(): String?

    suspend fun saveBirthDate(birthDateString: Date): Unit
    suspend fun getBirthDate(): String?

    suspend fun saveZodiacSign(zodiacSign: String): Unit
    suspend fun getZodiacSign(): String?

    suspend fun saveRole(role: String): Unit
    suspend fun getRole(): String?

    suspend fun saveFace(face: String): Unit
    suspend fun getFace(): String?

    suspend fun saveColor(color: String): Unit
    suspend fun getColor(): String?

    suspend fun saveEmotion(emotion: String): Unit
    suspend fun getEmotion(): String?
}