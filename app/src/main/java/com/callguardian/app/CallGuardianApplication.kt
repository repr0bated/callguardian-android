package com.callguardian.app

import android.app.Application
import com.callguardian.app.BuildConfig
import com.callguardian.app.util.logging.Logger
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
            com.callguardian.app.util.logging.LoggingConfig.init()
        }
        
        // Log application startup using our centralized logger
        Logger.d("CallGuardian Application started")
    }
}
