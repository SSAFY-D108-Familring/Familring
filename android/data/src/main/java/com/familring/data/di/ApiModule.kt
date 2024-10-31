package com.familring.data.di

import com.familring.data.network.api.AuthApi
import com.familring.data.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideAuthApi(
        @NetworkModule.AuthClient retrofit: Retrofit,
    ): AuthApi = retrofit.create()

    @Provides
    @Singleton
    fun provideUserApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): UserApi = retrofit.create()
}
