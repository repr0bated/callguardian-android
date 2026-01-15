package com.example.callguardian.lookup.sources;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0002\u0017\u0018B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0016R2\u0010\t\u001a&\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b \f*\u0012\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000eX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u000eX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/example/callguardian/lookup/sources/NumLookupApiSource;", "Lcom/example/callguardian/lookup/ReverseLookupSource;", "preferences", "Lcom/example/callguardian/data/settings/LookupPreferences;", "client", "Lokhttp3/OkHttpClient;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Lcom/example/callguardian/data/settings/LookupPreferences;Lokhttp3/OkHttpClient;Lcom/squareup/moshi/Moshi;)V", "adapter", "Lcom/squareup/moshi/JsonAdapter;", "Lcom/example/callguardian/lookup/sources/NumLookupApiSource$NumLookupResponse;", "kotlin.jvm.PlatformType", "displayName", "", "getDisplayName", "()Ljava/lang/String;", "id", "getId", "lookup", "Lcom/example/callguardian/model/LookupResult;", "phoneNumber", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "NumLookupResponse", "app_debug"})
public final class NumLookupApiSource implements com.example.callguardian.lookup.ReverseLookupSource {
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.data.settings.LookupPreferences preferences = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    private final com.squareup.moshi.JsonAdapter<com.example.callguardian.lookup.sources.NumLookupApiSource.NumLookupResponse> adapter = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = "numlookup";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = "NumLookupAPI";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String BASE_URL = "https://api.numlookupapi.com/";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.callguardian.lookup.sources.NumLookupApiSource.Companion Companion = null;
    
    @javax.inject.Inject()
    public NumLookupApiSource(@org.jetbrains.annotations.NotNull()
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/callguardian/lookup/sources/NumLookupApiSource$Companion;", "", "()V", "BASE_URL", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u001c\b\u0082\b\u0018\u00002\u00020\u0001BQ\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0001\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0001\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0001\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0017J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u0010\u0010\u001e\u001a\u0004\u0018\u00010\nH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0014J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003Jb\u0010 \u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0003\u0010\u0007\u001a\u0004\u0018\u00010\u00052\n\b\u0003\u0010\b\u001a\u0004\u0018\u00010\u00052\n\b\u0003\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001\u00a2\u0006\u0002\u0010!J\u0013\u0010\"\u001a\u00020\u00032\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020\nH\u00d6\u0001J\t\u0010%\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0013\u0010\b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0015\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\n\n\u0002\u0010\u0015\u001a\u0004\b\u0013\u0010\u0014R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006&"}, d2 = {"Lcom/example/callguardian/lookup/sources/NumLookupApiSource$NumLookupResponse;", "", "valid", "", "name", "", "carrier", "lineType", "countryCode", "spamScore", "", "error", "(Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;)V", "getCarrier", "()Ljava/lang/String;", "getCountryCode", "getError", "getLineType", "getName", "getSpamScore", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getValid", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;)Lcom/example/callguardian/lookup/sources/NumLookupApiSource$NumLookupResponse;", "equals", "other", "hashCode", "toString", "app_debug"})
    static final class NumLookupResponse {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Boolean valid = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String name = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String carrier = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String lineType = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String countryCode = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer spamScore = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String error = null;
        
        public NumLookupResponse(@org.jetbrains.annotations.Nullable()
        java.lang.Boolean valid, @org.jetbrains.annotations.Nullable()
        java.lang.String name, @org.jetbrains.annotations.Nullable()
        java.lang.String carrier, @com.squareup.moshi.Json(name = "line_type")
        @org.jetbrains.annotations.Nullable()
        java.lang.String lineType, @com.squareup.moshi.Json(name = "country_code")
        @org.jetbrains.annotations.Nullable()
        java.lang.String countryCode, @com.squareup.moshi.Json(name = "spam_score")
        @org.jetbrains.annotations.Nullable()
        java.lang.Integer spamScore, @org.jetbrains.annotations.Nullable()
        java.lang.String error) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Boolean getValid() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getName() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getCarrier() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getLineType() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getCountryCode() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getSpamScore() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getError() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Boolean component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component4() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component5() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer component6() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component7() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.lookup.sources.NumLookupApiSource.NumLookupResponse copy(@org.jetbrains.annotations.Nullable()
        java.lang.Boolean valid, @org.jetbrains.annotations.Nullable()
        java.lang.String name, @org.jetbrains.annotations.Nullable()
        java.lang.String carrier, @com.squareup.moshi.Json(name = "line_type")
        @org.jetbrains.annotations.Nullable()
        java.lang.String lineType, @com.squareup.moshi.Json(name = "country_code")
        @org.jetbrains.annotations.Nullable()
        java.lang.String countryCode, @com.squareup.moshi.Json(name = "spam_score")
        @org.jetbrains.annotations.Nullable()
        java.lang.Integer spamScore, @org.jetbrains.annotations.Nullable()
        java.lang.String error) {
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
}