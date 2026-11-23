package com.example.callguardian.di

import android.content.Context
import androidx.room.Room
import com.example.callguardian.data.db.CallGuardianDatabase
import com.example.callguardian.data.db.PhoneProfileDao
import com.example.callguardian.data.db.ScreeningEventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CallGuardianDatabase =
        Room.databaseBuilder(context, CallGuardianDatabase::class.java, "call_guardian.db")
            // v1/v2 schemas from the original app were never shipped to users.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides fun events(db: CallGuardianDatabase): ScreeningEventDao = db.events()
    @Provides fun profiles(db: CallGuardianDatabase): PhoneProfileDao = db.profiles()
    @Provides fun receipts(db: CallGuardianDatabase): com.example.callguardian.data.db.ReceiptDao = db.receipts()
}
