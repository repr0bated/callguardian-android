package com.example.callguardian.lookup;

/**
 * A single remote data source that can perform a reverse lookup.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0018\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u000bR\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005\u00a8\u0006\f"}, d2 = {"Lcom/example/callguardian/lookup/ReverseLookupSource;", "", "displayName", "", "getDisplayName", "()Ljava/lang/String;", "id", "getId", "lookup", "Lcom/example/callguardian/model/LookupResult;", "phoneNumber", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ReverseLookupSource {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getId();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getDisplayName();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object lookup(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult> $completion);
}