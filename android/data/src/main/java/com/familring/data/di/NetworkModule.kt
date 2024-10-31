package com.familring.data.di

import com.familring.data.BuildConfig
import com.familring.data.network.api.AuthApi
import com.familring.data.network.interceptor.AccessTokenInterceptor
import com.familring.data.network.interceptor.ErrorHandlingInterceptor
import com.familring.data.network.interceptor.JwtAuthenticator
import com.familring.domain.datasource.TokenDataSource
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
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.SERVER_URL)
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
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideJwtAuthenticator(
        tokenDataSource: TokenDataSource,
        authApi: AuthApi,
    ): JwtAuthenticator = JwtAuthenticator(tokenDataSource, authApi)

    @Singleton
    @Provides
    fun provideAccessTokenInterceptor(tokenDataSource: TokenDataSource): AccessTokenInterceptor = AccessTokenInterceptor(tokenDataSource)
}
