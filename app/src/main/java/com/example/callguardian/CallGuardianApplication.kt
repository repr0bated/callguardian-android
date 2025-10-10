package com.example.callguardian

import android.app.Application
import com.example.callguardian.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CallGuardianApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG && Timber.forest().none { it is Timber.DebugTree }) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
