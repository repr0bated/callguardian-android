package com.example.callguardian.screening

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner

/** On-device generative inference (AICore) is only allowed while the app is in the foreground. */
object AppForeground {
    val isForeground: Boolean
        get() = runCatching {
            ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
        }.getOrDefault(false)
}
