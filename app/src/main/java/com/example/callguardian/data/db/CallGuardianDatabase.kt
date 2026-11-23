package com.example.callguardian.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScreeningEventEntity::class, PhoneProfileEntity::class, ReceiptEntity::class], version = 3, exportSchema = true)
abstract class CallGuardianDatabase : RoomDatabase() {
    abstract fun events(): ScreeningEventDao
    abstract fun profiles(): PhoneProfileDao
    abstract fun receipts(): ReceiptDao
}
