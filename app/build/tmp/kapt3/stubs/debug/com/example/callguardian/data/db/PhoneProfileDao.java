package com.example.callguardian.data.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\t0\bH\'J\u0018\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\b2\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u001e\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0011J(\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u0019"}, d2 = {"Lcom/example/callguardian/data/db/PhoneProfileDao;", "", "getByNumber", "Lcom/example/callguardian/data/db/PhoneProfileEntity;", "phoneNumber", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAll", "Lkotlinx/coroutines/flow/Flow;", "", "observeByNumber", "update", "", "profile", "(Lcom/example/callguardian/data/db/PhoneProfileEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateBlockMode", "blockMode", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateContactInfo", "contactId", "", "isExistingContact", "", "(Ljava/lang/String;Ljava/lang/Long;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsert", "app_debug"})
@androidx.room.Dao()
public abstract interface PhoneProfileDao {
    
    @androidx.room.Query(value = "SELECT * FROM phone_profiles WHERE phone_number = :phoneNumber")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByNumber(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.data.db.PhoneProfileEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM phone_profiles WHERE phone_number = :phoneNumber")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.example.callguardian.data.db.PhoneProfileEntity> observeByNumber(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber);
    
    @androidx.room.Query(value = "SELECT * FROM phone_profiles ORDER BY last_lookup_at DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.callguardian.data.db.PhoneProfileEntity>> observeAll();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.db.PhoneProfileEntity profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.db.PhoneProfileEntity profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE phone_profiles SET block_mode = :blockMode WHERE phone_number = :phoneNumber")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateBlockMode(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String blockMode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE phone_profiles SET contact_id = :contactId, is_existing_contact = :isExistingContact WHERE phone_number = :phoneNumber")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateContactInfo(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, boolean isExistingContact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}