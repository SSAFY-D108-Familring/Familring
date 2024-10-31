package com.familring.data.datasourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.familring.domain.datasource.AuthDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDataSourceImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : AuthDataSource {
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
            }
        }

        companion object {
            val KAKAO_ID_KEY = stringPreferencesKey("kakao_id_key")
        }
    }
