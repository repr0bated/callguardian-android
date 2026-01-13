package com.example.callguardian.lookup

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Performance optimization system for reverse lookup operations
 * Implements rate limiting, request batching, and intelligent retry strategies
 */
@Singleton
class PerformanceOptimizer @Inject constructor(
    private val sources: Set<@JvmSuppressWildcards ReverseLookupSource>
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutex = Mutex()
    private val rateLimiters = ConcurrentHashMap<String, RateLimiter>()
    private val requestQueue = ConcurrentHashMap<String, RequestBatch>()
    private val retryMetrics = ConcurrentHashMap<String, RetryMetrics>()

    init {
        // Initialize rate limiters for each source
        sources.forEach { source ->
            rateLimiters[source.id] = RateLimiter()
        }

        // Start request batching processor
        scope.launch {
            while (true) {
                processRequestBatches()
                delay(BATCH_PROCESSING_INTERVAL_MS)
            }
        }
    }

    /**
     * Executes a lookup with performance optimizations
     */
    suspend fun executeOptimizedLookup(
        phoneNumber: String,
        source: ReverseLookupSource,
        lookupFunction: suspend () -> com.example.callguardian.model.LookupResult?
    ): com.example.callguardian.model.LookupResult? {
        val sourceId = source.id

        // Check rate limits
        if (rateLimiters[sourceId]?.canProceed() == false) {
            Timber.d("Rate limit exceeded for $sourceId, queuing request")
            return queueRequest(phoneNumber, source, lookupFunction)
        }

        // Execute with retry logic
        return executeWithRetry(phoneNumber, sourceId, lookupFunction)
    }

    /**
     * Gets current performance metrics
     */
    suspend fun getPerformanceMetrics(): PerformanceMetrics = mutex.withLock {
        PerformanceMetrics(
            totalRequests = sources.sumOf { source ->
                retryMetrics[source.id]?.totalAttempts ?: 0L
            },
            successfulRequests = sources.sumOf { source ->
                retryMetrics[source.id]?.successfulAttempts ?: 0L
            },
            failedRequests = sources.sumOf { source ->
                retryMetrics[source.id]?.failedAttempts ?: 0L
            },
            averageResponseTimeMs = calculateAverageResponseTime(),
            rateLimitHits = sources.sumOf { source ->
                rateLimiters[source.id]?.getHits() ?: 0L
            },
            queuedRequests = requestQueue.size
        )
    }

    private suspend fun executeWithRetry(
        phoneNumber: String,
        sourceId: String,
        lookupFunction: suspend () -> com.example.callguardian.model.LookupResult?
    ): com.example.callguardian.model.LookupResult? {
        val metrics = retryMetrics.getOrPut(sourceId) { RetryMetrics() }
        val startTime = System.currentTimeMillis()

        repeat(MAX_RETRY_ATTEMPTS) { attempt ->
            try {
                val result = withTimeoutOrNull(REQUEST_TIMEOUT_MS) {
                    lookupFunction()
                }

                if (result != null) {
                    val responseTime = System.currentTimeMillis() - startTime
                    recordSuccess(sourceId, metrics, responseTime)
                    return result
                } else {
                    recordFailure(sourceId, metrics)
                    if (attempt < MAX_RETRY_ATTEMPTS - 1) {
                        delay(getRetryDelay(attempt))
                    }
                }
            } catch (e: Exception) {
                recordFailure(sourceId, metrics)
                Timber.w(e, "Lookup attempt $attempt failed for $sourceId")
                if (attempt < MAX_RETRY_ATTEMPTS - 1) {
                    delay(getRetryDelay(attempt))
                }
            }
        }

        recordFinalFailure(sourceId, metrics)
        return null
    }

    private suspend fun queueRequest(
        phoneNumber: String,
        source: ReverseLookupSource,
        lookupFunction: suspend () -> com.example.callguardian.model.LookupResult?
    ): com.example.callguardian.model.LookupResult? {
        val batch = requestQueue.getOrPut(phoneNumber) { RequestBatch(phoneNumber) }
        batch.addRequest(source, lookupFunction)

        // If batch is full, process immediately
        if (batch.requests.size >= MAX_BATCH_SIZE) {
            return processBatch(phoneNumber)
        }

        return null // Will be processed asynchronously
    }

    private suspend fun processRequestBatches() {
        val batchesToProcess = requestQueue.values.filter { it.requests.isNotEmpty() }
        batchesToProcess.forEach { batch ->
            processBatch(batch.phoneNumber)
        }
    }

    private suspend fun processBatch(phoneNumber: String): com.example.callguardian.model.LookupResult? {
        val batch = requestQueue[phoneNumber] ?: return null

        try {
            // Execute all requests in the batch
            val results = batch.requests.mapNotNull { (source, function) ->
                executeWithRetry(phoneNumber, source.id, function)
            }

            // Return first successful result
            val result = results.firstOrNull()

            // Clean up batch
            requestQueue.remove(phoneNumber)

            return result
        } catch (e: Exception) {
            Timber.w(e, "Failed to process batch for $phoneNumber")
            requestQueue.remove(phoneNumber)
            return null
        }
    }

    private fun getRetryDelay(attempt: Int): Long {
        // Exponential backoff with jitter
        val baseDelay = (1L shl attempt) * 1000L // 1s, 2s, 4s, 8s
        val jitter = (0..500).random() // Add up to 500ms jitter
        return baseDelay + jitter
    }

    private suspend fun recordSuccess(sourceId: String, metrics: RetryMetrics, responseTime: Long) = mutex.withLock {
        retryMetrics[sourceId] = metrics.recordSuccess(responseTime)
    }

    private suspend fun recordFailure(sourceId: String, metrics: RetryMetrics) = mutex.withLock {
        retryMetrics[sourceId] = metrics.recordFailure()
    }

    private suspend fun recordFinalFailure(sourceId: String, metrics: RetryMetrics) = mutex.withLock {
        retryMetrics[sourceId] = metrics.recordFinalFailure()
    }

    private fun calculateAverageResponseTime(): Double {
        val allMetrics = retryMetrics.values
        if (allMetrics.isEmpty()) return 0.0

        val totalResponseTime = allMetrics.sumOf { it.totalResponseTimeMs }
        val totalSuccesses = allMetrics.sumOf { it.successfulAttempts }

        return if (totalSuccesses > 0) totalResponseTime.toDouble() / totalSuccesses else 0.0
    }

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3
        private const val REQUEST_TIMEOUT_MS = 5000L
        private const val MAX_BATCH_SIZE = 3
        private const val BATCH_PROCESSING_INTERVAL_MS = 1000L
    }
}

