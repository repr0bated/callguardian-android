package com.example.callguardian.lookup

import com.example.callguardian.data.cache.DataFreshnessManager
import com.example.callguardian.data.cache.LookupCacheManager
import com.example.callguardian.model.LookupResult
import com.example.callguardian.util.TestDataFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PerformanceOptimizerTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var optimizer: PerformanceOptimizer
    
    // Mock dependencies
    private val mockCacheManager = mockk<LookupCacheManager>(relaxed = true)
    private val mockFreshnessManager = mockk<DataFreshnessManager>(relaxed = true)
    private val mockHealthMonitor = mockk<ServiceHealthMonitor>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        optimizer = PerformanceOptimizer(
            cacheManager = mockCacheManager,
            freshnessManager = mockFreshnessManager,
            healthMonitor = mockHealthMonitor,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `shouldUseCache returns true when data is fresh`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        
        coEvery { mockCacheManager.get(phoneNumber) } returns result
        coEvery { mockFreshnessManager.isDataFresh(phoneNumber) } returns true

        // When
        val shouldUseCache = optimizer.shouldUseCache(phoneNumber)

        // Then
        assertTrue(shouldUseCache)
        coVerify { mockCacheManager.get(phoneNumber) }
        coVerify { mockFreshnessManager.isDataFresh(phoneNumber) }
    }

    @Test
    fun `shouldUseCache returns false when data is not fresh`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        
        coEvery { mockCacheManager.get(phoneNumber) } returns result
        coEvery { mockFreshnessManager.isDataFresh(phoneNumber) } returns false

        // When
        val shouldUseCache = optimizer.shouldUseCache(phoneNumber)

        // Then
        assertTrue(!shouldUseCache)
        coVerify { mockCacheManager.get(phoneNumber) }
        coVerify { mockFreshnessManager.isDataFresh(phoneNumber) }
    }

    @Test
    fun `shouldUseCache returns false when no cached data exists`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        
        coEvery { mockCacheManager.get(phoneNumber) } returns null

        // When
        val shouldUseCache = optimizer.shouldUseCache(phoneNumber)

        // Then
        assertTrue(!shouldUseCache)
        coVerify { mockCacheManager.get(phoneNumber) }
        coVerify(exactly = 0) { mockFreshnessManager.isDataFresh(any()) }
    }

    @Test
    fun `shouldUseCache returns false when cache is disabled`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        
        coEvery { mockCacheManager.get(phoneNumber) } returns result
        coEvery { mockFreshnessManager.isDataFresh(phoneNumber) } returns true
        coEvery { mockCacheManager.isCacheEnabled() } returns false

        // When
        val shouldUseCache = optimizer.shouldUseCache(phoneNumber)

        // Then
        assertTrue(!shouldUseCache)
        coVerify { mockCacheManager.isCacheEnabled() }
    }

    @Test
    fun `shouldUseCache returns true when cache is enabled and data is fresh`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        
        coEvery { mockCacheManager.get(phoneNumber) } returns result
        coEvery { mockFreshnessManager.isDataFresh(phoneNumber) } returns true
        coEvery { mockCacheManager.isCacheEnabled() } returns true

        // When
        val shouldUseCache = optimizer.shouldUseCache(phoneNumber)

        // Then
        assertTrue(shouldUseCache)
        coVerify { mockCacheManager.isCacheEnabled() }
        coVerify { mockCacheManager.get(phoneNumber) }
        coVerify { mockFreshnessManager.isDataFresh(phoneNumber) }
    }

    @Test
    fun `shouldUseCache handles edge case with null result`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        
        coEvery { mockCacheManager.get(phoneNumber) } returns null
        coEvery { mockCacheManager.isCacheEnabled() } returns true

        // When
        val shouldUseCache = optimizer.shouldUseCache(phoneNumber)

        // Then
        assertTrue(!shouldUseCache)
        coVerify { mockCacheManager.get(phoneNumber) }
    }

    @Test
    fun `shouldUseCache is resilient to freshness manager errors`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        
        coEvery { mockCacheManager.get(phoneNumber) } returns result
        coEvery { mockFreshnessManager.isDataFresh(phoneNumber) } throws Exception("Freshness check failed")
        coEvery { mockCacheManager.isCacheEnabled() } returns true

        // When
        val shouldUseCache = optimizer.shouldUseCache(phoneNumber)

        // Then
        assertTrue(!shouldUseCache) // Should fail safe and not use cache
        coVerify { mockCacheManager.get(phoneNumber) }
        coVerify { mockFreshnessManager.isDataFresh(phoneNumber) }
    }

    @Test
    fun `shouldUseCache performance test with multiple calls`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        
        coEvery { mockCacheManager.get(phoneNumber) } returns result
        coEvery { mockFreshnessManager.isDataFresh(phoneNumber) } returns true
        coEvery { mockCacheManager.isCacheEnabled() } returns true

        // When
        repeat(10) {
            val shouldUseCache = optimizer.shouldUseCache(phoneNumber)
            assertTrue(shouldUseCache)
        }

        // Then
        coVerify(exactly = 10) { mockCacheManager.get(phoneNumber) }
        coVerify(exactly = 10) { mockFreshnessManager.isDataFresh(phoneNumber) }
    }
}