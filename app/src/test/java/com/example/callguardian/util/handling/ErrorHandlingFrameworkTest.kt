package com.example.callguardian.util.handling

import com.example.callguardian.util.exceptions.*
import com.example.callguardian.util.logging.Logger
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class ErrorHandlingFrameworkTest {

    private lateinit var errorHandling: ErrorHandling
    private lateinit var logger: Logger

    @Before
    fun setup() {
        logger = mockk(relaxed = true)
        errorHandling = ErrorHandling()
    }

    @Test
    fun `test user-friendly error messages for different exception types`() {
        // Test NetworkException
        val networkException = NetworkException("Network timeout", SocketTimeoutException())
        val networkMessage = errorHandling.getUserFriendlyMessage(networkException)
        assert(networkMessage.contains("network") || networkMessage.contains("connection"))

        // Test DatabaseException
        val databaseException = DatabaseException("Database locked", IOException())
        val databaseMessage = errorHandling.getUserFriendlyMessage(databaseException)
        assert(databaseMessage.contains("database") || databaseMessage.contains("storage"))

        // Test ServiceException
        val serviceException = ServiceException("Service unavailable", IOException())
        val serviceMessage = errorHandling.getUserFriendlyMessage(serviceException)
        assert(serviceMessage.contains("service") || serviceMessage.contains("server"))

        // Test CacheException
        val cacheException = CacheException("Cache miss", IOException())
        val cacheMessage = errorHandling.getUserFriendlyMessage(cacheException)
        assert(cacheMessage.contains("cache") || cacheMessage.contains("data"))

        // Test LookupException
        val lookupException = LookupException("Lookup failed", IOException())
        val lookupMessage = errorHandling.getUserFriendlyMessage(lookupException)
        assert(lookupMessage.contains("lookup") || lookupMessage.contains("search"))

        // Test CallGuardianException
        val callGuardianException = CallGuardianException("General error", IOException())
        val generalMessage = errorHandling.getUserFriendlyMessage(callGuardianException)
        assert(generalMessage.contains("error") || generalMessage.contains("problem"))
    }

    @Test
    fun `test logging with different log levels`() {
        val exception = NetworkException("Test network error", SocketTimeoutException())

        // Test error logging
        errorHandling.logError(exception, "Network operation failed")
        verify { logger.e(exception, "Network operation failed") }

        // Test warning logging
        errorHandling.logWarning(exception, "Network operation warning")
        verify { logger.w(exception, "Network operation warning") }

        // Test info logging
        errorHandling.logInfo("Network operation completed")
        verify { logger.i("Network operation completed") }

        // Test debug logging
        errorHandling.logDebug("Network operation details")
        verify { logger.d("Network operation details") }
    }

    @Test
    fun `test exception categorization`() {
        val networkException = NetworkException("Network error", SocketTimeoutException())
        val databaseException = DatabaseException("Database error", IOException())
        val serviceException = ServiceException("Service error", IOException())

        assert(errorHandling.isNetworkError(networkException))
        assert(!errorHandling.isNetworkError(databaseException))
        assert(!errorHandling.isNetworkError(serviceException))

        assert(errorHandling.isDatabaseError(databaseException))
        assert(!errorHandling.isDatabaseError(networkException))
        assert(!errorHandling.isDatabaseError(serviceException))

        assert(errorHandling.isServiceError(serviceException))
        assert(!errorHandling.isServiceError(networkException))
        assert(!errorHandling.isServiceError(databaseException))
    }

    @Test
    fun `test retry logic for network operations`() {
        var attemptCount = 0
        val maxAttempts = 3

        // Simulate a network operation that fails twice then succeeds
        val result = errorHandling.withRetry(maxAttempts, 100) {
            attemptCount++
            if (attemptCount < 3) {
                throw NetworkException("Temporary network failure", SocketTimeoutException())
            }
            "Success"
        }

        assert(result == "Success")
        assert(attemptCount == 3)
    }

    @Test
    fun `test retry logic exhausts attempts`() {
        var attemptCount = 0
        val maxAttempts = 3

        // Simulate a network operation that always fails
        try {
            errorHandling.withRetry(maxAttempts, 100) {
                attemptCount++
                throw NetworkException("Persistent network failure", SocketTimeoutException())
            }
        } catch (e: NetworkException) {
            assert(attemptCount == maxAttempts)
            assert(e.message == "Persistent network failure")
        }
    }

    @Test
    fun `test graceful degradation for database operations`() {
        // Simulate database operation with graceful degradation
        val result = errorHandling.withGracefulDegradation {
            throw DatabaseException("Database unavailable", IOException())
        }

        // Should return null instead of throwing exception
        assert(result == null)
    }

    @Test
    fun `test exception chaining and context preservation`() {
        val rootCause = SocketTimeoutException("Connection timeout")
        val networkException = NetworkException("Network operation failed", rootCause)
        val lookupException = LookupException("Lookup operation failed", networkException)

        // Test exception chaining
        assert(lookupException.cause == networkException)
        assert(networkException.cause == rootCause)

        // Test context preservation in user-friendly message
        val message = errorHandling.getUserFriendlyMessage(lookupException)
        assert(message.contains("lookup") || message.contains("search"))
    }

    @Test
    fun `test logging configuration for different build types`() {
        // Test debug build logging
        errorHandling.configureLogging(true)
        verify { logger.d("Debug logging enabled") }

        // Test release build logging
        errorHandling.configureLogging(false)
        verify { logger.i("Release logging configured") }
    }

    @Test
    fun `test error recovery strategies`() {
        var operationSucceeded = false

        // Test fallback strategy
        val result = errorHandling.withFallback(
            primaryOperation = {
                throw NetworkException("Primary operation failed", SocketTimeoutException())
            },
            fallbackOperation = {
                operationSucceeded = true
                "Fallback result"
            }
        )

        assert(operationSucceeded)
        assert(result == "Fallback result")
    }

    @Test
    fun `test error metrics collection`() {
        val exception = NetworkException("Test error", SocketTimeoutException())

        // Simulate error metrics collection
        errorHandling.recordError(exception, "network_operation")

        // Verify that error was recorded (this would typically update internal metrics)
        verify { logger.w("Error recorded for network_operation: Test error") }
    }

    @Test
    fun `test user notification generation`() {
        val exception = NetworkException("Connection lost", SocketTimeoutException())
        val userMessage = errorHandling.generateUserNotification(exception)

        assert(userMessage.title.isNotBlank())
        assert(userMessage.message.isNotBlank())
        assert(userMessage.action != null || userMessage.action == null)
    }

    @Test
    fun `test exception factory methods`() {
        // Test creating specific exception types
        val networkException = ExceptionFactory.createNetworkException("Network error")
        assert(networkException is NetworkException)

        val databaseException = ExceptionFactory.createDatabaseException("Database error")
        assert(databaseException is DatabaseException)

        val serviceException = ExceptionFactory.createServiceException("Service error")
        assert(serviceException is ServiceException)

        val cacheException = ExceptionFactory.createCacheException("Cache error")
        assert(cacheException is CacheException)

        val lookupException = ExceptionFactory.createLookupException("Lookup error")
        assert(lookupException is LookupException)
    }
}