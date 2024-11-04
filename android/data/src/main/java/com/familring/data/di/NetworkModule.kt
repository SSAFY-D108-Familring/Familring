package com.familring.data.di

import com.familring.data.BuildConfig
import com.familring.data.network.api.AuthApi
import com.familring.data.network.interceptor.AccessTokenInterceptor
import com.familring.data.network.interceptor.ErrorHandlingInterceptor
import com.familring.data.network.interceptor.JwtAuthenticator
import com.familring.domain.datasource.TokenDataStore
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseClient

    @BaseClient
    @Singleton
    @Provides
    fun provideOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        jwtAuthenticator: JwtAuthenticator,
    ) = OkHttpClient.Builder().run {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        addInterceptor(ErrorHandlingInterceptor())
        addInterceptor(accessTokenInterceptor)
        authenticator(jwtAuthenticator)
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        build()
    }

    @AuthClient
    @Singleton
    @Provides
    fun provideAuthClient(accessTokenInterceptor: AccessTokenInterceptor) =
        OkHttpClient.Builder().run {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            addInterceptor(ErrorHandlingInterceptor())
            addInterceptor(accessTokenInterceptor)
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            build()
        }

    @AuthClient
    @Singleton
    @Provides
    fun provideAuthRetrofit(
        @AuthClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create(),
                ),
            ).baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @BaseClient
    fun provideRetrofit(
        @BaseClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").setLenient().create(),
                ),
            ).baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideJwtAuthenticator(
        tokenDataStore: TokenDataStore,
        authApi: AuthApi,
    ): JwtAuthenticator = JwtAuthenticator(tokenDataStore, authApi)

    @Singleton
    @Provides
    fun provideAccessTokenInterceptor(tokenDataStore: TokenDataStore): AccessTokenInterceptor = AccessTokenInterceptor(tokenDataStore)
}
