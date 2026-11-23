package com.example.callguardian.di

import android.content.Context
import com.example.callguardian.methods.CapabilityPolicy
import com.example.callguardian.methods.MethodRegistry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MethodsModule {
    @Provides
    @Singleton
    fun registry(@ApplicationContext context: Context): MethodRegistry =
        MethodRegistry { path -> context.assets.open("schemas/$path").bufferedReader().use { it.readText() } }

    @Provides
    @Singleton
    fun policy(registry: MethodRegistry): CapabilityPolicy = CapabilityPolicy(registry)
}
