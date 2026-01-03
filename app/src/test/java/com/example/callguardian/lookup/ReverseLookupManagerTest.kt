package com.example.callguardian.lookup

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
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ReverseLookupManagerTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var manager: ReverseLookupManager
    
    // Mock dependencies
    private val mockSource1 = mockk<ReverseLookupSource>(relaxed = true)
    private val mockSource2 = mockk<ReverseLookupSource>(relaxed = true)
    private val mockCacheManager = mockk<LookupCacheManager>(relaxed = true)
    private val mockHealthMonitor = mockk<ServiceHealthMonitor>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Set up mock sources
        every { mockSource1.id } returns "source1"
        every { mockSource2.id } returns "source2"
        
        manager = ReverseLookupManager(
            sources = setOf(mockSource1, mockSource2),
            cacheManager = mockCacheManager,
            healthMonitor = mockHealthMonitor,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `lookup returns cached result when available`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val cachedResult = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        
        coEvery { mockCacheManager.get(phoneNumber) } returns cachedResult
        coEvery { mockHealthMonitor.isHealthy(any()) } returns true

        // When
        val result = manager.lookup(phoneNumber)

        // Then
        assertEquals(cachedResult, result)
        coVerify { mockCacheManager.get(phoneNumber) }
        coVerify(exactly = 0) { mockSource1.lookup(any()) }
        coVerify(exactly = 0) { mockSource2.lookup(any()) }
    }

    @Test
    fun `lookup returns first successful result from parallel sources`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result1 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber, displayName = "Source 1 Result")
        val result2 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber, displayName = "Source 2 Result")
        
        coEvery { mockCacheManager.get(phoneNumber) } returns null
        coEvery { mockHealthMonitor.isHealthy(any()) } returns true
        coEvery { mockSource1.lookup(phoneNumber) } returns result1
        coEvery { mockSource2.lookup(phoneNumber) } returns result2
        coEvery { mockCacheManager.put(phoneNumber, any()) } returns Unit
        coEvery { mockHealthMonitor.recordSuccess(any(), any()) } returns Unit

        // When
        val result = manager.lookup(phoneNumber)

        // Then
        assertEquals(result1, result)
        coVerify { mockCacheManager.put(phoneNumber, result1) }
        coVerify { mockHealthMonitor.recordSuccess("source1", any()) }
    }

    @Test
    fun `lookup falls back to sequential when parallel fails`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result2 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber, displayName = "Source 2 Result")
        
        coEvery { mockCacheManager.get(phoneNumber) } returns null
        coEvery { mockHealthMonitor.isHealthy(any()) } returns true
        coEvery { mockSource1.lookup(phoneNumber) } throws Exception("Source 1 failed")
        coEvery { mockSource2.lookup(phoneNumber) } returns result2
        coEvery { mockCacheManager.put(phoneNumber, any()) } returns Unit
        coEvery { mockHealthMonitor.recordSuccess(any(), any()) } returns Unit
        coEvery { mockHealthMonitor.recordFailure(any(), any()) } returns Unit

        // When
        val result = manager.lookup(phoneNumber)

        // Then
        assertEquals(result2, result)
        coVerify { mockCacheManager.put(phoneNumber, result2) }
        coVerify { mockHealthMonitor.recordSuccess("source2", any()) }
        coVerify { mockHealthMonitor.recordFailure("source1", any()) }
    }

    @Test
    fun `lookup returns null when all sources fail`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        
        coEvery { mockCacheManager.get(phoneNumber) } returns null
        coEvery { mockHealthMonitor.isHealthy(any()) } returns true
        coEvery { mockSource1.lookup(phoneNumber) } throws Exception("Source 1 failed")
        coEvery { mockSource2.lookup(phoneNumber) } throws Exception("Source 2 failed")
        coEvery { mockHealthMonitor.recordFailure(any(), any()) } returns Unit

        // When
        val result = manager.lookup(phoneNumber)

        // Then
        assertNull(result)
        coVerify { mockHealthMonitor.recordFailure("source1", any()) }
        coVerify { mockHealthMonitor.recordFailure("source2", any()) }
    }

    @Test
    fun `lookup skips unhealthy sources`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result2 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber, displayName = "Source 2 Result")
        
        coEvery { mockCacheManager.get(phoneNumber) } returns null
        coEvery { mockHealthMonitor.isHealthy("source1") } returns false
        coEvery { mockHealthMonitor.isHealthy("source2") } returns true
        coEvery { mockSource2.lookup(phoneNumber) } returns result2
        coEvery { mockCacheManager.put(phoneNumber, any()) } returns Unit
        coEvery { mockHealthMonitor.recordSuccess(any(), any()) } returns Unit

        // When
        val result = manager.lookup(phoneNumber)

        // Then
        assertEquals(result2, result)
        coVerify(exactly = 0) { mockSource1.lookup(any()) }
        coVerify { mockSource2.lookup(phoneNumber) }
    }

    @Test
    fun `lookup caches successful results`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        
        coEvery { mockCacheManager.get(phoneNumber) } returns null
        coEvery { mockHealthMonitor.isHealthy(any()) } returns true
        coEvery { mockSource1.lookup(phoneNumber) } returns result
        coEvery { mockCacheManager.put(phoneNumber, any()) } returns Unit
        coEvery { mockHealthMonitor.recordSuccess(any(), any()) } returns Unit

        // When
        manager.lookup(phoneNumber)
        val cachedResult = manager.lookup(phoneNumber)

        // Then
        assertEquals(result, cachedResult)
        coVerify { mockCacheManager.put(phoneNumber, result) }
        coVerify(exactly = 1) { mockSource1.lookup(phoneNumber) } // Only called once
    }

    @Test
    fun `lookup handles sources with different response times`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result1 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber, displayName = "Fast Source")
        val result2 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber, displayName = "Slow Source")
        
        coEvery { mockCacheManager.get(phoneNumber) } returns null
        coEvery { mockHealthMonitor.isHealthy(any()) } returns true
        coEvery { mockSource1.lookup(phoneNumber) } answers {
            kotlinx.coroutines.delay(10) // Simulate fast response
            result1
        }
        coEvery { mockSource2.lookup(phoneNumber) } answers {
            kotlinx.coroutines.delay(100) // Simulate slow response
            result2
        }
        coEvery { mockCacheManager.put(phoneNumber, any()) } returns Unit
        coEvery { mockHealthMonitor.recordSuccess(any(), any()) } returns Unit

        // When
        val result = manager.lookup(phoneNumber)

        // Then
        assertEquals(result1, result) // Should get the faster result
        coVerify { mockCacheManager.put(phoneNumber, result1) }
    }

    @Test
    fun `lookup orders sources by performance when health data available`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val result1 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber, displayName = "Source 1")
        val result2 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber, displayName = "Source 2")
        
        coEvery { mockCacheManager.get(phoneNumber) } returns null
        coEvery { mockHealthMonitor.isHealthy(any()) } returns true
        coEvery { mockHealthMonitor.getHealthStatus() } returns mapOf(
            "source1" to ServiceHealthMonitor.HealthMetrics(averageResponseTimeMs = 50),
            "source2" to ServiceHealthMonitor.HealthMetrics(averageResponseTimeMs = 100)
        )
        coEvery { mockSource1.lookup(phoneNumber) } returns result1
        coEvery { mockSource2.lookup(phoneNumber) } returns result2
        coEvery { mockCacheManager.put(phoneNumber, any()) } returns Unit
        coEvery { mockHealthMonitor.recordSuccess(any(), any()) } returns Unit

        // When
        val result = manager.lookup(phoneNumber)

        // Then
        assertEquals(result1, result) // Should prefer faster source
        coVerify { mockCacheManager.put(phoneNumber, result1) }
    }
}