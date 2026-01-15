package com.example.callguardian.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\bH\'J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\nH\'\u00a8\u0006\u000b"}, d2 = {"Lcom/example/callguardian/di/LookupModule;", "", "()V", "bindAbstractApiSource", "Lcom/example/callguardian/lookup/ReverseLookupSource;", "source", "Lcom/example/callguardian/lookup/sources/AbstractApiSource;", "bindCustomEndpointSource", "Lcom/example/callguardian/lookup/sources/CustomEndpointLookupSource;", "bindNumLookupSource", "Lcom/example/callguardian/lookup/sources/NumLookupApiSource;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class LookupModule {
    
    public LookupModule() {
        super();
    }
    
    @dagger.Binds()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.callguardian.lookup.ReverseLookupSource bindNumLookupSource(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.lookup.sources.NumLookupApiSource source);
    
    @dagger.Binds()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.callguardian.lookup.ReverseLookupSource bindCustomEndpointSource(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.lookup.sources.CustomEndpointLookupSource source);
    
    @dagger.Binds()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.callguardian.lookup.ReverseLookupSource bindAbstractApiSource(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.lookup.sources.AbstractApiSource source);
}