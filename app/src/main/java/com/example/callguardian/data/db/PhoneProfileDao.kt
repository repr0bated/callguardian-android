package com.example.callguardian.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PhoneProfileDao {

    @Query("SELECT * FROM phone_profiles WHERE phone_number = :phoneNumber")
    suspend fun getByNumber(phoneNumber: String): PhoneProfileEntity?

    @Query("SELECT * FROM phone_profiles WHERE phone_number = :phoneNumber")
    fun observeByNumber(phoneNumber: String): Flow<PhoneProfileEntity?>

    @Query("SELECT * FROM phone_profiles ORDER BY last_lookup_at DESC")
    fun observeAll(): Flow<List<PhoneProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: PhoneProfileEntity)

    @Update
    suspend fun update(profile: PhoneProfileEntity)

    @Query("UPDATE phone_profiles SET block_mode = :blockMode WHERE phone_number = :phoneNumber")
    suspend fun updateBlockMode(phoneNumber: String, blockMode: String)

    @Query("UPDATE phone_profiles SET contact_id = :contactId, is_existing_contact = :isExistingContact WHERE phone_number = :phoneNumber")
    suspend fun updateContactInfo(phoneNumber: String, contactId: Long?, isExistingContact: Boolean)
}
