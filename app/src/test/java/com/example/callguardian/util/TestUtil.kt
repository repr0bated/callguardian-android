package com.example.callguardian.util

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.callguardian.data.db.CallGuardianDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Test rule for managing test database lifecycle and coroutine dispatchers
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestDatabaseRule(
    private val testDispatcher: TestDispatcher
) : TestWatcher() {

    private lateinit var _database: CallGuardianDatabase
    val database: CallGuardianDatabase
        get() = _database

    override fun starting(description: Description) {
        super.starting(description)
        
        // Set test dispatcher as main dispatcher
        Dispatchers.setMain(testDispatcher)
        
        // Create in-memory database
        val context = ApplicationProvider.getApplicationContext<Context>()
        _database = Room.inMemoryDatabaseBuilder(
            context,
            CallGuardianDatabase::class.java
        ).build()
    }

    override fun finished(description: Description) {
        super.finished(description)
        
        // Clean up database
        _database.close()
        
        // Reset main dispatcher
        Dispatchers.resetMain()
    }
}

/**
 * Test rule for managing Hilt test components
 */
class HiltTestRule : TestWatcher() {
    
    override fun starting(description: Description) {
        super.starting(description)
        // Hilt test setup would go here if needed
    }
    
    override fun finished(description: Description) {
        super.finished(description)
        // Hilt test cleanup would go here if needed
    }
}

/**
 * Test utilities for common testing operations
 */
object TestUtil {
    
    /**
     * Waits for a condition to be true with a timeout
     */
    fun <T> waitForCondition(
        timeoutMs: Long = 5000L,
        condition: () -> T?
    ): T {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            condition()?.let { return it }
            Thread.sleep(100)
        }
        throw AssertionError("Condition not met within $timeoutMs ms")
    }
    
    /**
     * Creates a test coroutine dispatcher for testing
     */
    fun createTestDispatcher(): TestDispatcher {
        return kotlinx.coroutines.test.StandardTestDispatcher()
    }
}