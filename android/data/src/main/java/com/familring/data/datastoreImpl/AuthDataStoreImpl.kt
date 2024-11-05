package com.familring.data.datastoreImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.familring.domain.datasource.AuthDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class AuthDataStoreImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : AuthDataStore {
        override suspend fun saveKakaoId(kakaoId: String) {
            dataStore.edit { prefs ->
                prefs[KAKAO_ID_KEY] = kakaoId
            }
        }

        override suspend fun getKakaoId(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[KAKAO_ID_KEY]
                }.first()

        override suspend fun deleteAuthData() {
            dataStore.edit { prefs ->
                prefs.remove(KAKAO_ID_KEY)
                prefs.remove(NICKNAME_KEY)
                prefs.remove(BIRTH_DATE_KEY)
                prefs.remove(ZODIAC_SIGN_KEY)
                prefs.remove(ROLE_KEY)
                prefs.remove(FACE_KEY)
                prefs.remove(COLOR_KEY)
                prefs.remove(EMOTION_KEY)
            }
        }

        override suspend fun saveNickname(nickname: String) {
            dataStore.edit { prefs ->
                prefs[NICKNAME_KEY] = nickname
            }
        }

        override suspend fun getNickname(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[NICKNAME_KEY]
                }.first()

        override suspend fun saveBirthDate(birthDateString: Date) {
            dataStore.edit { prefs ->
                prefs[BIRTH_DATE_KEY] = birthDateString.toString()
            }
        }

        override suspend fun getBirthDate(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[BIRTH_DATE_KEY]
                }.first()

        override suspend fun saveZodiacSign(zodiacSign: String) {
            dataStore.edit { prefs ->
                prefs[ZODIAC_SIGN_KEY] = zodiacSign
            }
        }

        override suspend fun getZodiacSign(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[ZODIAC_SIGN_KEY]
                }.first()

        override suspend fun saveRole(role: String) {
            dataStore.edit { prefs ->
                prefs[ROLE_KEY] = role
            }
        }

        override suspend fun getRole(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[ROLE_KEY]
                }.first()

        override suspend fun saveFace(face: String) {
            dataStore.edit { prefs ->
                prefs[FACE_KEY] = face
            }
        }

        override suspend fun getFace(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[FACE_KEY]
                }.first()

        override suspend fun saveColor(color: String) {
            dataStore.edit { prefs ->
                prefs[COLOR_KEY] = color
            }
        }

        override suspend fun getColor(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[COLOR_KEY]
                }.first()

        override suspend fun saveEmotion(emotion: String) {
            dataStore.edit { prefs ->
                prefs[EMOTION_KEY] = emotion
            }
        }

        override suspend fun getEmotion(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[EMOTION_KEY]
                }.first()

        companion object {
            val KAKAO_ID_KEY = stringPreferencesKey("kakao_id_key")
            val NICKNAME_KEY = stringPreferencesKey("nickname_key")
            val BIRTH_DATE_KEY = stringPreferencesKey("birth_date_key")
            val ZODIAC_SIGN_KEY = stringPreferencesKey("zodiac_sign_key")
            val ROLE_KEY = stringPreferencesKey("role_key")
            val FACE_KEY = stringPreferencesKey("face_key")
            val COLOR_KEY = stringPreferencesKey("color_key")
            val EMOTION_KEY = stringPreferencesKey("emotion_key")
        }
    }
