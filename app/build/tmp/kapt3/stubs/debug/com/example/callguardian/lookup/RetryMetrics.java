package com.example.callguardian.lookup;

/**
 * Retry metrics for a source
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\bH\u00c6\u0003J=\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bH\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\u0006\u0010\u001c\u001a\u00020\u0000J\u0006\u0010\u001d\u001a\u00020\u0000J\u000e\u0010\u001e\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u0003J\t\u0010 \u001a\u00020!H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000b\u00a8\u0006\""}, d2 = {"Lcom/example/callguardian/lookup/RetryMetrics;", "", "totalAttempts", "", "successfulAttempts", "failedAttempts", "totalResponseTimeMs", "lastFailureTime", "Ljava/time/Instant;", "(JJJJLjava/time/Instant;)V", "getFailedAttempts", "()J", "getLastFailureTime", "()Ljava/time/Instant;", "getSuccessfulAttempts", "getTotalAttempts", "getTotalResponseTimeMs", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "recordFailure", "recordFinalFailure", "recordSuccess", "responseTime", "toString", "", "app_debug"})
public final class RetryMetrics {
    private final long totalAttempts = 0L;
    private final long successfulAttempts = 0L;
    private final long failedAttempts = 0L;
    private final long totalResponseTimeMs = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.time.Instant lastFailureTime = null;
    
    public RetryMetrics(long totalAttempts, long successfulAttempts, long failedAttempts, long totalResponseTimeMs, @org.jetbrains.annotations.Nullable()
    java.time.Instant lastFailureTime) {
        super();
    }
    
    public final long getTotalAttempts() {
        return 0L;
    }
    
    public final long getSuccessfulAttempts() {
        return 0L;
    }
    
    public final long getFailedAttempts() {
        return 0L;
    }
    
    public final long getTotalResponseTimeMs() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.Instant getLastFailureTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.RetryMetrics recordSuccess(long responseTime) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.RetryMetrics recordFailure() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.RetryMetrics recordFinalFailure() {
        return null;
    }
    
    public RetryMetrics() {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.Instant component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.RetryMetrics copy(long totalAttempts, long successfulAttempts, long failedAttempts, long totalResponseTimeMs, @org.jetbrains.annotations.Nullable()
    java.time.Instant lastFailureTime) {
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