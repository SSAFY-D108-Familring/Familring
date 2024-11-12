package com.d108.familring.core

import android.app.Application
import com.d108.familring.BuildConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FarmilringApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        }

        FirebaseMessaging
            .getInstance()
            .token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Timber.tag("FCM_TOKEN").d("Current FCM Token: $token")
                } else {
                    Timber.tag("FCM_TOKEN").e("Failed to get FCM token: ${task.exception}")
                }
            }

        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}
