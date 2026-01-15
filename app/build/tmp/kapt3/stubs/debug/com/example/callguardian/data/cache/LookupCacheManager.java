package com.example.callguardian.data.cache;

/**
 * In-memory cache with TTL for reverse lookup results
 * Provides fast access to previously fetched data while managing memory usage
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u0000 \u001b2\u00020\u0001:\u0002\u001a\u001bB\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\rH\u0002J\b\u0010\u000e\u001a\u00020\nH\u0002J\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u0005H\u0086@\u00a2\u0006\u0002\u0010\u0012J(\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u00102\b\b\u0002\u0010\u0018\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u0019R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/example/callguardian/data/cache/LookupCacheManager;", "", "()V", "cache", "", "", "Lcom/example/callguardian/data/cache/LookupCacheManager$CacheEntry;", "mutex", "Lkotlinx/coroutines/sync/Mutex;", "clear", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "estimateMemoryUsage", "", "evictLeastRecentlyUsed", "get", "Lcom/example/callguardian/model/LookupResult;", "phoneNumber", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getStats", "Lcom/example/callguardian/data/cache/CacheStats;", "invalidate", "put", "result", "ttlSeconds", "(Ljava/lang/String;Lcom/example/callguardian/model/LookupResult;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "CacheEntry", "Companion", "app_debug"})
public final class LookupCacheManager {
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, com.example.callguardian.data.cache.LookupCacheManager.CacheEntry> cache = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.sync.Mutex mutex = null;
    private static final long DEFAULT_TTL_SECONDS = 604800L;
    private static final int MAX_CACHE_SIZE = 10000;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.callguardian.data.cache.LookupCacheManager.Companion Companion = null;
    
    @javax.inject.Inject()
    public LookupCacheManager() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object get(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object put(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.model.LookupResult result, long ttlSeconds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object invalidate(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clear(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getStats(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.data.cache.CacheStats> $completion) {
        return null;
    }
    
    private final void evictLeastRecentlyUsed() {
    }
    
    private final long estimateMemoryUsage() {
        return 0L;
    }
    
    /**
     * Cache entry with TTL and metadata
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\tH\u00c6\u0003J1\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u000e2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001d\u001a\u00020\tH\u00d6\u0001J\u0006\u0010\u001e\u001a\u00020\u0000J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006!"}, d2 = {"Lcom/example/callguardian/data/cache/LookupCacheManager$CacheEntry;", "", "result", "Lcom/example/callguardian/model/LookupResult;", "timestamp", "Ljava/time/Instant;", "ttlSeconds", "", "hitCount", "", "(Lcom/example/callguardian/model/LookupResult;Ljava/time/Instant;JI)V", "getHitCount", "()I", "isExpired", "", "()Z", "getResult", "()Lcom/example/callguardian/model/LookupResult;", "getTimestamp", "()Ljava/time/Instant;", "getTtlSeconds", "()J", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "incrementHitCount", "toString", "", "app_debug"})
    static final class CacheEntry {
        @org.jetbrains.annotations.NotNull()
        private final com.example.callguardian.model.LookupResult result = null;
        @org.jetbrains.annotations.NotNull()
        private final java.time.Instant timestamp = null;
        private final long ttlSeconds = 0L;
        private final int hitCount = 0;
        
        public CacheEntry(@org.jetbrains.annotations.NotNull()
        com.example.callguardian.model.LookupResult result, @org.jetbrains.annotations.NotNull()
        java.time.Instant timestamp, long ttlSeconds, int hitCount) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.model.LookupResult getResult() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.time.Instant getTimestamp() {
            return null;
        }
        
        public final long getTtlSeconds() {
            return 0L;
        }
        
        public final int getHitCount() {
            return 0;
        }
        
        public final boolean isExpired() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.data.cache.LookupCacheManager.CacheEntry incrementHitCount() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.model.LookupResult component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.time.Instant component2() {
            return null;
        }
        
        public final long component3() {
            return 0L;
        }
        
        public final int component4() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.data.cache.LookupCacheManager.CacheEntry copy(@org.jetbrains.annotations.NotNull()
        com.example.callguardian.model.LookupResult result, @org.jetbrains.annotations.NotNull()
        java.time.Instant timestamp, long ttlSeconds, int hitCount) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/example/callguardian/data/cache/LookupCacheManager$Companion;", "", "()V", "DEFAULT_TTL_SECONDS", "", "MAX_CACHE_SIZE", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}