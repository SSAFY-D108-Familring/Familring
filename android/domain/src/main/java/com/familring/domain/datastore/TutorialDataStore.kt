package com.familring.domain.datastore

interface TutorialDataStore {
    suspend fun setTimeCapsuleReadTutorial(isRead: Boolean)

    suspend fun getTimeCapsuleReadTutorial(): Boolean

    suspend fun setInterestReadTutorial(isRead: Boolean)

    suspend fun getInterestReadTutorial(): Boolean

    suspend fun setChatReadTutorial(isRead: Boolean)

    suspend fun getChatReadTutorial(): Boolean

    suspend fun setQuestionReadTutorial(isRead: Boolean)

    suspend fun getQuestionReadTutorial(): Boolean

    suspend fun setAlbumReadTutorial(isRead: Boolean)

    suspend fun getAlbumReadTutorial(): Boolean

    suspend fun setCalendarReadTutorial(isRead: Boolean)

    suspend fun getCalendarReadTutorial(): Boolean
}
