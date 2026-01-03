package com.callguardian.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        PhoneProfileEntity::class,
        PhoneInteractionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class CallGuardianDatabase : RoomDatabase() {
    abstract fun phoneProfileDao(): PhoneProfileDao
    abstract fun phoneInteractionDao(): PhoneInteractionDao
}
