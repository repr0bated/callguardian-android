package com.example.callguardian.lookup.sources;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 \u00182\u00020\u0001:\u0002\u0017\u0018B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u0016R2\u0010\t\u001a&\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b \f*\u0012\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000eX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u000eX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/example/callguardian/lookup/sources/AbstractApiSource;", "Lcom/example/callguardian/lookup/ReverseLookupSource;", "preferences", "Lcom/example/callguardian/data/settings/LookupPreferences;", "client", "Lokhttp3/OkHttpClient;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Lcom/example/callguardian/data/settings/LookupPreferences;Lokhttp3/OkHttpClient;Lcom/squareup/moshi/Moshi;)V", "adapter", "Lcom/squareup/moshi/JsonAdapter;", "Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse;", "kotlin.jvm.PlatformType", "displayName", "", "getDisplayName", "()Ljava/lang/String;", "id", "getId", "lookup", "Lcom/example/callguardian/model/LookupResult;", "phoneNumber", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "AbstractApiResponse", "Companion", "app_debug"})
public final class AbstractApiSource implements com.example.callguardian.lookup.ReverseLookupSource {
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.data.settings.LookupPreferences preferences = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = "abstractapi";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = "AbstractAPI";
    private final com.squareup.moshi.JsonAdapter<com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse> adapter = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String BASE_URL = "https://phonevalidation.abstractapi.com/v1/";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.callguardian.lookup.sources.AbstractApiSource.Companion Companion = null;
    
    @javax.inject.Inject()
    public AbstractApiSource(@org.jetbrains.annotations.NotNull()
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\b\n\u0002\b\u0005\b\u0082\b\u0018\u00002\u00020\u0001:\u0003\'()BA\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0018J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\fH\u00c6\u0003JV\u0010 \u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00c6\u0001\u00a2\u0006\u0002\u0010!J\u0013\u0010\"\u001a\u00020\u00032\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020%H\u00d6\u0001J\t\u0010&\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000fR\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0019\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006*"}, d2 = {"Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse;", "", "valid", "", "carrier", "", "type", "owner", "Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Owner;", "location", "Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Location;", "risk", "Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Risk;", "(Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Owner;Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Location;Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Risk;)V", "getCarrier", "()Ljava/lang/String;", "getLocation", "()Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Location;", "getOwner", "()Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Owner;", "getRisk", "()Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Risk;", "getType", "getValid", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Owner;Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Location;Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Risk;)Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse;", "equals", "other", "hashCode", "", "toString", "Location", "Owner", "Risk", "app_debug"})
    static final class AbstractApiResponse {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Boolean valid = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String carrier = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String type = null;
        @org.jetbrains.annotations.Nullable()
        private final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Owner owner = null;
        @org.jetbrains.annotations.Nullable()
        private final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Location location = null;
        @org.jetbrains.annotations.Nullable()
        private final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Risk risk = null;
        
        public AbstractApiResponse(@org.jetbrains.annotations.Nullable()
        java.lang.Boolean valid, @org.jetbrains.annotations.Nullable()
        java.lang.String carrier, @org.jetbrains.annotations.Nullable()
        java.lang.String type, @org.jetbrains.annotations.Nullable()
        com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Owner owner, @org.jetbrains.annotations.Nullable()
        com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Location location, @org.jetbrains.annotations.Nullable()
        com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Risk risk) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Boolean getValid() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getCarrier() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getType() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Owner getOwner() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Location getLocation() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Risk getRisk() {
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
        public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Owner component4() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Location component5() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Risk component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse copy(@org.jetbrains.annotations.Nullable()
        java.lang.Boolean valid, @org.jetbrains.annotations.Nullable()
        java.lang.String carrier, @org.jetbrains.annotations.Nullable()
        java.lang.String type, @org.jetbrains.annotations.Nullable()
        com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Owner owner, @org.jetbrains.annotations.Nullable()
        com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Location location, @org.jetbrains.annotations.Nullable()
        com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Risk risk) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0005J\u000b\u0010\t\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\n\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J!\u0010\u000b\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Location;", "", "city", "", "country", "(Ljava/lang/String;Ljava/lang/String;)V", "getCity", "()Ljava/lang/String;", "getCountry", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
        public static final class Location {
            @org.jetbrains.annotations.Nullable()
            private final java.lang.String city = null;
            @org.jetbrains.annotations.Nullable()
            private final java.lang.String country = null;
            
            public Location(@org.jetbrains.annotations.Nullable()
            java.lang.String city, @org.jetbrains.annotations.Nullable()
            java.lang.String country) {
                super();
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.String getCity() {
                return null;
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.String getCountry() {
                return null;
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.String component2() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Location copy(@org.jetbrains.annotations.Nullable()
            java.lang.String city, @org.jetbrains.annotations.Nullable()
            java.lang.String country) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0011\u0012\n\b\u0001\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u000b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0015\u0010\b\u001a\u00020\u00002\n\b\u0003\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\f\u001a\u00020\rH\u00d6\u0001J\t\u0010\u000e\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Owner;", "", "name", "", "(Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
        public static final class Owner {
            @org.jetbrains.annotations.Nullable()
            private final java.lang.String name = null;
            
            public Owner(@com.squareup.moshi.Json(name = "name")
            @org.jetbrains.annotations.Nullable()
            java.lang.String name) {
                super();
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.String getName() {
                return null;
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Owner copy(@com.squareup.moshi.Json(name = "name")
            @org.jetbrains.annotations.Nullable()
            java.lang.String name) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010\r\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\nJ&\u0010\u000e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001\u00a2\u0006\u0002\u0010\u000fJ\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0013\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Risk;", "", "level", "", "score", "", "(Ljava/lang/String;Ljava/lang/Integer;)V", "getLevel", "()Ljava/lang/String;", "getScore", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "copy", "(Ljava/lang/String;Ljava/lang/Integer;)Lcom/example/callguardian/lookup/sources/AbstractApiSource$AbstractApiResponse$Risk;", "equals", "", "other", "hashCode", "toString", "app_debug"})
        public static final class Risk {
            @org.jetbrains.annotations.Nullable()
            private final java.lang.String level = null;
            @org.jetbrains.annotations.Nullable()
            private final java.lang.Integer score = null;
            
            public Risk(@org.jetbrains.annotations.Nullable()
            java.lang.String level, @org.jetbrains.annotations.Nullable()
            java.lang.Integer score) {
                super();
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.String getLevel() {
                return null;
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.Integer getScore() {
                return null;
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.Nullable()
            public final java.lang.Integer component2() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.example.callguardian.lookup.sources.AbstractApiSource.AbstractApiResponse.Risk copy(@org.jetbrains.annotations.Nullable()
            java.lang.String level, @org.jetbrains.annotations.Nullable()
            java.lang.Integer score) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/callguardian/lookup/sources/AbstractApiSource$Companion;", "", "()V", "BASE_URL", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}