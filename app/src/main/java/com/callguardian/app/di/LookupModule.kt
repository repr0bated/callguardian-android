package com.callguardian.app.di

import com.callguardian.app.data.settings.EncryptedPreferences
import com.callguardian.app.lookup.ReverseLookupSource
import com.callguardian.app.lookup.sources.AbstractApiSource
import com.callguardian.app.lookup.sources.CustomEndpointLookupSource
import com.callguardian.app.lookup.sources.NumLookupApiSource
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
