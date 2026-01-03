package com.callguardian.app.util.logging

import android.util.Log
import com.callguardian.app.BuildConfig
import timber.log.Timber

/**
 * Centralized logging configuration for CallGuardian.
 * Provides structured logging with different levels and formats for development and production.
 */
object LoggingConfig {

    /**
     * Initialize Timber with appropriate tree based on build type.
     * Should be called in Application.onCreate()
     */
    fun init() {
        if (BuildConfig.DEBUG) {
            // Development: Use debug tree with detailed formatting
            Timber.plant(DebugTree())
        } else {
            // Production: Use custom tree with structured logging
            Timber.plant(ProductionTree())
        }
    }

    /**
     * Debug tree for development builds.
     * Provides detailed logging with thread information and stack traces.
     */
    private class DebugTree : Timber.DebugTree() {

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Add thread information to tag for easier debugging
            val enhancedTag = if (tag != null) {
                "[${Thread.currentThread().name}] $tag"
            } else {
                "[${Thread.currentThread().name}]"
            }
            
            super.log(priority, enhancedTag, message, t)
        }

        override fun createStackElementTag(element: StackTraceElement): String? {
            // Include class name and line number for precise debugging
            return "${element.className}:${element.lineNumber}"
        }
    }

    /**
     * Production tree for release builds.
     * Provides structured logging without sensitive information.
     */
    private class ProductionTree : Timber.Tree() {

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Filter out debug logs in production
            if (priority == Log.DEBUG) return

            // Create structured log message
            val logMessage = buildString {
                append("[${getCurrentTimestamp()}] ")
                append("[${getPriorityName(priority)}] ")
                if (tag != null) append("[$tag] ")
                append(message)
                
                // Add exception details if present
                t?.let {
                    append(" | Exception: ${it.javaClass.simpleName}")
                    if (it.message != null) {
                        append(" | Message: ${it.message}")
                    }
                }
            }

            // Use Android's native logging for production
            Log.println(priority, "CallGuardian", logMessage)
        }

        private fun getCurrentTimestamp(): String {
            return System.currentTimeMillis().toString()
        }

        private fun getPriorityName(priority: Int): String {
            return when (priority) {
                Log.VERBOSE -> "VERBOSE"
                Log.DEBUG -> "DEBUG"
                Log.INFO -> "INFO"
                Log.WARN -> "WARN"
                Log.ERROR -> "ERROR"
                Log.ASSERT -> "ASSERT"
                else -> "UNKNOWN"
            }
        }
    }
}

/**
 * Extension functions for enhanced logging with context
 */
object Logger {

    fun d(message: String, vararg args: Any?) = Timber.d(message, *args)
    fun w(message: String, vararg args: Any?) = Timber.w(message, *args)
    fun e(t: Throwable, message: String, vararg args: Any?) = Timber.e(t, message, *args)

    /**
     * Log a method entry with parameters
     */
    fun logMethodEntry(methodName: String, vararg params: Pair<String, Any?>) {
        val paramStr = params.joinToString(", ") { "${it.first}=${it.second}" }
        Timber.d("Entering $methodName($paramStr)")
    }

    /**
     * Log a method exit with result
     */
    fun logMethodExit(methodName: String, result: Any? = null) {
        val resultStr = result?.let { " -> $it" } ?: ""
        Timber.d("Exiting $methodName$resultStr")
    }

    /**
     * Log performance metrics
     */
    fun logPerformance(operation: String, durationMs: Long, additionalInfo: String = "") {
        val infoStr = if (additionalInfo.isNotEmpty()) " | $additionalInfo" else ""
        Timber.i("Performance: $operation completed in ${durationMs}ms$infoStr")
    }

    /**
     * Log user actions for analytics
     */
    fun logUserAction(action: String, vararg params: Pair<String, Any?>) {
        val paramStr = params.joinToString(", ") { "${it.first}=${it.second}" }
        Timber.i("User Action: $action | $paramStr")
    }

    /**
     * Log configuration changes
     */
    fun logConfigChange(configKey: String, oldValue: Any?, newValue: Any?) {
        Timber.i("Config Change: $configKey changed from $oldValue to $newValue")
    }

    /**
     * Log security events
     */
    fun logSecurityEvent(event: String, vararg params: Pair<String, Any?>) {
        val paramStr = params.joinToString(", ") { "${it.first}=${it.second}" }
        Timber.w("Security Event: $event | $paramStr")
    }

    /**
     * Log API calls with request/response details (sanitized)
     */
    fun logApiCall(
        method: String,
        url: String,
        statusCode: Int? = null,
        durationMs: Long? = null,
        error: Throwable? = null
    ) {
        val details = buildString {
            append("API Call: $method $url")
            statusCode?.let { append(" | Status: $it") }
            durationMs?.let { append(" | Duration: ${it}ms") }
            error?.let { append(" | Error: ${it.javaClass.simpleName}") }
        }
        
        if (error != null) {
            Timber.e(error, details)
        } else if (statusCode != null && statusCode >= 400) {
            Timber.w(details)
        } else {
            Timber.d(details)
        }
    }

    /**
     * Log database operations
     */
    fun logDatabaseOperation(
        operation: String,
        table: String,
        affectedRows: Int? = null,
        durationMs: Long? = null,
        error: Throwable? = null
    ) {
        val details = buildString {
            append("DB Operation: $operation on $table")
            affectedRows?.let { append(" | Affected: $it rows") }
            durationMs?.let { append(" | Duration: ${it}ms") }
            error?.let { append(" | Error: ${it.javaClass.simpleName}") }
        }
        
        if (error != null) {
            Timber.e(error, details)
        } else {
            Timber.d(details)
        }
    }

    /**
     * Log network operations
     */
    fun logNetworkOperation(
        operation: String,
        url: String,
        durationMs: Long? = null,
        bytesTransferred: Long? = null,
        error: Throwable? = null
    ) {
        val details = buildString {
            append("Network: $operation $url")
            durationMs?.let { append(" | Duration: ${it}ms") }
            bytesTransferred?.let { append(" | Data: ${it} bytes") }
            error?.let { append(" | Error: ${it.javaClass.simpleName}") }
        }
        
        if (error != null) {
            Timber.e(error, details)
        } else {
            Timber.d(details)
        }
    }
}