/**
 * Rate limiter implementation using token bucket algorithm
 */
class RateLimiter {
    private val mutex = Mutex()
    private val tokens = AtomicInteger(MAX_TOKENS)
    private val lastRefill = AtomicLong(System.currentTimeMillis())
    private val hits = AtomicLong(0)

    fun getHits(): Long = hits.get()

    suspend fun canProceed(): Boolean = mutex.withLock {
        refillTokens()
        if (tokens.get() > 0) {
            tokens.decrementAndGet()
            true
        } else {
            hits.incrementAndGet()
            false
        }
    }

    private fun refillTokens() {
        val now = System.currentTimeMillis()
        val timePassed = now - lastRefill.get()

        if (timePassed >= REFILL_INTERVAL_MS) {
            val tokensToAdd = (timePassed / REFILL_INTERVAL_MS) * TOKENS_PER_INTERVAL
            val newTokenCount = (tokens.get().toLong() + tokensToAdd).coerceAtMost(MAX_TOKENS.toLong()).toInt()
            tokens.set(newTokenCount)
            lastRefill.set(now)
        }
    }

    companion object {
        private const val MAX_TOKENS = 10
        private const val REFILL_INTERVAL_MS = 1000L // 1 second
        private const val TOKENS_PER_INTERVAL = 2
    }
}

/**
 * Batch of requests for a phone number
 */
data class RequestBatch(
    val phoneNumber: String,
    val requests: MutableMap<ReverseLookupSource, suspend () -> com.example.callguardian.model.LookupResult?> = mutableMapOf()
) {
    fun addRequest(source: ReverseLookupSource, function: suspend () -> com.example.callguardian.model.LookupResult?) {
        requests[source] = function
    }
}

/**
 * Retry metrics for a source
 */
data class RetryMetrics(
    val totalAttempts: Long = 0,
    val successfulAttempts: Long = 0,
    val failedAttempts: Long = 0,
    val totalResponseTimeMs: Long = 0,
    val lastFailureTime: Instant? = null
) {
    fun recordSuccess(responseTime: Long): RetryMetrics = copy(
        totalAttempts = totalAttempts + 1,
        successfulAttempts = successfulAttempts + 1,
        totalResponseTimeMs = totalResponseTimeMs + responseTime
    )

    fun recordFailure(): RetryMetrics = copy(
        totalAttempts = totalAttempts + 1,
        failedAttempts = failedAttempts + 1,
        lastFailureTime = Instant.now()
    )

    fun recordFinalFailure(): RetryMetrics = copy(
        totalAttempts = totalAttempts + 1,
        failedAttempts = failedAttempts + 1,
        lastFailureTime = Instant.now()
    )
}

/**
 * Overall performance metrics
 */
data class PerformanceMetrics(
    val totalRequests: Long,
    val successfulRequests: Long,
    val failedRequests: Long,
    val averageResponseTimeMs: Double,
    val rateLimitHits: Long,
    val queuedRequests: Int
) {
    val successRate: Double
        get() = if (totalRequests > 0) successfulRequests.toDouble() / totalRequests else 0.0
}
