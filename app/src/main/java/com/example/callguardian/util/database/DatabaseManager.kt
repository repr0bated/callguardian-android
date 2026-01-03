package com.example.callguardian.util.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.callguardian.data.db.CallGuardianDatabase
import com.example.callguardian.util.exceptions.DatabaseException
import com.example.callguardian.util.exceptions.ExceptionFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * Database manager for handling database operations with error handling and graceful degradation.
 */
class DatabaseManager(private val context: Context) {

    companion object {
        private const val DATABASE_NAME = "callguardian.db"
        private const val BACKUP_SUFFIX = ".backup"
        private const val CORRUPTION_MARKER = ".corrupted"
    }

    private var database: CallGuardianDatabase? = null
    private var isDatabaseCorrupted = false

    /**
     * Gets or creates the database instance with error handling.
     */
    suspend fun getDatabase(): CallGuardianDatabase {
        return database ?: withContext(Dispatchers.IO) {
            try {
                if (isDatabaseCorrupted) {
                    throw DatabaseException(
                        message = "Database is corrupted and cannot be recovered",
                        isDataLoss = true,
                        userMessage = "Database appears to be corrupted. Please restart the app or contact support."
                    )
                }

                val db = Room.databaseBuilder(
                    context.applicationContext,
                    CallGuardianDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: RoomDatabase) {
                            Timber.d("Database created")
                        }

                        override fun onOpen(db: RoomDatabase) {
                            Timber.d("Database opened")
                            checkDatabaseIntegrity()
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()

                database = db
                Timber.d("Database initialized successfully")
                db
            } catch (e: Exception) {
                handleDatabaseError(e, "database_initialization")
                throw e
            }
        }
    }

    /**
     * Checks database integrity and handles corruption.
     */
    private fun checkDatabaseIntegrity() {
        try {
            val dbFile = File(context.getDatabasePath(DATABASE_NAME).path)
            if (!dbFile.exists() || !dbFile.canRead()) {
                throw IOException("Database file is missing or unreadable")
            }

            // Try a simple query to verify database is functional
            val db = database ?: return
            db.openHelper.writableDatabase.version // This will throw if database is corrupted

            Timber.d("Database integrity check passed")
        } catch (e: Exception) {
            Timber.e(e, "Database integrity check failed")
            handleDatabaseCorruption(e)
        }
    }

    /**
     * Handles database corruption with recovery attempts.
     */
    private fun handleDatabaseCorruption(cause: Exception) {
        isDatabaseCorrupted = true
        
        try {
            val dbFile = File(context.getDatabasePath(DATABASE_NAME).path)
            val backupFile = File("${dbFile.absolutePath}$BACKUP_SUFFIX")
            
            // Try to create a backup
            if (dbFile.exists()) {
                dbFile.copyTo(backupFile, overwrite = true)
                Timber.w("Created backup of corrupted database")
            }
            
            // Mark original as corrupted
            val corruptedFile = File("${dbFile.absolutePath}$CORRUPTION_MARKER")
            dbFile.renameTo(corruptedFile)
            
            Timber.w("Database marked as corrupted, will attempt recovery on next startup")
        } catch (e: Exception) {
            Timber.e(e, "Failed to handle database corruption")
        }
    }

    /**
     * Attempts to recover from database corruption.
     */
    suspend fun attemptRecovery(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val dbFile = File(context.getDatabasePath(DATABASE_NAME).path)
                val corruptedFile = File("${dbFile.absolutePath}$CORRUPTION_MARKER")
                val backupFile = File("${dbFile.absolutePath}$BACKUP_SUFFIX")
                
                // Try to restore from backup
                if (backupFile.exists()) {
                    backupFile.copyTo(dbFile, overwrite = true)
                    corruptedFile.delete()
                    backupFile.delete()
                    
                    // Reset corruption flag and try to get database
                    isDatabaseCorrupted = false
                    database = null
                    
                    try {
                        getDatabase()
                        Timber.d("Database recovery successful")
                        return@withContext true
                    } catch (e: Exception) {
                        Timber.e(e, "Database recovery failed after restore from backup")
                        return@withContext false
                    }
                }
                
                Timber.w("No backup available for recovery")
                false
            } catch (e: Exception) {
                Timber.e(e, "Database recovery attempt failed")
                false
            }
        }
    }

    /**
     * Clears the database instance (for testing or recovery).
     */
    fun clearDatabaseInstance() {
        database = null
        isDatabaseCorrupted = false
    }

    /**
     * Handles database errors with appropriate logging and user feedback.
     */
    private fun handleDatabaseError(throwable: Throwable, operation: String): DatabaseException {
        return when (throwable) {
            is DatabaseException -> throwable
            is IOException -> {
                Timber.e(throwable, "IO error during database operation '$operation'")
                ExceptionFactory.databaseError(
                    message = "IO error during database operation '$operation'",
                    cause = throwable,
                    operation = operation,
                    isDataLoss = false
                )
            }
            is IllegalStateException -> {
                Timber.e(throwable, "Illegal state during database operation '$operation'")
                ExceptionFactory.databaseError(
                    message = "Database state error during operation '$operation'",
                    cause = throwable,
                    operation = operation,
                    isDataLoss = true
                )
            }
            else -> {
                Timber.e(throwable, "Unexpected database error during operation '$operation'")
                ExceptionFactory.databaseError(
                    message = "Unexpected database error during operation '$operation'",
                    cause = throwable,
                    operation = operation,
                    isDataLoss = false
                )
            }
        }
    }

    /**
     * Executes a database query with error handling and graceful degradation.
     */
    suspend fun <T> safeQuery(operation: String, block: suspend CallGuardianDatabase.() -> T): T? {
        return try {
            val database = getDatabase()
            val result = block(database)
            Timber.d("Database query '$operation' completed successfully")
            result
        } catch (e: Exception) {
            Timber.e(e, "Database query '$operation' failed")
            null
        }
    }

    /**
     * Executes a database transaction with error handling and graceful degradation.
     */
    suspend fun safeTransaction(operation: String, block: suspend CallGuardianDatabase.() -> Unit): Boolean {
        return try {
            val database = getDatabase()
            database.withTransaction {
                block(database)
            }
            Timber.d("Database transaction '$operation' completed successfully")
            true
        } catch (e: Exception) {
            Timber.e(e, "Database transaction '$operation' failed")
            false
        }
    }
}

