package com.example.callguardian.data.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0019\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0007\u00a2\u0006\u0002\u0010\u000bJ\u0018\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\r2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\u000e\u001a\u00020\u00062\b\u0010\u000f\u001a\u0004\u0018\u00010\u0004H\u0007J\u0019\u0010\u0010\u001a\u0004\u0018\u00010\n2\b\u0010\u0011\u001a\u0004\u0018\u00010\bH\u0007\u00a2\u0006\u0002\u0010\u0012J\u0018\u0010\u0013\u001a\u00020\u00062\u000e\u0010\u0014\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\rH\u0007\u00a8\u0006\u0015"}, d2 = {"Lcom/example/callguardian/data/db/Converters;", "", "()V", "fromBlockMode", "Lcom/example/callguardian/model/BlockMode;", "value", "", "fromEpoch", "Ljava/time/Instant;", "epoch", "", "(Ljava/lang/Long;)Ljava/time/Instant;", "fromTags", "", "toBlockMode", "mode", "toEpoch", "instant", "(Ljava/time/Instant;)Ljava/lang/Long;", "toTags", "tags", "app_debug"})
public final class Converters {
    
    public Converters() {
        super();
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.Nullable()
    public final java.time.Instant fromEpoch(@org.jetbrains.annotations.Nullable()
    java.lang.Long epoch) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long toEpoch(@org.jetbrains.annotations.Nullable()
    java.time.Instant instant) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.model.BlockMode fromBlockMode(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toBlockMode(@org.jetbrains.annotations.Nullable()
    com.example.callguardian.model.BlockMode mode) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> fromTags(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
        return null;
    }
    
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toTags(@org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.String> tags) {
        return null;
    }
}