package com.example.callguardian.firebase

import android.content.Context
import com.example.callguardian.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import timber.log.Timber

/**
 * Initializes Firebase whether or not app/google-services.json was present at build time.
 *
 * - With google-services.json (or FIREBASE_* gradle properties) the app is fully configured:
 *   cloud Gemini calls work once App Check is set up.
 * - Without it, Firebase is initialized with inert placeholder options so that the on-device
 *   path of Firebase AI Logic (Gemma 4 / Gemini Nano via AICore) still works. Cloud calls are
 *   disabled in the UI in that case.
 */
object FirebaseBootstrap {

    @Volatile var cloudConfigured: Boolean = false
        private set

    fun init(context: Context) {
        val existing = FirebaseApp.getApps(context)
        if (existing.isNotEmpty()) {
            cloudConfigured = BuildConfig.HAS_GOOGLE_SERVICES_JSON
        } else {
            val auto = FirebaseApp.initializeApp(context)
            if (auto != null) {
                cloudConfigured = true
            } else {
                val hasProps = BuildConfig.FIREBASE_API_KEY.isNotBlank() && BuildConfig.FIREBASE_APP_ID.isNotBlank()
                val options = FirebaseOptions.Builder()
                    .setApiKey(BuildConfig.FIREBASE_API_KEY.ifBlank { "placeholder-api-key" })
                    .setApplicationId(BuildConfig.FIREBASE_APP_ID.ifBlank { "1:000000000000:android:0000000000000000" })
                    .setProjectId(BuildConfig.FIREBASE_PROJECT_ID.ifBlank { "callguardian-placeholder" })
                    .build()
                FirebaseApp.initializeApp(context, options)
                cloudConfigured = hasProps
            }
        }
        runCatching { AppCheckInstaller.install() }
            .onFailure { Timber.w(it, "App Check provider install failed") }
        Timber.i("Firebase ready (cloud configured=%s, appCheck=%s)", cloudConfigured, AppCheckInstaller.providerName)
    }
}
