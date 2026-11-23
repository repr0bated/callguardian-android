package com.example.callguardian.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScreeningEventDao {
    @Insert suspend fun insert(e: ScreeningEventEntity): Long
    @Update suspend fun update(e: ScreeningEventEntity)
    @Query("SELECT * FROM screening_events WHERE id = :id") suspend fun get(id: Long): ScreeningEventEntity?
    @Query("SELECT * FROM screening_events WHERE id = :id") fun observe(id: Long): Flow<ScreeningEventEntity?>
    @Query("SELECT * FROM screening_events ORDER BY received_at DESC LIMIT :limit") fun observeRecent(limit: Int = 200): Flow<List<ScreeningEventEntity>>
    @Query("SELECT COUNT(*) FROM screening_events WHERE channel = 'CALL' AND number = :number AND received_at > :since")
    suspend fun countCallsSince(number: String, since: Long): Int
    @Query("SELECT * FROM screening_events WHERE ai_status = 'PENDING' ORDER BY received_at DESC LIMIT 10")
    suspend fun pendingAi(): List<ScreeningEventEntity>
    @Query("DELETE FROM screening_events WHERE id = :id") suspend fun delete(id: Long)
    @Query("DELETE FROM screening_events") suspend fun clear()
}

@Dao
interface PhoneProfileDao {
    @Query("SELECT * FROM phone_profiles WHERE phone_number = :n") suspend fun get(n: String): PhoneProfileEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(p: PhoneProfileEntity)
    @Query("SELECT * FROM phone_profiles WHERE block_mode != 'NONE' OR trusted = 1 ORDER BY updated_at DESC")
    fun observeListed(): Flow<List<PhoneProfileEntity>>
}

@Dao
interface ReceiptDao {
    @Insert suspend fun insert(r: ReceiptEntity): Long
    @Query("SELECT * FROM method_receipts ORDER BY created_at DESC LIMIT :limit") fun observeRecent(limit: Int = 300): Flow<List<ReceiptEntity>>
    @Query("SELECT * FROM method_receipts WHERE event_id = :eventId ORDER BY created_at DESC") fun observeForEvent(eventId: Long): Flow<List<ReceiptEntity>>
    @Query("SELECT * FROM method_receipts WHERE id = :id") fun observe(id: Long): Flow<ReceiptEntity?>
    @Query("DELETE FROM method_receipts") suspend fun clear()
}
