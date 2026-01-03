package com.example.callguardian.util.exceptions

import java.io.IOException

/**
 * Base exception for all CallGuardian application errors.
 * Provides a foundation for structured error handling with user-friendly messages.
 */
open class CallGuardianException(
    message: String? = null,
    cause: Throwable? = null,
    val errorCode: String? = null,
    val userMessage: String? = null
) : Exception(message, cause) {

    /**
     * Returns a user-friendly error message for display in the UI.
     * Falls back to the exception message if no user message is provided.
     */
    fun getUserMessage(): String = userMessage ?: message ?: "An unexpected error occurred"
}

/**
 * Exception thrown when network operations fail.
 * Includes retry information and network-specific error details.
 */
class NetworkException(
    message: String? = null,
    cause: Throwable? = null,
    val httpCode: Int? = null,
    val isRetryable: Boolean = true,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "NETWORK_ERROR", userMessage)

/**
 * Exception thrown when database operations fail.
 * Includes information about the operation and whether data loss occurred.
 */
class DatabaseException(
    message: String? = null,
    cause: Throwable? = null,
    val operation: String? = null,
    val isDataLoss: Boolean = false,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "DATABASE_ERROR", userMessage)

/**
 * Exception thrown when API calls fail with specific error codes.
 */
class ApiException(
    message: String? = null,
    cause: Throwable? = null,
    val apiCode: String? = null,
    val isRateLimited: Boolean = false,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "API_ERROR", userMessage)

/**
 * Exception thrown when user input validation fails.
 */
class ValidationException(
    message: String? = null,
    cause: Throwable? = null,
    val field: String? = null,
    val validationRule: String? = null,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "VALIDATION_ERROR", userMessage)

/**
 * Exception thrown when permissions are missing or denied.
 */
class PermissionException(
    message: String? = null,
    cause: Throwable? = null,
    val permission: String? = null,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "PERMISSION_ERROR", userMessage)

/**
 * Exception thrown when storage operations fail.
 */
class StorageException(
    message: String? = null,
    cause: Throwable? = null,
    val storageType: String? = null,
    val isCorrupted: Boolean = false,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "STORAGE_ERROR", userMessage)

/**
 * Exception thrown when AI processing fails.
 */
class AiProcessingException(
    message: String? = null,
    cause: Throwable? = null,
    val model: String? = null,
    val inputSize: Int? = null,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "AI_PROCESSING_ERROR", userMessage)

/**
 * Exception thrown when contact operations fail.
 */
class ContactException(
    message: String? = null,
    cause: Throwable? = null,
    val operation: String? = null,
    val contactId: Long? = null,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "CONTACT_ERROR", userMessage)

/**
 * Exception thrown when lookup operations fail.
 */
class LookupException(
    message: String? = null,
    cause: Throwable? = null,
    val source: String? = null,
    val phoneNumber: String? = null,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "LOOKUP_ERROR", userMessage)

/**
 * Exception thrown when cache operations fail.
 */
class CacheException(
    message: String? = null,
    cause: Throwable? = null,
    val operation: String? = null,
    val isExpired: Boolean = false,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "CACHE_ERROR", userMessage)

/**
 * Exception thrown when service operations fail.
 */
class ServiceException(
    message: String? = null,
    cause: Throwable? = null,
    val serviceName: String? = null,
    val isRetryable: Boolean = true,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "SERVICE_ERROR", userMessage)

/**
 * Exception thrown when configuration is invalid or missing.
 */
class ConfigurationException(
    message: String? = null,
    cause: Throwable? = null,
    val configKey: String? = null,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "CONFIGURATION_ERROR", userMessage)

/**
 * Exception thrown when security operations fail.
 */
class SecurityException(
    message: String? = null,
    cause: Throwable? = null,
    val operation: String? = null,
    val securityLevel: String? = null,
    val userMessage: String? = null
) : CallGuardianException(message, cause, "SECURITY_ERROR", userMessage)

/**
 * Utility functions for creating common exceptions with user-friendly messages
 */
object ExceptionFactory {

    fun networkError(
        message: String? = null,
        cause: Throwable? = null,
        httpCode: Int? = null,
        isRetryable: Boolean = true
    ): NetworkException {
        val userMessage = when {
            cause is IOException -> "Network connection failed. Please check your internet connection and try again."
            httpCode == 401 -> "Authentication failed. Please check your API credentials."
            httpCode == 403 -> "Access denied. You don't have permission to perform this action."
            httpCode == 429 -> "Too many requests. Please wait a moment and try again."
            httpCode in 500..599 -> "Server error. Please try again later."
            else -> "Network error occurred. Please try again."
        }
        return NetworkException(message, cause, httpCode, isRetryable, userMessage)
    }

