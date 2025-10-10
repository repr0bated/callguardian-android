package com.example.callguardian.data.cache

import com.example.callguardian.data.db.PhoneProfileDao
import com.example.callguardian.data.db.PhoneProfileEntity
import com.example.callguardian.lookup.ReverseLookupManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages data freshness and implements background refresh strategies
 * Ensures lookup data remains current while optimizing for performance
 */
@Singleton
class DataFreshnessManager @Inject constructor(
    private val cacheManager: LookupCacheManager,
    private val profileDao: PhoneProfileDao,
    private val reverseLookupManager: ReverseLookupManager
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutex = Mutex()
    private val refreshQueue = mutableSetOf<String>()
    private val lastRefreshTimes = mutableMapOf<String, Instant>()

    init {
        // Start background refresh scheduler
        scope.launch {
            while (true) {
                performScheduledRefresh()
                delay(REFRESH_CHECK_INTERVAL_MS)
            }
        }
    }

    /**
     * Marks a phone number for refresh when data becomes stale
     */
    suspend fun scheduleRefresh(phoneNumber: String, priority: RefreshPriority = RefreshPriority.NORMAL) = mutex.withLock {
        refreshQueue.add(phoneNumber)
        Timber.d("Scheduled refresh for $phoneNumber with priority $priority")
    }

    /**
     * Gets the freshness status of cached data for a phone number
     */
    suspend fun getFreshnessStatus(phoneNumber: String): FreshnessStatus {
        val profile = profileDao.getByNumber(phoneNumber)
        val cacheAge = getCacheAge(phoneNumber)

        return when {
            profile == null -> FreshnessStatus.UNKNOWN
            cacheAge == null -> FreshnessStatus.STALE
            cacheAge < STALE_THRESHOLD_HOURS -> FreshnessStatus.FRESH
            cacheAge < EXPIRED_THRESHOLD_HOURS -> FreshnessStatus.STALE
            else -> FreshnessStatus.EXPIRED
        }
    }

    /**
     * Forces a refresh of data for a phone number
     */
    suspend fun refreshNow(phoneNumber: String): Boolean = mutex.withLock {
        try {
            Timber.d("Forcing refresh for $phoneNumber")
            val result = reverseLookupManager.lookup(phoneNumber)
            if (result != null) {
                cacheManager.put(phoneNumber, result, getOptimalTtl(phoneNumber))
                lastRefreshTimes[phoneNumber] = Instant.now()
                refreshQueue.remove(phoneNumber)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Timber.w(e, "Failed to refresh data for $phoneNumber")
            false
        }
    }

    private suspend fun performScheduledRefresh() {
        val now = Instant.now()

        // Get profiles that need refreshing
        val allProfiles = profileDao.observeAll().first()
        val profilesNeedingRefresh = allProfiles.filter { profile ->
            val lastRefresh = lastRefreshTimes[profile.phoneNumber]
            val needsRefresh = when {
                lastRefresh == null -> true
                ChronoUnit.HOURS.between(lastRefresh, now) > getOptimalRefreshInterval(profile) -> true
                else -> false
            }

            needsRefresh && profile.phoneNumber !in refreshQueue
        }

        // Refresh high-priority items first
        profilesNeedingRefresh
            .sortedByDescending { getRefreshPriority(it) }
            .take(MAX_CONCURRENT_REFRESHES)
            .forEach { profile ->
                scope.launch {
                    refreshNow(profile.phoneNumber)
                }
            }
    }

    private suspend fun getCacheAge(phoneNumber: String): Long? {
        // This would need to be implemented based on cache metadata
        // For now, we'll use profile update time as proxy
        val profile = profileDao.getByNumber(phoneNumber)
        return profile?.lastLookupAt?.let { ChronoUnit.HOURS.between(it, Instant.now()) }
    }

    private fun getOptimalTtl(phoneNumber: String): Long {
        // Adjust TTL based on usage patterns and data volatility
        return when {
            // High-frequency numbers get shorter TTL for freshness
            phoneNumber.startsWith("+1") -> 3600L * 4 // 4 hours for US numbers
            phoneNumber.startsWith("+44") -> 3600L * 6 // 6 hours for UK numbers
            else -> 3600L * 12 // 12 hours for international numbers
        }
    }

    private fun getOptimalRefreshInterval(profile: PhoneProfileEntity): Long {
        // More frequent refresh for high-priority numbers
        val priority = getRefreshPriority(profile)
        return when (priority) {
            RefreshPriority.HIGH -> 2L // 2 hours
            RefreshPriority.NORMAL -> 6L // 6 hours
            RefreshPriority.LOW -> 24L // 24 hours
        }
    }

    private fun getRefreshPriority(profile: PhoneProfileEntity): RefreshPriority {
        // Prioritize based on usage patterns and importance
        return when {
            profile.isExistingContact -> RefreshPriority.HIGH
            profile.blockMode != com.example.callguardian.model.BlockMode.NONE -> RefreshPriority.NORMAL
            else -> RefreshPriority.LOW
        }
    }

    companion object {
        private const val REFRESH_CHECK_INTERVAL_MS = 3600000L // 1 hour
        private const val MAX_CONCURRENT_REFRESHES = 5
        private const val STALE_THRESHOLD_HOURS = 6L
        private const val EXPIRED_THRESHOLD_HOURS = 24L
    }
}

enum class RefreshPriority {
    HIGH, NORMAL, LOW
}

enum class FreshnessStatus {
    FRESH, STALE, EXPIRED, UNKNOWN
}
