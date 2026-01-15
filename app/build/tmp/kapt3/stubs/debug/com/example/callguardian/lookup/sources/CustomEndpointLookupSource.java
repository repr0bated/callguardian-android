package com.example.callguardian.lookup.sources;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0018B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u0017Rj\u0010\t\u001a^\u0012(\u0012&\u0012\u0004\u0012\u00020\f\u0012\u0006\u0012\u0004\u0018\u00010\r \u000e*\u0012\u0012\u0004\u0012\u00020\f\u0012\u0006\u0012\u0004\u0018\u00010\r\u0018\u00010\u000b0\u000b \u000e*.\u0012(\u0012&\u0012\u0004\u0012\u00020\f\u0012\u0006\u0012\u0004\u0018\u00010\r \u000e*\u0012\u0012\u0004\u0012\u00020\f\u0012\u0006\u0012\u0004\u0018\u00010\r\u0018\u00010\u000b0\u000b\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\u00020\fX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\fX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/example/callguardian/lookup/sources/CustomEndpointLookupSource;", "Lcom/example/callguardian/lookup/ReverseLookupSource;", "preferences", "Lcom/example/callguardian/data/settings/LookupPreferences;", "client", "Lokhttp3/OkHttpClient;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Lcom/example/callguardian/data/settings/LookupPreferences;Lokhttp3/OkHttpClient;Lcom/squareup/moshi/Moshi;)V", "adapter", "Lcom/squareup/moshi/JsonAdapter;", "", "", "", "kotlin.jvm.PlatformType", "displayName", "getDisplayName", "()Ljava/lang/String;", "id", "getId", "lookup", "Lcom/example/callguardian/model/LookupResult;", "phoneNumber", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "KNOWN_KEYS", "app_debug"})
public final class CustomEndpointLookupSource implements com.example.callguardian.lookup.ReverseLookupSource {
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.data.settings.LookupPreferences preferences = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    private final com.squareup.moshi.JsonAdapter<java.util.Map<java.lang.String, java.lang.Object>> adapter = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = "custom";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = "Custom Endpoint";
    
    @javax.inject.Inject()
    public CustomEndpointLookupSource(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.settings.LookupPreferences preferences, @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient client, @org.jetbrains.annotations.NotNull()
    com.squareup.moshi.Moshi moshi) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getId() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getDisplayName() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object lookup(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupResult> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/callguardian/lookup/sources/CustomEndpointLookupSource$KNOWN_KEYS;", "", "()V", "carrier", "", "displayName", "region", "spamScore", "app_debug"})
    static final class KNOWN_KEYS {
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String displayName = "display_name";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String carrier = "carrier";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String region = "region";
        @org.jetbrains.annotations.NotNull()
        public static final java.lang.String spamScore = "spam_score";
        @org.jetbrains.annotations.NotNull()
        public static final com.example.callguardian.lookup.sources.CustomEndpointLookupSource.KNOWN_KEYS INSTANCE = null;
        
        private KNOWN_KEYS() {
            super();
        }
    }
}