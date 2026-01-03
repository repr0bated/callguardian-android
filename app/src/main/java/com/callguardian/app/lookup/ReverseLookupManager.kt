package com.callguardian.app.lookup

import com.callguardian.app.data.cache.LookupCacheManager
import com.callguardian.app.model.LookupResult
import com.callguardian.app.util.exceptions.CacheException
import com.callguardian.app.util.exceptions.ExceptionFactory
import com.callguardian.app.util.exceptions.LookupException
import com.callguardian.app.util.exceptions.NetworkException
import com.callguardian.app.util.exceptions.ServiceException
import com.callguardian.app.util.handling.ErrorHandling
import com.callguardian.app.util.logging.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReverseLookupManager @Inject constructor(
    private val sources: Set<@JvmSuppressWildcards ReverseLookupSource>,
    private val cacheManager: LookupCacheManager,
    private val healthMonitor: ServiceHealthMonitor,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun lookup(phoneNumber: String): LookupResult? = withContext(ioDispatcher) {
        try {
            lookupWithFallback(phoneNumber)
        } catch (e: LookupException) {
            Logger.e(e, "Lookup failed for phone number: $phoneNumber")
            throw e
        } catch (e: Exception) {
            Logger.e(e, "Unexpected error during lookup for phone number: $phoneNumber")
            throw ExceptionFactory.createLookupException("Reverse lookup failed", e)
        }
    }

    private suspend fun lookupWithFallback(phoneNumber: String): LookupResult? {
        // Check cache first
        val cachedResult = runCatching { cacheManager.get(phoneNumber) }
            .onFailure {
                Logger.w(it, "Cache lookup failed for phone number: $phoneNumber")
                // Continue with network lookup even if cache fails
            }
            .getOrNull()
        
        if (cachedResult != null) {
            Logger.d("Returning cached result for $phoneNumber")
            return cachedResult
        }

        // Try parallel lookup first for speed
        val parallelResult = lookupParallel(phoneNumber)
        if (parallelResult != null) {
            // Cache successful result
            runCatching { cacheManager.put(phoneNumber, parallelResult) }
                .onFailure {
                    Logger.w(it, "Failed to cache lookup result for phone number: $phoneNumber")
                    // Continue even if caching fails
                }
            return parallelResult
        }

        // Fallback to sequential if parallel fails
        val sequentialResult = lookupSequential(phoneNumber)
        if (sequentialResult != null) {
            // Cache successful result
            runCatching { cacheManager.put(phoneNumber, sequentialResult) }
                .onFailure {
                    Logger.w(it, "Failed to cache lookup result for phone number: $phoneNumber")
                    // Continue even if caching fails
                }
            return sequentialResult
        }

        return null
    }

    private suspend fun lookupParallel(phoneNumber: String): LookupResult? = coroutineScope {
        // Get healthy sources ordered by performance
        val healthySources = getHealthySourcesOrderedByPerformance()

        val deferredResults = healthySources.map { source ->
            async {
                val startTime = System.currentTimeMillis()
                runCatching {
                    source.lookup(phoneNumber)
                }.onSuccess { result ->
                    val responseTime = System.currentTimeMillis() - startTime
                    healthMonitor.recordSuccess(source.id, responseTime)
                    Logger.d("Parallel lookup succeeded with ${source.id} in ${responseTime}ms")
                }.onFailure { error ->
                    val responseTime = System.currentTimeMillis() - startTime
                    healthMonitor.recordFailure(source.id, error)
                    Logger.w(error, "Parallel lookup failed for ${source.id}: ${error.message}")
                }.getOrNull()
            }
        }

        // Return first successful result
        deferredResults.forEach { deferred ->
            deferred.await()?.let { return@coroutineScope it }
        }
        null
    }

    private suspend fun lookupSequential(phoneNumber: String): LookupResult? {
        // Use health-aware source ordering for sequential fallback
        val healthySources = getHealthySourcesOrderedByPerformance()

        healthySources.forEach { source ->
            val startTime = System.currentTimeMillis()
            runCatching {
                source.lookup(phoneNumber)
            }.onSuccess { result ->
                val responseTime = System.currentTimeMillis() - startTime
                healthMonitor.recordSuccess(source.id, responseTime)
                Logger.d("Sequential lookup succeeded with ${source.id} in ${responseTime}ms")
                return result
            }.onFailure { error ->
                val responseTime = System.currentTimeMillis() - startTime
                healthMonitor.recordFailure(source.id, error)
                Logger.w(error, "Sequential lookup failed for ${source.id}: ${error.message}")
            }
        }
        return null
    }

    private suspend fun getHealthySourcesOrderedByPerformance(): List<ReverseLookupSource> {
        val healthySources = sources.filter { healthMonitor.isHealthy(it.id) }

        // If we have health metrics, order by performance
        val healthStatus = healthMonitor.getHealthStatus()
        return if (healthStatus.isNotEmpty()) {
            healthySources.sortedBy { source ->
                healthStatus[source.id]?.averageResponseTimeMs ?: Long.MAX_VALUE
            }
        } else {
            // Fallback to original order if no health data
            healthySources.toList()
        }
    }
}
