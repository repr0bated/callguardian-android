package com.example.callguardian.ai;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u0000 \u001c2\u00020\u0001:\u0003\u001c\u001d\u001eB\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ$\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\b\u0010\u0014\u001a\u0004\u0018\u00010\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0002J,\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0013\u001a\u00020\u00122\b\u0010\u0014\u001a\u0004\u0018\u00010\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0086@\u00a2\u0006\u0002\u0010\u0019J\u0012\u0010\u001a\u001a\u0004\u0018\u00010\f2\u0006\u0010\u001b\u001a\u00020\u0012H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000Rb\u0010\t\u001aV\u0012$\u0012\"\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b \r*\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b\u0018\u00010\u000b0\u000b \r**\u0012$\u0012\"\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b \r*\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b\u0018\u00010\u000b0\u000b\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R2\u0010\u000e\u001a&\u0012\f\u0012\n \r*\u0004\u0018\u00010\u000f0\u000f \r*\u0012\u0012\f\u0012\n \r*\u0004\u0018\u00010\u000f0\u000f\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000RJ\u0010\u0010\u001a>\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\f \r*\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b0\u000b \r*\u001e\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\f \r*\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b0\u000b\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/example/callguardian/ai/AiRiskScorer;", "", "preferences", "Lcom/example/callguardian/data/settings/LookupPreferences;", "client", "Lokhttp3/OkHttpClient;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Lcom/example/callguardian/data/settings/LookupPreferences;Lokhttp3/OkHttpClient;Lcom/squareup/moshi/Moshi;)V", "listResponseAdapter", "Lcom/squareup/moshi/JsonAdapter;", "", "Lcom/example/callguardian/ai/AiRiskScorer$ResponseItem;", "kotlin.jvm.PlatformType", "requestAdapter", "Lcom/example/callguardian/ai/AiRiskScorer$RequestPayload;", "responseAdapter", "buildInput", "", "phoneNumber", "messageBody", "lookupResult", "Lcom/example/callguardian/model/LookupResult;", "evaluate", "Lcom/example/callguardian/model/AiAssessment;", "(Ljava/lang/String;Ljava/lang/String;Lcom/example/callguardian/model/LookupResult;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseResponse", "rawBody", "Companion", "RequestPayload", "ResponseItem", "app_debug"})
public final class AiRiskScorer {
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.data.settings.LookupPreferences preferences = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    private final com.squareup.moshi.JsonAdapter<com.example.callguardian.ai.AiRiskScorer.RequestPayload> requestAdapter = null;
    private final com.squareup.moshi.JsonAdapter<java.util.List<com.example.callguardian.ai.AiRiskScorer.ResponseItem>> responseAdapter = null;
    private final com.squareup.moshi.JsonAdapter<java.util.List<java.util.List<com.example.callguardian.ai.AiRiskScorer.ResponseItem>>> listResponseAdapter = null;
    @org.jetbrains.annotations.NotNull()
    private static final okhttp3.MediaType JSON_MEDIA_TYPE = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.callguardian.ai.AiRiskScorer.Companion Companion = null;
    
    @javax.inject.Inject()
    public AiRiskScorer(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.settings.LookupPreferences preferences, @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient client, @org.jetbrains.annotations.NotNull()
    com.squareup.moshi.Moshi moshi) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object evaluate(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.Nullable()
    java.lang.String messageBody, @org.jetbrains.annotations.Nullable()
    com.example.callguardian.model.LookupResult lookupResult, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.model.AiAssessment> $completion) {
        return null;
    }
    
    private final com.example.callguardian.ai.AiRiskScorer.ResponseItem parseResponse(java.lang.String rawBody) {
        return null;
    }
    
    private final java.lang.String buildInput(java.lang.String phoneNumber, java.lang.String messageBody, com.example.callguardian.model.LookupResult lookupResult) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/callguardian/ai/AiRiskScorer$Companion;", "", "()V", "JSON_MEDIA_TYPE", "Lokhttp3/MediaType;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\f\u001a\u00020\rH\u00d6\u0001J\t\u0010\u000e\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/example/callguardian/ai/AiRiskScorer$RequestPayload;", "", "inputs", "", "(Ljava/lang/String;)V", "getInputs", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class RequestPayload {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String inputs = null;
        
        public RequestPayload(@org.jetbrains.annotations.NotNull()
        java.lang.String inputs) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getInputs() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.ai.AiRiskScorer.RequestPayload copy(@org.jetbrains.annotations.NotNull()
        java.lang.String inputs) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B%\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0003\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0007J\u000b\u0010\u000e\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\fJ\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J2\u0010\u0011\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0003\u0010\u0006\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010\u0012J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0019"}, d2 = {"Lcom/example/callguardian/ai/AiRiskScorer$ResponseItem;", "", "label", "", "score", "", "error", "(Ljava/lang/String;Ljava/lang/Double;Ljava/lang/String;)V", "getError", "()Ljava/lang/String;", "getLabel", "getScore", "()Ljava/lang/Double;", "Ljava/lang/Double;", "component1", "component2", "component3", "copy", "(Ljava/lang/String;Ljava/lang/Double;Ljava/lang/String;)Lcom/example/callguardian/ai/AiRiskScorer$ResponseItem;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class ResponseItem {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String label = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Double score = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String error = null;
        
        public ResponseItem(@org.jetbrains.annotations.Nullable()
        java.lang.String label, @org.jetbrains.annotations.Nullable()
        java.lang.Double score, @com.squareup.moshi.Json(name = "error")
        @org.jetbrains.annotations.Nullable()
        java.lang.String error) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getLabel() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Double getScore() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getError() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Double component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.ai.AiRiskScorer.ResponseItem copy(@org.jetbrains.annotations.Nullable()
        java.lang.String label, @org.jetbrains.annotations.Nullable()
        java.lang.Double score, @com.squareup.moshi.Json(name = "error")
        @org.jetbrains.annotations.Nullable()
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