package com.callguardian.app.util.handling

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.callguardian.app.R
import com.callguardian.app.util.exceptions.CallGuardianException
import com.callguardian.app.util.exceptions.ConfigurationException
import com.callguardian.app.util.exceptions.ContactException
import com.callguardian.app.util.exceptions.DatabaseException
import com.callguardian.app.util.exceptions.LookupException
import com.callguardian.app.util.exceptions.NetworkException
import com.callguardian.app.util.exceptions.PermissionException
import com.callguardian.app.util.exceptions.SecurityException
import com.callguardian.app.util.exceptions.StorageException
import com.callguardian.app.util.exceptions.ValidationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Centralized error handling utilities for CallGuardian.
 * Provides consistent error handling patterns across the application.
 */
object ErrorHandling {

    /**
     * Handles exceptions with appropriate logging and user feedback.
     * Returns a user-friendly message that can be displayed to the user.
     */
    fun handleException(
        context: Context,
        throwable: Throwable,
        operation: String? = null,
        showToast: Boolean = true
    ): String {
        val userMessage = when (throwable) {
            is CallGuardianException -> {
                // Log the exception with context
                Timber.e(throwable, "CallGuardian exception during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is NetworkException -> {
                Timber.e(throwable, "Network error during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is DatabaseException -> {
                Timber.e(throwable, "Database error during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is PermissionException -> {
                Timber.w(throwable, "Permission error during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is ValidationException -> {
                Timber.w(throwable, "Validation error during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is StorageException -> {
                Timber.e(throwable, "Storage error during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is SecurityException -> {
                Timber.e(throwable, "Security error during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is ConfigurationException -> {
                Timber.e(throwable, "Configuration error during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is ContactException -> {
                Timber.e(throwable, "Contact error during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is LookupException -> {
                Timber.e(throwable, "Lookup error during $operation: ${throwable.message}")
                throwable.getUserMessage()
            }
            is SocketTimeoutException -> {
                Timber.w(throwable, "Socket timeout during $operation")
                context.getString(R.string.error_network_timeout)
            }
            is UnknownHostException -> {
                Timber.w(throwable, "Unknown host during $operation")
                context.getString(R.string.error_network_no_connection)
            }
            is IOException -> {
                Timber.w(throwable, "IO error during $operation")
                context.getString(R.string.error_network_io_error)
            }
            else -> {
                Timber.e(throwable, "Unexpected error during $operation: ${throwable.message}")
                context.getString(R.string.error_generic)
            }
        }

        // Show toast if requested
        if (showToast) {
            showToast(context, userMessage)
        }

        return userMessage
    }

    /**
     * Safely executes a suspend function with error handling.
     * Catches exceptions and converts them to user-friendly messages.
     */
    suspend fun <T> safeExecute(
        context: Context,
        operation: String,
        block: suspend () -> T
    ): Result<T> {
        return try {
            val result = block()
            Timber.d("Operation '$operation' completed successfully")
            Result.success(result)
        } catch (e: Exception) {
            val userMessage = handleException(context, e, operation, showToast = false)
            Timber.w("Operation '$operation' failed: $userMessage")
            Result.failure(CallGuardianException(
                message = "Operation '$operation' failed",
                cause = e,
                userMessage = userMessage
            ))
        }
    }

    /**
     * Safely executes a suspend function with retry logic.
     */
    suspend fun <T> safeExecuteWithRetry(
        context: Context,
        operation: String,
        maxRetries: Int = 3,
        retryDelayMs: Long = 1000L,
        block: suspend () -> T
    ): Result<T> {
        var lastException: Exception? = null

        repeat(maxRetries + 1) { attempt ->
            try {
                val result = block()
                if (attempt > 0) {
                    Timber.d("Operation '$operation' succeeded on attempt ${attempt + 1}")
                } else {
                    Timber.d("Operation '$operation' completed successfully")
                }
                return Result.success(result)
            } catch (e: Exception) {
                lastException = e
                if (attempt < maxRetries && isRetryableError(e)) {
                    Timber.w("Operation '$operation' failed on attempt ${attempt + 1}, retrying in ${retryDelayMs}ms: ${e.message}")
                    kotlinx.coroutines.delay(retryDelayMs * (attempt + 1)) // Exponential backoff
                } else {
                    Timber.w("Operation '$operation' failed after ${attempt + 1} attempts: ${e.message}")
                    break
                }
            }
        }

        val userMessage = handleException(context, lastException!!, operation, showToast = false)
        return Result.failure(CallGuardianException(
            message = "Operation '$operation' failed after ${maxRetries + 1} attempts",
            cause = lastException,
            userMessage = userMessage
        ))
    }

    /**
     * Checks if an exception is retryable.
     */
    private fun isRetryableError(throwable: Throwable): Boolean {
        return when (throwable) {
            is NetworkException -> throwable.isRetryable
            is SocketTimeoutException -> true
            is UnknownHostException -> true
            is IOException -> true
            else -> false
        }
    }

    /**
     * Shows a toast message with appropriate duration based on message length.
     */
    private fun showToast(context: Context, message: String) {
        val duration = if (message.length > 50) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(context, message, duration).show()
    }

    /**
     * Handles permission denial with user guidance.
     */
    fun handlePermissionDenied(
        context: Context,
        permission: String,
        rationale: String? = null
    ) {
        val message = rationale ?: context.getString(R.string.permission_denied_rationale, permission)
        handleException(context, PermissionException(
            message = "Permission denied: $permission",
            permission = permission
        ), "permission_request", showToast = true)
    }

    /**
     * Handles validation errors with field-specific messages.
     */
    fun handleValidationError(
        context: Context,
        field: String,
        validationRule: String,
        value: Any? = null
    ): String {
        val message = context.getString(R.string.validation_error, field, validationRule)
        val exception = ValidationException(
            message = "Validation failed for field '$field'",
            field = field,
            validationRule = validationRule
        )
        return handleException(context, exception, "validation", showToast = true)
    }

    /**
     * Handles database errors with graceful degradation.
     */
    fun handleDatabaseError(
        context: Context,
        operation: String,
        throwable: Throwable,
        isDataLoss: Boolean = false
    ): String {
        val exception = DatabaseException(
            message = "Database operation '$operation' failed",
            cause = throwable,
            operation = operation,
            isDataLoss = isDataLoss
        )
        return handleException(context, exception, operation, showToast = true)
    }

    /**
     * Handles network errors with retry suggestions.
     */
    fun handleNetworkError(
        context: Context,
        operation: String,
        throwable: Throwable,
        isRetryable: Boolean = true
    ): String {
        val exception = NetworkException(
            message = "Network operation '$operation' failed",
            cause = throwable,
            isRetryable = isRetryable
        )
        return handleException(context, exception, operation, showToast = true)
    }

    /**
     * Handles API errors with specific error codes.
     */
    fun handleApiError(
        context: Context,
        operation: String,
        throwable: Throwable,
        apiCode: String? = null,
        isRateLimited: Boolean = false
    ): String {
        val exception = com.callguardian.app.util.exceptions.ApiException(
            message = "API operation '$operation' failed",
            cause = throwable,
            apiCode = apiCode,
            isRateLimited = isRateLimited
        )
        return handleException(context, exception, operation, showToast = true)
    }

    /**
     * Handles lookup errors with source information.
     */
    fun handleLookupError(
        context: Context,
        phoneNumber: String,
        source: String? = null,
        throwable: Throwable
    ): String {
        val exception = LookupException(
            message = "Lookup for $phoneNumber failed",
            cause = throwable,
            source = source,
            phoneNumber = phoneNumber
        )
        return handleException(context, exception, "lookup", showToast = true)
    }

    /**
     * Handles contact errors with operation context.
     */
    fun handleContactError(
        context: Context,
        operation: String,
        throwable: Throwable,
        contactId: Long? = null
    ): String {
        val exception = ContactException(
            message = "Contact operation '$operation' failed",
            cause = throwable,
            operation = operation,
            contactId = contactId
        )
        return handleException(context, exception, operation, showToast = true)
    }

    /**
     * Handles configuration errors with restart suggestions.
     */
    fun handleConfigurationError(
        context: Context,
        configKey: String,
        throwable: Throwable
    ): String {
        val exception = ConfigurationException(
            message = "Configuration error for key '$configKey'",
            cause = throwable,
            configKey = configKey
        )
        return handleException(context, exception, "configuration", showToast = true)
    }

    /**
     * Handles security errors with immediate action required.
     */
    fun handleSecurityError(
        context: Context,
        operation: String,
        throwable: Throwable,
        securityLevel: String? = null
    ): String {
        val exception = SecurityException(
            message = "Security error during '$operation'",
            cause = throwable,
            operation = operation,
            securityLevel = securityLevel
        )
        return handleException(context, exception, operation, showToast = true)
    }

    /**
     * Handles storage errors with corruption detection.
     */
    fun handleStorageError(
        context: Context,
        operation: String,
        throwable: Throwable,
        storageType: String? = null,
        isCorrupted: Boolean = false
    ): String {
        val exception = StorageException(
            message = "Storage operation '$operation' failed",
            cause = throwable,
            storageType = storageType,
            isCorrupted = isCorrupted
        )
        return handleException(context, exception, operation, showToast = true)
    }
}

/**
 * Extension function for safe execution of suspend functions.
 */
suspend fun <T> CoroutineScope.safeLaunch(
    context: Context,
    operation: String,
    onError: ((String) -> Unit)? = null,
    block: suspend CoroutineScope.() -> T
) {
    launch {
        val result = ErrorHandling.safeExecute(context, operation, block)
        if (result.isFailure) {
            val exception = result.exceptionOrNull()
            val userMessage = exception?.message ?: "Operation failed"
            onError?.invoke(userMessage)
        }
    }
}

/**
 * Extension function for safe execution with retry logic.
 */
suspend fun <T> CoroutineScope.safeLaunchWithRetry(
    context: Context,
    operation: String,
    maxRetries: Int = 3,
    retryDelayMs: Long = 1000L,
    onError: ((String) -> Unit)? = null,
    block: suspend CoroutineScope.() -> T
) {
    launch {
        val result = ErrorHandling.safeExecuteWithRetry(
            context = context,
            operation = operation,
            maxRetries = maxRetries,
            retryDelayMs = retryDelayMs,
            block = block
        )
        if (result.isFailure) {
            val exception = result.exceptionOrNull()
            val userMessage = exception?.message ?: "Operation failed after retries"
            onError?.invoke(userMessage)
        }
    }
}

/**
 * Utility for background error handling without UI context.
 */
object BackgroundErrorHandling {
    
    /**
     * Logs errors that occur in background operations.
     */
    fun logBackgroundError(operation: String, throwable: Throwable) {
        Timber.e(throwable, "Background operation '$operation' failed")
    }
    
    /**
     * Safely executes a background operation with error logging.
     */
    suspend fun <T> safeExecuteBackground(
        operation: String,
        block: suspend () -> T
    ): Result<T> {
        return try {
            val result = block()
            Timber.d("Background operation '$operation' completed successfully")
            Result.success(result)
        } catch (e: Exception) {
            logBackgroundError(operation, e)
            Result.failure(e)
        }
    }
}