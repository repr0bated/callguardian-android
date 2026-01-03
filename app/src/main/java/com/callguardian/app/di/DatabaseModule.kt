package com.callguardian.app.di

import android.content.Context
import androidx.room.Room
import com.callguardian.app.data.db.CallGuardianDatabase
import com.callguardian.app.data.db.PhoneInteractionDao
import com.callguardian.app.data.db.PhoneProfileDao
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
        Room.databaseBuilder(
            context,
            CallGuardianDatabase::class.java,
            "call_guardian.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providePhoneProfileDao(db: CallGuardianDatabase): PhoneProfileDao = db.phoneProfileDao()

    @Provides
    fun providePhoneInteractionDao(db: CallGuardianDatabase): PhoneInteractionDao = db.phoneInteractionDao()
}
