package com.example.callguardian.di

import com.example.callguardian.data.settings.EncryptedPreferences
import com.example.callguardian.lookup.ReverseLookupSource
import com.example.callguardian.lookup.sources.AbstractApiSource
import com.example.callguardian.lookup.sources.CustomEndpointLookupSource
import com.example.callguardian.lookup.sources.NumLookupApiSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class LookupModule {

    @Binds
    @IntoSet
    abstract fun bindNumLookupSource(source: NumLookupApiSource): ReverseLookupSource

    @Binds
    @IntoSet
    abstract fun bindCustomEndpointSource(source: CustomEndpointLookupSource): ReverseLookupSource

    @Binds
    @IntoSet
    abstract fun bindAbstractApiSource(source: AbstractApiSource): ReverseLookupSource
}
