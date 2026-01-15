package com.example.callguardian.lookup;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B2\b\u0007\u0012\u0011\u0010\u0002\u001a\r\u0012\t\u0012\u00070\u0004\u00a2\u0006\u0002\b\u00050\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u000eH\u0082@\u00a2\u0006\u0002\u0010\u000fJ\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0082@\u00a2\u0006\u0002\u0010\u0014J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0082@\u00a2\u0006\u0002\u0010\u0014J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0082@\u00a2\u0006\u0002\u0010\u0014R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0002\u001a\r\u0012\t\u0012\u00070\u0004\u00a2\u0006\u0002\b\u00050\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/example/callguardian/lookup/ReverseLookupManager;", "", "sources", "", "Lcom/example/callguardian/lookup/ReverseLookupSource;", "Lkotlin/jvm/JvmSuppressWildcards;", "cacheManager", "Lcom/example/callguardian/data/cache/LookupCacheManager;", "healthMonitor", "Lcom/example/callguardian/lookup/ServiceHealthMonitor;", "ioDispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "(Ljava/util/Set;Lcom/example/callguardian/data/cache/LookupCacheManager;Lcom/example/callguardian/lookup/ServiceHealthMonitor;Lkotlinx/coroutines/CoroutineDispatcher;)V", "getHealthySourcesOrderedByPerformance", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "lookup", "Lcom/example/callguardian/model/LookupResult;", "phoneNumber", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "lookupParallel", "lookupSequential", "lookupWithFallback", "app_debug"})
public final class ReverseLookupManager {
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<com.example.callguardian.lookup.ReverseLookupSource> sources = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.data.cache.LookupCacheManager cacheManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.lookup.ServiceHealthMonitor healthMonitor = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineDispatcher ioDispatcher = null;
    
    @javax.inject.Inject()
    public ReverseLookupManager(@org.jetbrains.annotations.NotNull()
    java.util.Set<com.example.callguardian.lookup.ReverseLookupSource> sources, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.cache.LookupCacheManager cacheManager, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.lookup.ServiceHealthMonitor healthMonitor, @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineDispatcher ioDispatcher) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object lookup(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult> $completion) {
        return null;
    }
    
    private final java.lang.Object lookupWithFallback(java.lang.String phoneNumber, kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult> $completion) {
        return null;
    }
    
    private final java.lang.Object lookupParallel(java.lang.String phoneNumber, kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult> $completion) {
        return null;
    }
    
    private final java.lang.Object lookupSequential(java.lang.String phoneNumber, kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult> $completion) {
        return null;
    }
    
    private final java.lang.Object getHealthySourcesOrderedByPerformance(kotlin.coroutines.Continuation<? super java.util.List<? extends com.example.callguardian.lookup.ReverseLookupSource>> $completion) {
        return null;
    }
}