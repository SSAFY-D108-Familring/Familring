package com.familring.domain.datastore

import java.time.LocalDate

interface AuthDataStore {
    suspend fun saveKakaoId(kakaoId: String)

    suspend fun getKakaoId(): String?

    suspend fun deleteAuthData()

    suspend fun saveNickname(nickname: String)

    suspend fun getNickname(): String?

    suspend fun saveBirthDate(birthDateString: LocalDate)

    suspend fun getBirthDate(): String?

    suspend fun saveZodiacSign(zodiacSign: String)

    suspend fun getZodiacSign(): String?

    suspend fun saveRole(role: String)

    suspend fun getRole(): String?

    suspend fun saveFace(face: String): Unit

    suspend fun getFace(): String?

    suspend fun saveColor(color: String)

    suspend fun getColor(): String?

    suspend fun saveEmotion(emotion: String)

    suspend fun getEmotion(): String?

    suspend fun saveUserId(userId: Long)

    suspend fun getUserId(): Long?

    suspend fun saveFamilyId(familyId: Long)

    suspend fun getFamilyId(): Long?
}
