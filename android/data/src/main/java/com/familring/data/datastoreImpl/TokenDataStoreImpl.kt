package com.familring.data.datastoreImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.familring.domain.datastore.TokenDataStore
import com.familring.domain.model.JwtToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataStoreImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : TokenDataStore {
        override suspend fun saveJwtToken(jwtToken: JwtToken) {
            dataStore.edit { prefs ->
                prefs[ACCESS_TOKEN_KEY] = jwtToken.accessToken
                prefs[REFRESH_TOKEN_KEY] = jwtToken.refreshToken
            }
        }

        override suspend fun getAccessToken(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[ACCESS_TOKEN_KEY]
                }.first()

        override suspend fun getRefreshToken(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[REFRESH_TOKEN_KEY]
                }.first()

        override suspend fun getJwtToken(): Pair<String, String> =
            dataStore.data.run {
                Pair(getAccessToken() ?: "", getRefreshToken() ?: "")
            }

        override suspend fun deleteJwtToken() {
            dataStore.edit { prefs ->
                prefs.remove(ACCESS_TOKEN_KEY)
                prefs.remove(REFRESH_TOKEN_KEY)
            }
        }

        companion object {
            val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
            val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")
        }
    }
