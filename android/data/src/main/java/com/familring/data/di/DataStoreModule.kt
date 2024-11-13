package com.familring.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.familring.data.R
import com.familring.data.datastoreImpl.AuthDataStoreImpl
import com.familring.data.datastoreImpl.TokenDataStoreImpl
import com.familring.domain.datastore.AuthDataStore
import com.familring.domain.datastore.TokenDataStore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Singleton
    @Provides
    fun providesDataStore(
        @ApplicationContext appContext: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            appContext.preferencesDataStoreFile(appContext.getString(R.string.app_name))
        }

    @Singleton
    @Provides
    fun provideTokenDataStore(dataStore: DataStore<Preferences>): TokenDataStore = TokenDataStoreImpl(dataStore)

    @Singleton
    @Provides
    fun provideAuthDataStore(dataStore: DataStore<Preferences>): AuthDataStore = AuthDataStoreImpl(dataStore)

    @Singleton
    @Provides
    fun provideFriebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}
