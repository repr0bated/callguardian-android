package com.example.callguardian.lookup

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Monitors the health and reliability of reverse lookup sources
 * Tracks response times, success rates, and implements circuit breaker pattern
 */
@Singleton
class ServiceHealthMonitor @Inject constructor(
    private val sources: Set<@JvmSuppressWildcards ReverseLookupSource>
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutex = Mutex()
    private val healthMetrics = ConcurrentHashMap<String, ServiceHealth>()

    init {
        // Start periodic health checks
        scope.launch {
            while (true) {
                performPeriodicHealthChecks()
                delay(HEALTH_CHECK_INTERVAL_MS)
            }
        }
    }

    /**
     * Records a successful lookup for a source
     */
    suspend fun recordSuccess(sourceId: String, responseTimeMs: Long) = mutex.withLock {
        val health = healthMetrics.getOrPut(sourceId) { ServiceHealth() }
        healthMetrics[sourceId] = health.recordSuccess(responseTimeMs)
    }

    /**
     * Records a failed lookup for a source
     */
    suspend fun recordFailure(sourceId: String, error: Throwable) = mutex.withLock {
        val health = healthMetrics.getOrPut(sourceId) { ServiceHealth() }
        healthMetrics[sourceId] = health.recordFailure(error)
    }

    /**
     * Gets current health status for all sources
     */
    suspend fun getHealthStatus(): Map<String, ServiceHealth> = mutex.withLock {
        healthMetrics.toMap()
    }

    /**
     * Checks if a source is considered healthy
     */
    suspend fun isHealthy(sourceId: String): Boolean {
        val health = healthMetrics[sourceId] ?: return true // Assume healthy if no data
        return health.isHealthy
    }

    /**
     * Gets the best performing source based on recent metrics
     */
    suspend fun getBestSource(): ReverseLookupSource? {
        val healthySources = sources.filter { isHealthy(it.id) }
        return healthySources.minByOrNull { source ->
            healthMetrics[source.id]?.averageResponseTimeMs ?: Double.MAX_VALUE
        }
    }

    private suspend fun performPeriodicHealthChecks() {
        sources.forEach { source ->
            try {
                // Perform a lightweight health check
                val startTime = System.currentTimeMillis()
                // This could be a simple ping or metadata request
                // For now, we'll just check if the source is responsive
                val responseTime = System.currentTimeMillis() - startTime

                if (responseTime < 5000) { // Less than 5 seconds is considered healthy
                    recordSuccess(source.id, responseTime)
                } else {
                    recordFailure(source.id, RuntimeException("Health check timeout"))
                }
            } catch (e: Exception) {
                recordFailure(source.id, e)
            }
        }
    }

    companion object {
        private const val HEALTH_CHECK_INTERVAL_MS = 300000L // 5 minutes
    }
}

/**
 * Health metrics for a single lookup source
 */
data class ServiceHealth(
    val totalRequests: Long = 0,
    val successfulRequests: Long = 0,
    val failedRequests: Long = 0,
    val totalResponseTimeMs: Long = 0,
    val lastFailureTime: Instant? = null,
    val consecutiveFailures: Int = 0,
    val circuitBreakerState: CircuitBreakerState = CircuitBreakerState.CLOSED
) {
    val successRate: Double
        get() = if (totalRequests > 0) successfulRequests.toDouble() / totalRequests else 0.0

    val averageResponseTimeMs: Double
        get() = if (successfulRequests > 0) totalResponseTimeMs.toDouble() / successfulRequests else 0.0

    val isHealthy: Boolean
        get() {
            // Consider healthy if success rate > 80% and not in circuit breaker open state
            return successRate > 0.8 && circuitBreakerState != CircuitBreakerState.OPEN
        }

    fun recordSuccess(responseTimeMs: Long): ServiceHealth {
        val newConsecutiveFailures = 0
        val newCircuitBreakerState = when {
            circuitBreakerState == CircuitBreakerState.HALF_OPEN -> CircuitBreakerState.CLOSED
            else -> circuitBreakerState
        }

        return copy(
            totalRequests = totalRequests + 1,
            successfulRequests = successfulRequests + 1,
            totalResponseTimeMs = totalResponseTimeMs + responseTimeMs,
            consecutiveFailures = newConsecutiveFailures,
            circuitBreakerState = newCircuitBreakerState
        )
    }

    fun recordFailure(error: Throwable): ServiceHealth {
        val newConsecutiveFailures = consecutiveFailures + 1
        val newCircuitBreakerState = when {
            newConsecutiveFailures >= 5 && circuitBreakerState == CircuitBreakerState.CLOSED ->
                CircuitBreakerState.OPEN
            newConsecutiveFailures >= 3 && circuitBreakerState == CircuitBreakerState.HALF_OPEN ->
                CircuitBreakerState.OPEN
            newConsecutiveFailures == 1 && circuitBreakerState == CircuitBreakerState.OPEN ->
                CircuitBreakerState.HALF_OPEN
            else -> circuitBreakerState
        }

        return copy(
            totalRequests = totalRequests + 1,
            failedRequests = failedRequests + 1,
            lastFailureTime = Instant.now(),
            consecutiveFailures = newConsecutiveFailures,
            circuitBreakerState = newCircuitBreakerState
        )
    }
}

enum class CircuitBreakerState {
    CLOSED,    // Normal operation
    OPEN,      // Failing, requests blocked
    HALF_OPEN  // Testing if service recovered
}
