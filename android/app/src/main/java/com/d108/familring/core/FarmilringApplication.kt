package com.d108.familring.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FarmilringApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
