package com.d108.project.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FarmilyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
