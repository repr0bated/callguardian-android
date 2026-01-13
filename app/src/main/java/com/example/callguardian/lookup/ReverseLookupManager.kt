package com.example.callguardian.lookup

import com.example.callguardian.data.cache.LookupCacheManager
import com.example.callguardian.model.LookupResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
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
        lookupWithFallback(phoneNumber)
    }

    private suspend fun lookupWithFallback(phoneNumber: String): LookupResult? {
        // Check cache first
        val cachedResult = cacheManager.get(phoneNumber)
        if (cachedResult != null) {
            Timber.d("Returning cached result for $phoneNumber")
            return cachedResult
        }

        // Try parallel lookup first for speed
        val parallelResult = lookupParallel(phoneNumber)
        if (parallelResult != null) {
            // Cache successful result
            cacheManager.put(phoneNumber, parallelResult)
            return parallelResult
        }

        // Fallback to sequential if parallel fails
        val sequentialResult = lookupSequential(phoneNumber)
        if (sequentialResult != null) {
            // Cache successful result
            cacheManager.put(phoneNumber, sequentialResult)
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
                }.onFailure { error ->
                    val responseTime = System.currentTimeMillis() - startTime
                    healthMonitor.recordFailure(source.id, error)
                    Timber.w("Parallel lookup failed for ${source.id}: ${error.message}")
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
                Timber.d("Sequential lookup succeeded with ${source.id}")
                return result
            }.onFailure { error ->
                val responseTime = System.currentTimeMillis() - startTime
                healthMonitor.recordFailure(source.id, error)
                Timber.w("Sequential lookup failed for ${source.id}: ${error.message}")
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
                healthStatus[source.id]?.averageResponseTimeMs ?: Double.MAX_VALUE
            }
        } else {
            // Fallback to original order if no health data
            healthySources.toList()
        }
    }
}
