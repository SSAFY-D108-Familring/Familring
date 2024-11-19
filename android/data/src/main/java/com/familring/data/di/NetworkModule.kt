package com.familring.data.di

import android.content.Context
import com.familring.data.BuildConfig
import com.familring.data.network.api.AuthApi
import com.familring.data.network.interceptor.AccessTokenInterceptor
import com.familring.data.network.interceptor.ErrorHandlingInterceptor
import com.familring.data.network.interceptor.JwtAuthenticator
import com.familring.data.network.interceptor.isJsonArray
import com.familring.data.network.interceptor.isJsonObject
import com.familring.domain.datastore.TokenDataStore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.google.gson.JsonSyntaxException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .setLenient()
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonDeserializer { json, _, _ ->
                    LocalDateTime.parse(
                        json.asString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    )
                },
            ).registerTypeAdapter(
                LocalDateTime::class.java,
                JsonSerializer<LocalDateTime> { src, _, _ ->
                    JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                },
            ).registerTypeAdapter(
                LocalDate::class.java,
                JsonDeserializer { json, _, _ ->
                    LocalDate.parse(json.asString, DateTimeFormatter.ISO_DATE)
                },
            ).registerTypeAdapter(
                LocalDate::class.java,
                JsonSerializer<LocalDate> { src, _, _ ->
                    JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE))
                },
            ).create()

    @BaseClient
    @Singleton
    @Provides
    fun provideOkHttpClient(
        logger: HttpLoggingInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor,
        jwtAuthenticator: JwtAuthenticator,
    ) = OkHttpClient.Builder().run {
        addInterceptor(logger)
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
    fun provideAuthClient(
        logger: HttpLoggingInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor,
    ) = OkHttpClient.Builder().run {
        addInterceptor(logger)
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
        gson: Gson,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(
                GsonConverterFactory.create(gson),
            ).baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @BaseClient
    fun provideRetrofit(
        @BaseClient
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(
                GsonConverterFactory.create(gson),
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

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor =
            HttpLoggingInterceptor {
                when {
                    !it.isJsonArray() && !it.isJsonObject() ->
                        Timber.tag("RETROFIT").d("CONNECTION INFO: $it")

                    else ->
                        try {
                            Timber.tag("RETROFIT").d(
                                GsonBuilder().setPrettyPrinting().create().toJson(
                                    JsonParser().parse(it),
                                ),
                            )
                        } catch (m: JsonSyntaxException) {
                            Timber.tag("RETROFIT").d(it)
                        }
                }
            }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context
}
