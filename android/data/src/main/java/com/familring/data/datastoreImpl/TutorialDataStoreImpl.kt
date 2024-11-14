package com.familring.data.datastoreImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.familring.domain.datastore.TutorialDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TutorialDataStoreImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : TutorialDataStore {
        override suspend fun setTimeCapsuleReadTutorial(isRead: Boolean) {
            dataStore.edit { prefs ->
                prefs[TUTORIAL_TIME_CAPSULE_KEY] = isRead
            }
        }

        override suspend fun getTimeCapsuleReadTutorial(): Boolean =
            dataStore.data
                .map { prefs ->
                    prefs[TUTORIAL_TIME_CAPSULE_KEY]
                }.first() ?: false

        override suspend fun setInterestReadTutorial(isRead: Boolean) {
            dataStore.edit { prefs ->
                prefs[TUTORIAL_INTEREST_KEY] = isRead
            }
        }

        override suspend fun getInterestReadTutorial(): Boolean =
            dataStore.data
                .map { prefs ->
                    prefs[TUTORIAL_INTEREST_KEY]
                }.first() ?: false

        override suspend fun setChatReadTutorial(isRead: Boolean) {
            dataStore.edit { prefs ->
                prefs[TUTORIAL_CHAT_KEY] = isRead
            }
        }

        override suspend fun getChatReadTutorial(): Boolean =
            dataStore.data
                .map { prefs ->
                    prefs[TUTORIAL_CHAT_KEY]
                }.first() ?: false

        override suspend fun setQuestionReadTutorial(isRead: Boolean) {
            dataStore.edit { prefs ->
                prefs[TUTORIAL_QUESTION_KEY] = isRead
            }
        }

        override suspend fun getQuestionReadTutorial(): Boolean =
            dataStore.data
                .map { prefs ->
                    prefs[TUTORIAL_QUESTION_KEY]
                }.first() ?: false

        override suspend fun setAlbumReadTutorial(isRead: Boolean) {
            dataStore.edit { prefs ->
                prefs[TUTORIAL_ALBUM_KEY] = isRead
            }
        }

        override suspend fun getAlbumReadTutorial(): Boolean =
            dataStore.data
                .map { prefs ->
                    prefs[TUTORIAL_ALBUM_KEY]
                }.first() ?: false

        override suspend fun setCalendarReadTutorial(isRead: Boolean) {
            dataStore.edit { prefs ->
                prefs[TUTORIAL_CALENDAR_KEY] = isRead
            }
        }

        override suspend fun getCalendarReadTutorial(): Boolean =
            dataStore.data
                .map { prefs ->
                    prefs[TUTORIAL_CALENDAR_KEY]
                }.first() ?: false

        companion object {
            val TUTORIAL_TIME_CAPSULE_KEY = booleanPreferencesKey("tutorial_time_capsule_key")
            val TUTORIAL_INTEREST_KEY = booleanPreferencesKey("tutorial_interest_key")
            val TUTORIAL_CHAT_KEY = booleanPreferencesKey("tutorial_chat_key")
            val TUTORIAL_QUESTION_KEY = booleanPreferencesKey("tutorial_question_key")
            val TUTORIAL_ALBUM_KEY = booleanPreferencesKey("tutorial_album_key")
            val TUTORIAL_CALENDAR_KEY = booleanPreferencesKey("tutorial_calendar_key")
        }
    }
