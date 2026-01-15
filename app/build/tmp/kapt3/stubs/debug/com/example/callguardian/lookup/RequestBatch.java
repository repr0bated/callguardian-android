package com.example.callguardian.lookup;

/**
 * Batch of requests for a phone number
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012,\b\u0002\u0010\u0004\u001a&\u0012\u0004\u0012\u00020\u0006\u0012\u001c\u0012\u001a\b\u0001\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00070\u0005\u00a2\u0006\u0002\u0010\nJ3\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\u001e\u0010\u0012\u001a\u001a\b\u0001\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0007\u00a2\u0006\u0002\u0010\u0013J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J-\u0010\u0015\u001a&\u0012\u0004\u0012\u00020\u0006\u0012\u001c\u0012\u001a\b\u0001\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00070\u0005H\u00c6\u0003JA\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032,\b\u0002\u0010\u0004\u001a&\u0012\u0004\u0012\u00020\u0006\u0012\u001c\u0012\u001a\b\u0001\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00070\u0005H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR5\u0010\u0004\u001a&\u0012\u0004\u0012\u00020\u0006\u0012\u001c\u0012\u001a\b\u0001\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00070\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001d"}, d2 = {"Lcom/example/callguardian/lookup/RequestBatch;", "", "phoneNumber", "", "requests", "", "Lcom/example/callguardian/lookup/ReverseLookupSource;", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "Lcom/example/callguardian/model/LookupResult;", "(Ljava/lang/String;Ljava/util/Map;)V", "getPhoneNumber", "()Ljava/lang/String;", "getRequests", "()Ljava/util/Map;", "addRequest", "", "source", "function", "(Lcom/example/callguardian/lookup/ReverseLookupSource;Lkotlin/jvm/functions/Function1;)V", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class RequestBatch {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String phoneNumber = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.example.callguardian.lookup.ReverseLookupSource, kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult>, java.lang.Object>> requests = null;
    
    public RequestBatch(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.example.callguardian.lookup.ReverseLookupSource, kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult>, java.lang.Object>> requests) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPhoneNumber() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.example.callguardian.lookup.ReverseLookupSource, kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult>, java.lang.Object>> getRequests() {
        return null;
    }
    
    public final void addRequest(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.lookup.ReverseLookupSource source, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult>, ? extends java.lang.Object> function) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.example.callguardian.lookup.ReverseLookupSource, kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult>, java.lang.Object>> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.lookup.RequestBatch copy(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.example.callguardian.lookup.ReverseLookupSource, kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult>, java.lang.Object>> requests) {
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