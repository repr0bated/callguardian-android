package com.example.callguardian.lookup

import com.example.callguardian.model.LookupResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Queries the configured reverse-lookup sources in order and returns the first hit.
 * Results are cached in memory for an hour. Sources without API keys return null immediately,
 * so with no keys configured this costs nothing.
 */
@Singleton
class ReverseLookupManager @Inject constructor(
    private val sources: Set<@JvmSuppressWildcards ReverseLookupSource>,
    private val ioDispatcher: CoroutineDispatcher,
) {
    private data class Cached(val result: LookupResult, val at: Long)
    private val cache = ConcurrentHashMap<String, Cached>()

    suspend fun lookup(phoneNumber: String, budgetMs: Long = 2_000): LookupResult? = withContext(ioDispatcher) {
        cache[phoneNumber]?.takeIf { System.currentTimeMillis() - it.at < CACHE_MS }?.let { return@withContext it.result }
        withTimeoutOrNull(budgetMs) {
            for (source in sources.sortedBy { it.id }) {
                val r = runCatching { source.lookup(phoneNumber) }
                    .onFailure { Timber.w("Lookup via %s failed: %s", source.id, it.message) }
                    .getOrNull()
                if (r != null) {
                    cache[phoneNumber] = Cached(r, System.currentTimeMillis())
                    return@withTimeoutOrNull r
                }
            }
            null
        }
    }

    companion object { private const val CACHE_MS = 60 * 60 * 1000L }
}
