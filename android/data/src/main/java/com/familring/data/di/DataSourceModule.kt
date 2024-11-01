package com.familring.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.familring.data.R
import com.familring.data.datasourceImpl.AuthDataSourceImpl
import com.familring.data.datasourceImpl.TokenDataSourceImpl
import com.familring.domain.datasource.AuthDataSource
import com.familring.domain.datasource.TokenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
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
    fun provideTokenDataSource(dataStore: DataStore<Preferences>): TokenDataSource = TokenDataSourceImpl(dataStore)

    @Singleton
    @Provides
    fun provideAuthDataSource(dataStore: DataStore<Preferences>): AuthDataSource = AuthDataSourceImpl(dataStore)
}
