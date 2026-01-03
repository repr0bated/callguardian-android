package com.callguardian.app.data.cache

import com.callguardian.app.model.LookupResult
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory cache with TTL for reverse lookup results
 * Provides fast access to previously fetched data while managing memory usage
 */
@Singleton
class LookupCacheManager @Inject constructor() {

    private val cache = mutableMapOf<String, CacheEntry>()
    private val mutex = Mutex()

    /**
     * Cache entry with TTL and metadata
     */
    private data class CacheEntry(
        val result: LookupResult,
        val timestamp: Instant,
        val ttlSeconds: Long = DEFAULT_TTL_SECONDS,
        val hitCount: Int = 1
    ) {
        val isExpired: Boolean
            get() = Instant.now().isAfter(timestamp.plusSeconds(ttlSeconds))

        fun incrementHitCount(): CacheEntry = copy(hitCount = hitCount + 1)
    }

    suspend fun get(phoneNumber: String): LookupResult? = mutex.withLock {
        val entry = cache[phoneNumber]
        when {
            entry == null -> {
                Timber.d("Cache miss for $phoneNumber")
                null
            }
            entry.isExpired -> {
                Timber.d("Cache expired for $phoneNumber, removing")
                cache.remove(phoneNumber)
                null
            }
            else -> {
                Timber.d("Cache hit for $phoneNumber (hits: ${entry.hitCount})")
                cache[phoneNumber] = entry.incrementHitCount()
                entry.result
            }
        }
    }

    suspend fun put(phoneNumber: String, result: LookupResult, ttlSeconds: Long = DEFAULT_TTL_SECONDS) = mutex.withLock {
        val entry = CacheEntry(
            result = result,
            timestamp = Instant.now(),
            ttlSeconds = ttlSeconds,
            hitCount = 1
        )

        // Implement LRU-like behavior for memory management
        if (cache.size >= MAX_CACHE_SIZE) {
            evictLeastRecentlyUsed()
        }

        cache[phoneNumber] = entry
        Timber.d("Cached result for $phoneNumber (TTL: ${ttlSeconds}s)")
    }

    suspend fun invalidate(phoneNumber: String) = mutex.withLock {
        cache.remove(phoneNumber)
        Timber.d("Invalidated cache for $phoneNumber")
    }

    suspend fun clear() = mutex.withLock {
        cache.clear()
        Timber.d("Cleared entire cache")
    }

    suspend fun getStats(): CacheStats = mutex.withLock {
        val now = Instant.now()
        val activeEntries = cache.values.filterNot { it.isExpired }

        CacheStats(
            totalEntries = cache.size,
            activeEntries = activeEntries.size,
            totalHits = activeEntries.sumOf { it.hitCount },
            averageHitCount = if (activeEntries.isNotEmpty()) {
                activeEntries.sumOf { it.hitCount.toDouble() } / activeEntries.size
            } else 0.0,
            oldestEntryAgeMinutes = cache.values.minOfOrNull { ChronoUnit.MINUTES.between(it.timestamp, now) } ?: 0,
            memoryUsageKB = estimateMemoryUsage()
        )
    }

    private fun evictLeastRecentlyUsed() {
        // Simple LRU: remove entries with lowest hit count first
        val entriesToRemove = cache.entries
            .sortedBy { it.value.hitCount }
            .take(MAX_CACHE_SIZE / 4) // Remove 25% of entries

        entriesToRemove.forEach { (key, _) ->
            cache.remove(key)
        }

        Timber.d("Evicted ${entriesToRemove.size} cache entries for memory management")
    }

    private fun estimateMemoryUsage(): Long {
        // Rough estimation: assume average 1KB per entry
        return cache.size * 1024L
    }

    companion object {
        private const val DEFAULT_TTL_SECONDS = 3600L * 24 * 7 // 7 days
        private const val MAX_CACHE_SIZE = 10000 // Maximum cache entries
    }
}

data class CacheStats(
    val totalEntries: Int,
    val activeEntries: Int,
    val totalHits: Int,
    val averageHitCount: Double,
    val oldestEntryAgeMinutes: Long,
    val memoryUsageKB: Long
)

