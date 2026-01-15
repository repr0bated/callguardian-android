package com.example.callguardian.lookup;

/**
 * Health metrics for a single lookup source
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BM\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\t\u0010\'\u001a\u00020\nH\u00c6\u0003J\t\u0010(\u001a\u00020\fH\u00c6\u0003JQ\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010*\u001a\u00020\u00192\b\u0010+\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010,\u001a\u00020\nH\u00d6\u0001J\u000e\u0010-\u001a\u00020\u00002\u0006\u0010.\u001a\u00020/J\u000e\u00100\u001a\u00020\u00002\u0006\u00101\u001a\u00020\u0003J\t\u00102\u001a\u000203H\u00d6\u0001R\u0011\u0010\u000e\u001a\u00020\u000f8F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\u00198F\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u001aR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u001d\u001a\u00020\u000f8F\u00a2\u0006\u0006\u001a\u0004\b\u001e\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0017R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0017\u00a8\u00064"}, d2 = {"Lcom/example/callguardian/lookup/ServiceHealth;", "", "totalRequests", "", "successfulRequests", "failedRequests", "totalResponseTimeMs", "lastFailureTime", "Ljava/time/Instant;", "consecutiveFailures", "", "circuitBreakerState", "Lcom/example/callguardian/lookup/CircuitBreakerState;", "(JJJJLjava/time/Instant;ILcom/example/callguardian/lookup/CircuitBreakerState;)V", "averageResponseTimeMs", "", "getAverageResponseTimeMs", "()D", "getCircuitBreakerState", "()Lcom/example/callguardian/lookup/CircuitBreakerState;", "getConsecutiveFailures", "()I", "getFailedRequests", "()J", "isHealthy", "", "()Z", "getLastFailureTime", "()Ljava/time/Instant;", "successRate", "getSuccessRate", "getSuccessfulRequests", "getTotalRequests", "getTotalResponseTimeMs", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "recordFailure", "error", "", "recordSuccess", "responseTimeMs", "toString", "", "app_debug"})
public final class ServiceHealth {
    private final long totalRequests = 0L;
    private final long successfulRequests = 0L;
    private final long failedRequests = 0L;
    private final long totalResponseTimeMs = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.time.Instant lastFailureTime = null;
    private final int consecutiveFailures = 0;
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.lookup.CircuitBreakerState circuitBreakerState = null;
    
    public ServiceHealth(long totalRequests, long successfulRequests, long failedRequests, long totalResponseTimeMs, @org.jetbrains.annotations.Nullable()
    java.time.Instant lastFailureTime, int consecutiveFailures, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.lookup.CircuitBreakerState circuitBreakerState) {
        super();
    }
    
    public final long getTotalRequests() {
        return 0L;
    }
    
    public final long getSuccessfulRequests() {
        return 0L;
    }
    
    public final long getFailedRequests() {
        return 0L;
    }
    
    public final long getTotalResponseTimeMs() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.Instant getLastFailureTime() {
        return null;
    }
    
    public final int getConsecutiveFailures() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.CircuitBreakerState getCircuitBreakerState() {
        return null;
    }
    
    public final double getSuccessRate() {
        return 0.0;
    }
    
    public final double getAverageResponseTimeMs() {
        return 0.0;
    }
    
    public final boolean isHealthy() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.ServiceHealth recordSuccess(long responseTimeMs) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.ServiceHealth recordFailure(@org.jetbrains.annotations.NotNull()
    java.lang.Throwable error) {
        return null;
    }
    
    public ServiceHealth() {
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
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.CircuitBreakerState component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.ServiceHealth copy(long totalRequests, long successfulRequests, long failedRequests, long totalResponseTimeMs, @org.jetbrains.annotations.Nullable()
    java.time.Instant lastFailureTime, int consecutiveFailures, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.lookup.CircuitBreakerState circuitBreakerState) {
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