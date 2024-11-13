package com.familring.data.di

import com.familring.data.network.api.AuthApi
import com.familring.data.network.api.DailyApi
import com.familring.data.network.api.FaceApi
import com.familring.data.network.api.FamilyApi
import com.familring.data.network.api.GalleryApi
import com.familring.data.network.api.InterestApi
import com.familring.data.network.api.QuestionApi
import com.familring.data.network.api.ScheduleApi
import com.familring.data.network.api.TimeCapsuleApi
import com.familring.data.network.api.UserApi
import com.google.firebase.messaging.FirebaseMessaging
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

    @Provides
    @Singleton
    fun provideFamilyApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): FamilyApi = retrofit.create()

    @Provides
    @Singleton
    fun provideTimeCapsuleApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): TimeCapsuleApi = retrofit.create()

    @Provides
    @Singleton
    fun provideScheduleApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): ScheduleApi = retrofit.create()

    @Provides
    @Singleton
    fun provideDailyApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): DailyApi = retrofit.create()

    @Provides
    @Singleton
    fun provideQuestionApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): QuestionApi = retrofit.create()

    @Provides
    @Singleton
    fun provideGalleryApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): GalleryApi = retrofit.create()

    @Provides
    @Singleton
    fun provideFaceApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): FaceApi = retrofit.create()

    @Provides
    @Singleton
    fun provideInterestApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): InterestApi = retrofit.create()
}
