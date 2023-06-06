package com.hbworld.onehub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OneHubApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}