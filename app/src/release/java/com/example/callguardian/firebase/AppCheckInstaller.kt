package com.example.callguardian.firebase

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

/** Release builds attest with Play Integrity (requires the app to be linked in Play Console). */
object AppCheckInstaller {
    const val providerName = "Play Integrity"

    fun install() {
        Firebase.appCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
    }

    @Suppress("UNUSED_PARAMETER")
    fun debugToken(context: Context): String? = null
}