    fun databaseError(
        message: String? = null,
        cause: Throwable? = null,
        operation: String? = null,
        isDataLoss: Boolean = false
    ): DatabaseException {
        val userMessage = if (isDataLoss) {
            "Database error occurred and some data may have been lost. Please contact support."
        } else {
            "Database operation failed. Please try again or restart the app."
        }
        return DatabaseException(message, cause, operation, isDataLoss, userMessage)
    }

    fun apiError(
        message: String? = null,
        cause: Throwable? = null,
        apiCode: String? = null,
        isRateLimited: Boolean = false
    ): ApiException {
        val userMessage = if (isRateLimited) {
            "Too many requests to the service. Please wait a moment before trying again."
        } else {
            "Service temporarily unavailable. Please try again later."
        }
        return ApiException(message, cause, apiCode, isRateLimited, userMessage)
    }

    fun validationError(
        message: String? = null,
        cause: Throwable? = null,
        field: String? = null,
        validationRule: String? = null
    ): ValidationException {
        val userMessage = when (field) {
            "phoneNumber" -> "Please enter a valid phone number."
            "email" -> "Please enter a valid email address."
            "name" -> "Please enter a valid name."
            else -> "Invalid input. Please check your entry and try again."
        }
        return ValidationException(message, cause, field, validationRule, userMessage)
    }

    fun permissionError(
        message: String? = null,
        cause: Throwable? = null,
        permission: String? = null
    ): PermissionException {
        val userMessage = when (permission) {
            "android.permission.READ_CONTACTS" -> "Contacts permission is required to access your contacts."
            "android.permission.READ_PHONE_STATE" -> "Phone state permission is required for call screening."
            "android.permission.CALL_PHONE" -> "Call permission is required to make phone calls."
            "android.permission.READ_SMS" -> "SMS permission is required to read messages."
            else -> "Permission required. Please grant the necessary permissions in settings."
        }
        return PermissionException(message, cause, permission, userMessage)
    }

    fun storageError(
        message: String? = null,
        cause: Throwable? = null,
        storageType: String? = null,
        isCorrupted: Boolean = false
    ): StorageException {
        val userMessage = if (isCorrupted) {
            "Storage data appears to be corrupted. Please restart the app or contact support."
        } else {
            "Storage operation failed. Please check available storage space."
        }
        return StorageException(message, cause, storageType, isCorrupted, userMessage)
    }

    fun aiProcessingError(
        message: String? = null,
        cause: Throwable? = null,
        model: String? = null,
        inputSize: Int? = null
    ): AiProcessingException {
        val userMessage = "AI processing failed. Please try again or check your internet connection."
        return AiProcessingException(message, cause, model, inputSize, userMessage)
    }

    fun contactError(
        message: String? = null,
        cause: Throwable? = null,
        operation: String? = null,
        contactId: Long? = null
    ): ContactException {
        val userMessage = when (operation) {
            "lookup" -> "Failed to find contact information."
            "sync" -> "Contact synchronization failed. Please try again."
            "update" -> "Failed to update contact information."
            else -> "Contact operation failed. Please try again."
        }
        return ContactException(message, cause, operation, contactId, userMessage)
    }

    fun lookupError(
        message: String? = null,
        cause: Throwable? = null,
        source: String? = null,
        phoneNumber: String? = null
    ): LookupException {
        val userMessage = "Phone number lookup failed. Please check the number and try again."
        return LookupException(message, cause, source, phoneNumber, userMessage)
    }

    fun configurationError(
        message: String? = null,
        cause: Throwable? = null,
        configKey: String? = null
    ): ConfigurationException {
        val userMessage = "Configuration error occurred. Please restart the app or contact support."
        return ConfigurationException(message, cause, configKey, userMessage)
    }

    fun securityError(
        message: String? = null,
        cause: Throwable? = null,
        operation: String? = null,
        securityLevel: String? = null
    ): SecurityException {
        val userMessage = "Security operation failed. Please restart the app or contact support."
        return SecurityException(message, cause, operation, securityLevel, userMessage)
    }

    fun createLookupException(
        message: String? = null,
        cause: Throwable? = null,
        source: String? = null,
        phoneNumber: String? = null
    ): LookupException {
        return lookupError(message, cause, source, phoneNumber)
    }
}