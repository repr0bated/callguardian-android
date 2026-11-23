package com.example.callguardian.firebase

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import java.io.File

/**
 * Debug builds use the App Check *debug* provider. On first launch it prints a debug token to
 * logcat ("DebugAppCheckProvider: Enter this debug secret ...") and Settings > Developer shows it
 * too. Register it in Firebase console > Security > App Check > Apps > (app) > Manage debug tokens.
 * Never commit the token.
 */
object AppCheckInstaller {
    const val providerName = "Debug provider"

    fun install() {
        Firebase.appCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
    }

    /** Reads the debug secret the debug provider persisted, so it can be copied without adb. */
    fun debugToken(context: Context): String? {
        val dir = File(context.applicationInfo.dataDir, "shared_prefs")
        val files = dir.listFiles { f -> f.name.startsWith("com.google.firebase.appcheck.debug.store") } ?: return null
        val re = Regex("""<string name="com\.google\.firebase\.appcheck\.debug\.DEBUG_SECRET">([^<]+)</string>""")
        return files.firstNotNullOfOrNull { f -> runCatching { re.find(f.readText())?.groupValues?.get(1) }.getOrNull() }
    }
}
