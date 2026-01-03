package com.example.callguardian

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.callguardian.data.db.CallGuardianDatabase
import com.example.callguardian.data.settings.EncryptedPreferences
import com.example.callguardian.data.settings.LookupPreferences
import com.example.callguardian.lookup.PerformanceOptimizer
import com.example.callguardian.lookup.ReverseLookupManager
import com.example.callguardian.lookup.ServiceHealthMonitor
import com.example.callguardian.lookup.sources.ReverseLookupSource
import com.example.callguardian.util.TestUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

/**
 * Base test configuration class that provides common test setup and utilities
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class TestConfiguration {
    
    protected val testDispatcher = StandardTestDispatcher()
    protected lateinit var context: Context
    protected lateinit var database: CallGuardianDatabase
    protected lateinit var encryptedPreferences: EncryptedPreferences
    protected lateinit var lookupPreferences: LookupPreferences
    protected lateinit var reverseLookupManager: ReverseLookupManager
    protected lateinit var performanceOptimizer: PerformanceOptimizer
    protected lateinit var healthMonitor: ServiceHealthMonitor
    
    @Before
    open fun setup() {
        context = ApplicationProvider.getApplicationContext()
        Dispatchers.setMain(testDispatcher)
        
        // Initialize database
        database = Room.inMemoryDatabaseBuilder(
            context,
            CallGuardianDatabase::class.java
        ).build()
        
        // Initialize preferences
        encryptedPreferences = EncryptedPreferences(context)
        lookupPreferences = LookupPreferences(context)
        
        // Initialize services
        healthMonitor = ServiceHealthMonitor()
        performanceOptimizer = PerformanceOptimizer(
            cacheManager = mockk(relaxed = true),
            freshnessManager = mockk(relaxed = true),
            healthMonitor = healthMonitor,
            ioDispatcher = testDispatcher
        )
        
        reverseLookupManager = ReverseLookupManager(
            sources = emptySet(),
            cacheManager = mockk(relaxed = true),
            healthMonitor = healthMonitor,
            ioDispatcher = testDispatcher
        )
    }
    
    @After
    open fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }
    
    /**
     * Helper method to create a test source for testing
     */
    protected fun createTestSource(
        id: String = "test-source",
        baseUrl: String = "https://test.example.com/",
        apiKey: String = "test-api-key"
    ): ReverseLookupSource {
        return object : ReverseLookupSource {
            override val id: String = id
            
            override suspend fun lookup(phoneNumber: String): com.example.callguardian.model.LookupResult? {
                return com.example.callguardian.model.LookupResult(
                    phoneNumber = phoneNumber,
                    displayName = "Test Contact",
                    carrier = "Test Carrier",
                    region = "Test Region",
                    lineType = "Mobile",
                    spamScore = 25,
                    tags = listOf("Test Tag"),
                    summary = "Test summary"
                )
            }
        }
    }
    
    /**
     * Helper method to wait for async operations
     */
    protected fun waitForAsyncOperation(timeoutMs: Long = 5000L, operation: () -> Unit) {
        TestUtil.waitForCondition(timeoutMs) {
            try {
                operation()
                true
            } catch (e: Exception) {
                null
            }
        }
    }
}