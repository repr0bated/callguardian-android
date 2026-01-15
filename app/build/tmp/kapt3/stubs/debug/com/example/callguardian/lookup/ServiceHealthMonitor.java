package com.example.callguardian.lookup;

/**
 * Monitors the health and reliability of reverse lookup sources
 * Tracks response times, success rates, and implements circuit breaker pattern
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\b\u0007\u0018\u0000 !2\u00020\u0001:\u0001!B\u001a\b\u0007\u0012\u0011\u0010\u0002\u001a\r\u0012\t\u0012\u00070\u0004\u00a2\u0006\u0002\b\u00050\u0003\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0004H\u0086@\u00a2\u0006\u0002\u0010\u0010J\u001a\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010\u0016J\u000e\u0010\u0017\u001a\u00020\u0018H\u0082@\u00a2\u0006\u0002\u0010\u0010J\u001e\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u001bH\u0086@\u00a2\u0006\u0002\u0010\u001cJ\u001e\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\u001fH\u0086@\u00a2\u0006\u0002\u0010 R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0002\u001a\r\u0012\t\u0012\u00070\u0004\u00a2\u0006\u0002\b\u00050\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/example/callguardian/lookup/ServiceHealthMonitor;", "", "sources", "", "Lcom/example/callguardian/lookup/ReverseLookupSource;", "Lkotlin/jvm/JvmSuppressWildcards;", "(Ljava/util/Set;)V", "healthMetrics", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/example/callguardian/lookup/ServiceHealth;", "mutex", "Lkotlinx/coroutines/sync/Mutex;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "getBestSource", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHealthStatus", "", "isHealthy", "", "sourceId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "performPeriodicHealthChecks", "", "recordFailure", "error", "", "(Ljava/lang/String;Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recordSuccess", "responseTimeMs", "", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class ServiceHealthMonitor {
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<com.example.callguardian.lookup.ReverseLookupSource> sources = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.sync.Mutex mutex = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.example.callguardian.lookup.ServiceHealth> healthMetrics = null;
    private static final long HEALTH_CHECK_INTERVAL_MS = 300000L;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.callguardian.lookup.ServiceHealthMonitor.Companion Companion = null;
    
    @javax.inject.Inject()
    public ServiceHealthMonitor(@org.jetbrains.annotations.NotNull()
    java.util.Set<com.example.callguardian.lookup.ReverseLookupSource> sources) {
        super();
    }
    
    /**
     * Records a successful lookup for a source
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object recordSuccess(@org.jetbrains.annotations.NotNull()
    java.lang.String sourceId, long responseTimeMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Records a failed lookup for a source
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object recordFailure(@org.jetbrains.annotations.NotNull()
    java.lang.String sourceId, @org.jetbrains.annotations.NotNull()
    java.lang.Throwable error, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Gets current health status for all sources
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getHealthStatus(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.String, com.example.callguardian.lookup.ServiceHealth>> $completion) {
        return null;
    }
    
    /**
     * Checks if a source is considered healthy
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object isHealthy(@org.jetbrains.annotations.NotNull()
    java.lang.String sourceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Gets the best performing source based on recent metrics
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBestSource(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.lookup.ReverseLookupSource> $completion) {
        return null;
    }
    
    private final java.lang.Object performPeriodicHealthChecks(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/callguardian/lookup/ServiceHealthMonitor$Companion;", "", "()V", "HEALTH_CHECK_INTERVAL_MS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}