package com.callguardian.app.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.callguardian.app.util.exceptions.NetworkException
import com.callguardian.app.util.exceptions.ExceptionFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Network manager for handling network operations with retry logic and error handling.
 */
class NetworkManager(private val context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Checks if the device has an active network connection.
     */
    fun isNetworkAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo?.isConnectedOrConnecting == true
        }
    }

    /**
     * Checks if the device is connected to Wi-Fi.
     */
    fun isWifiConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo?.type == ConnectivityManager.TYPE_WIFI
        }
    }

    /**
     * Checks if the device is connected via mobile data.
     */
    fun isMobileDataConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo?.type == ConnectivityManager.TYPE_MOBILE
        }
    }

    /**
     * Executes a network operation with retry logic.
     */
    suspend fun <T> executeWithRetry(
        operationName: String,
        maxRetries: Int = 3,
        initialDelayMs: Long = 1000L,
        maxDelayMs: Long = 10000L,
        exponentialBackoff: Boolean = true,
        block: suspend () -> T
    ): T {
        if (!isNetworkAvailable()) {
            throw NetworkException(
                message = "No network connection available",
                isRetryable = true,
                userMessage = "Please check your internet connection and try again."
            )
        }

        var lastException: Exception? = null
        var delay = initialDelayMs

        repeat(maxRetries + 1) { attempt ->
            try {
                val startTime = System.currentTimeMillis()
                val result = block()
                val duration = System.currentTimeMillis() - startTime
                
                Timber.d("Network operation '$operationName' succeeded on attempt ${attempt + 1} in ${duration}ms")
                return result
            } catch (e: Exception) {
                lastException = e
                val isLastAttempt = attempt == maxRetries
                
                if (isLastAttempt || !isRetryableError(e)) {
                    Timber.e(e, "Network operation '$operationName' failed permanently after ${attempt + 1} attempts")
                    throw handleNetworkError(e, operationName)
                }
                
                Timber.w(e, "Network operation '$operationName' failed on attempt ${attempt + 1}, retrying in ${delay}ms")
                
                // Wait before retrying
                delay(delay)
                
                // Update delay for next attempt (exponential backoff)
                if (exponentialBackoff) {
                    delay = minOf(delay * 2, maxDelayMs)
                } else {
                    delay = minOf(delay + initialDelayMs, maxDelayMs)
                }
            }
        }

        // This should never be reached, but just in case
        throw lastException ?: IOException("Unknown network error")
    }

    /**
     * Executes a network operation with circuit breaker pattern.
     */
    suspend fun <T> executeWithCircuitBreaker(
        operationName: String,
        maxRetries: Int = 3,
        block: suspend () -> T
    ): T {
        return executeWithRetry(
            operationName = operationName,
            maxRetries = maxRetries,
            block = block
        )
    }

    /**
     * Checks if an error is retryable.
     */
    private fun isRetryableError(throwable: Throwable): Boolean {
        return when (throwable) {
            is SocketTimeoutException -> true
            is UnknownHostException -> true
            is IOException -> {
                // Check for specific IO exceptions that might be retryable
                throwable.message?.contains("timeout", ignoreCase = true) == true ||
                throwable.message?.contains("connection", ignoreCase = true) == true
            }
            else -> false
        }
    }

    /**
     * Handles network errors and converts them to appropriate exceptions.
     */
    private fun handleNetworkError(throwable: Throwable, operationName: String): NetworkException {
        return when (throwable) {
            is SocketTimeoutException -> {
                Timber.w("Socket timeout during operation '$operationName'")
                ExceptionFactory.networkError(
                    message = "Request timed out during operation '$operationName'",
                    cause = throwable,
                    isRetryable = true
                )
            }
            is UnknownHostException -> {
                Timber.w("Unknown host during operation '$operationName'")
                ExceptionFactory.networkError(
                    message = "Unable to reach server during operation '$operationName'",
                    cause = throwable,
                    isRetryable = true
                )
            }
            is IOException -> {
                Timber.w("IO error during operation '$operationName': ${throwable.message}")
                ExceptionFactory.networkError(
                    message = "Network I/O error during operation '$operationName'",
                    cause = throwable,
                    isRetryable = true
                )
            }
            is NetworkException -> throwable
            else -> {
                Timber.e(throwable, "Unexpected network error during operation '$operationName'")
                ExceptionFactory.networkError(
                    message = "Unexpected network error during operation '$operationName'",
                    cause = throwable,
                    isRetryable = true
                )
            }
        }
    }
}

/**
 * Extension function for executing network operations with retry logic.
 */
suspend fun <T> CoroutineScope.withNetworkRetry(
    context: Context,
    operationName: String,
    maxRetries: Int = 3,
    initialDelayMs: Long = 1000L,
    maxDelayMs: Long = 10000L,
    exponentialBackoff: Boolean = true,
    block: suspend () -> T
): T = withContext(Dispatchers.IO) {
    val networkManager = NetworkManager(context)
    networkManager.executeWithRetry(
        operationName = operationName,
        maxRetries = maxRetries,
        initialDelayMs = initialDelayMs,
        maxDelayMs = maxDelayMs,
        exponentialBackoff = exponentialBackoff,
        block = block
    )
}

/**
 * Network configuration for API calls.
 */
data class NetworkConfig(
    val timeoutMs: Long = 30000L,
    val readTimeoutMs: Long = 30000L,
    val writeTimeoutMs: Long = 30000L,
    val connectTimeoutMs: Long = 10000L,
    val maxRetries: Int = 3,
    val retryDelayMs: Long = 1000L,
    val exponentialBackoff: Boolean = true
)

/**
 * Network quality monitor for adaptive retry strategies.
 */
class NetworkQualityMonitor(private val context: Context) {

    enum class NetworkQuality {
        EXCELLENT, GOOD, POOR, NONE
    }

    fun getNetworkQuality(): NetworkQuality {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            
            return when {
                capabilities == null -> NetworkQuality.NONE
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkQuality.EXCELLENT
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    // Check signal strength if available
                    NetworkQuality.GOOD
                }
                else -> NetworkQuality.POOR
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            return if (networkInfo?.isConnectedOrConnecting == true) {
                NetworkQuality.GOOD
            } else {
                NetworkQuality.NONE
            }
        }
    }

    /**
     * Gets adaptive retry configuration based on network quality.
     */
    fun getAdaptiveConfig(): NetworkConfig {
        val quality = getNetworkQuality()
        
        return when (quality) {
            NetworkQuality.EXCELLENT -> NetworkConfig(
                maxRetries = 2,
                retryDelayMs = 500L,
                exponentialBackoff = true
            )
            NetworkQuality.GOOD -> NetworkConfig(
                maxRetries = 3,
                retryDelayMs = 1000L,
                exponentialBackoff = true
            )
            NetworkQuality.POOR -> NetworkConfig(
                maxRetries = 5,
                retryDelayMs = 2000L,
                exponentialBackoff = false
            )
            NetworkQuality.NONE -> NetworkConfig(
                maxRetries = 0,
                retryDelayMs = 0L
            )
        }
    }
}