/**
 * Extension function for safe database operations with error handling.
 */
suspend fun <T> CoroutineScope.safeDatabaseOperation(
    databaseManager: DatabaseManager,
    operation: String,
    block: suspend CallGuardianDatabase.() -> T
): T {
    return withContext(Dispatchers.IO) {
        try {
            val database = databaseManager.getDatabase()
            val result = block(database)
            Timber.d("Database operation '$operation' completed successfully")
            result
        } catch (e: Exception) {
            Timber.e(e, "Database operation '$operation' failed")
            throw e
        }
    }
}

/**
 * Database health monitor for monitoring database performance and issues.
 */
class DatabaseHealthMonitor(private val context: Context) {

    data class HealthStatus(
        val isHealthy: Boolean,
        val lastCheckTime: Long,
        val averageQueryTimeMs: Long,
        val errorCount: Int,
        val corruptionDetected: Boolean
    )

    private var healthStatus = HealthStatus(
        isHealthy = true,
        lastCheckTime = 0L,
        averageQueryTimeMs = 0L,
        errorCount = 0,
        corruptionDetected = false
    )

    /**
     * Performs a health check on the database.
     */
    suspend fun performHealthCheck(databaseManager: DatabaseManager): HealthStatus {
        return try {
            val startTime = System.currentTimeMillis()
            
            // Try to get database instance
            val database = databaseManager.getDatabase()
            
            // Perform a simple query to test functionality
            val testResult = database.phoneProfileDao().getAll().firstOrNull()
            
            val queryTime = System.currentTimeMillis() - startTime
            val isHealthy = queryTime < 5000L // Consider healthy if query completes in under 5 seconds
            
            healthStatus = healthStatus.copy(
                isHealthy = isHealthy,
                lastCheckTime = System.currentTimeMillis(),
                averageQueryTimeMs = if (healthStatus.averageQueryTimeMs == 0L) queryTime 
                    else (healthStatus.averageQueryTimeMs + queryTime) / 2,
                errorCount = 0
            )
            
            Timber.d("Database health check passed: healthy=$isHealthy, queryTime=${queryTime}ms")
            healthStatus
            
        } catch (e: Exception) {
            Timber.e(e, "Database health check failed")
            
            healthStatus = healthStatus.copy(
                isHealthy = false,
                lastCheckTime = System.currentTimeMillis(),
                errorCount = healthStatus.errorCount + 1,
                corruptionDetected = e is DatabaseException && e.isDataLoss == true
            )
            
            healthStatus
        }
    }

    /**
     * Gets the current health status.
     */
    fun getHealthStatus(): HealthStatus = healthStatus

    /**
     * Resets the health status (for testing or recovery).
     */
    fun resetHealthStatus() {
        healthStatus = healthStatus.copy(
            isHealthy = true,
            lastCheckTime = 0L,
            averageQueryTimeMs = 0L,
            errorCount = 0,
            corruptionDetected = false
        )
    }
}

/**
 * Database migration helper for handling schema changes safely.
 */
class DatabaseMigrationHelper {

    /**
     * Validates migration paths and provides rollback options.
     */
    fun validateMigration(currentVersion: Int, targetVersion: Int): Boolean {
        // Simple validation - in a real app, you'd have more sophisticated logic
        return targetVersion > currentVersion
    }

    /**
     * Creates a backup before migration.
     */
    fun createBackup(context: Context, databaseName: String): Boolean {
        return try {
            val dbFile = context.getDatabasePath(databaseName)
            val backupFile = File("${dbFile.absolutePath}.backup.${System.currentTimeMillis()}")
            dbFile.copyTo(backupFile, overwrite = true)
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to create database backup")
            false
        }
    }
}