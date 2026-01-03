package com.example.callguardian

import android.app.Application
import com.example.callguardian.BuildConfig
import com.example.callguardian.util.logging.Logger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CallGuardianApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize centralized logging system
        if (BuildConfig.DEBUG && Timber.forest().none { it is Timber.DebugTree }) {
            Timber.plant(Timber.DebugTree())
        } else {
            // Use our custom logging configuration
            com.example.callguardian.util.logging.LoggingConfig.init()
        }
        
        // Log application startup using our centralized logger
        Logger.d("CallGuardian Application started")
    }
}
