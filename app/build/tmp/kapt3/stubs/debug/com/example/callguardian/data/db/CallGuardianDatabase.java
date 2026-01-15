package com.example.callguardian.data.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcom/example/callguardian/data/db/CallGuardianDatabase;", "Landroidx/room/RoomDatabase;", "()V", "phoneInteractionDao", "Lcom/example/callguardian/data/db/PhoneInteractionDao;", "phoneProfileDao", "Lcom/example/callguardian/data/db/PhoneProfileDao;", "app_debug"})
@androidx.room.Database(entities = {com.example.callguardian.data.db.PhoneProfileEntity.class, com.example.callguardian.data.db.PhoneInteractionEntity.class}, version = 1, exportSchema = true)
@androidx.room.TypeConverters(value = {com.example.callguardian.data.db.Converters.class})
public abstract class CallGuardianDatabase extends androidx.room.RoomDatabase {
    
    public CallGuardianDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.callguardian.data.db.PhoneProfileDao phoneProfileDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.callguardian.data.db.PhoneInteractionDao phoneInteractionDao();
}