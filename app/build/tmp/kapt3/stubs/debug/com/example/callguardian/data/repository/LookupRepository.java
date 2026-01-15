package com.example.callguardian.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00b2\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B?\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J2\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001bH\u0086@\u00a2\u0006\u0002\u0010\u001fJ2\u0010 \u001a\u0004\u0018\u00010\u00142\b\u0010!\u001a\u0004\u0018\u00010\"2\b\u0010#\u001a\u0004\u0018\u00010$2\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002J\u0018\u0010\'\u001a\u0004\u0018\u00010$2\u0006\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010(\u001a\u00020\u00172\u0006\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u001e\u0010)\u001a\u0004\u0018\u00010\"2\b\u0010!\u001a\u0004\u0018\u00010\"2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002J\u0012\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020$0\u001b0+J\u001c\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u001b0+2\b\b\u0002\u0010.\u001a\u00020/J2\u00100\u001a\u0002012\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u0002052\n\b\u0002\u00106\u001a\u0004\u0018\u00010\u0014H\u0086@\u00a2\u0006\u0002\u00107J\u001e\u00108\u001a\u0002092\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010:\u001a\u00020;H\u0086@\u00a2\u0006\u0002\u0010<J(\u0010=\u001a\u0002092\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010>\u001a\u0004\u0018\u00010?2\u0006\u0010(\u001a\u00020\u0017H\u0086@\u00a2\u0006\u0002\u0010@J2\u0010A\u001a\u00020$*\u00020$2\b\u0010!\u001a\u0004\u0018\u00010\"2\u0006\u0010B\u001a\u00020\u00142\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002J(\u0010C\u001a\u00020$*\u00020\"2\u0006\u0010B\u001a\u00020\u00142\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002J$\u0010D\u001a\b\u0012\u0004\u0012\u00020\u00140\u001b*\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u001b2\b\u0010%\u001a\u0004\u0018\u00010&H\u0002J$\u0010E\u001a\b\u0012\u0004\u0012\u00020\u00140\u001b*\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u001b2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006F"}, d2 = {"Lcom/example/callguardian/data/repository/LookupRepository;", "", "reverseLookupManager", "Lcom/example/callguardian/lookup/ReverseLookupManager;", "phoneProfileDao", "Lcom/example/callguardian/data/db/PhoneProfileDao;", "interactionDao", "Lcom/example/callguardian/data/db/PhoneInteractionDao;", "aiRiskScorer", "Lcom/example/callguardian/ai/AiRiskScorer;", "contactLookupService", "Lcom/example/callguardian/service/ContactLookupService;", "contactSyncService", "Lcom/example/callguardian/service/ContactSyncService;", "ioDispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "(Lcom/example/callguardian/lookup/ReverseLookupManager;Lcom/example/callguardian/data/db/PhoneProfileDao;Lcom/example/callguardian/data/db/PhoneInteractionDao;Lcom/example/callguardian/ai/AiRiskScorer;Lcom/example/callguardian/service/ContactLookupService;Lcom/example/callguardian/service/ContactSyncService;Lkotlinx/coroutines/CoroutineDispatcher;)V", "analyzeContactSync", "Lcom/example/callguardian/service/ContactSyncResult;", "phoneNumber", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "applyContactSyncChanges", "", "contactInfo", "Lcom/example/callguardian/model/ContactInfo;", "approvedChanges", "", "Lcom/example/callguardian/service/ContactChange;", "newInfo", "Lcom/example/callguardian/service/ContactInfoField;", "(Lcom/example/callguardian/model/ContactInfo;Ljava/util/List;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buildSummary", "lookupResult", "Lcom/example/callguardian/model/LookupResult;", "profile", "Lcom/example/callguardian/data/db/PhoneProfileEntity;", "aiAssessment", "Lcom/example/callguardian/model/AiAssessment;", "getProfile", "isExistingContact", "mergeContactInfoWithLookupResult", "observeProfiles", "Lkotlinx/coroutines/flow/Flow;", "observeRecentInteractions", "Lcom/example/callguardian/data/db/PhoneInteractionEntity;", "limit", "", "performLookup", "Lcom/example/callguardian/model/LookupOutcome;", "type", "Lcom/example/callguardian/model/InteractionType;", "direction", "Lcom/example/callguardian/model/InteractionDirection;", "messageBody", "(Ljava/lang/String;Lcom/example/callguardian/model/InteractionType;Lcom/example/callguardian/model/InteractionDirection;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateBlockMode", "", "blockMode", "Lcom/example/callguardian/model/BlockMode;", "(Ljava/lang/String;Lcom/example/callguardian/model/BlockMode;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateContactInfo", "contactId", "", "(Ljava/lang/String;Ljava/lang/Long;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "merge", "normalizedNumber", "toEntity", "withAiTag", "withContactTag", "app_debug"})
public final class LookupRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.lookup.ReverseLookupManager reverseLookupManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.data.db.PhoneProfileDao phoneProfileDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.data.db.PhoneInteractionDao interactionDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.ai.AiRiskScorer aiRiskScorer = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.service.ContactLookupService contactLookupService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.service.ContactSyncService contactSyncService = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineDispatcher ioDispatcher = null;
    
    @javax.inject.Inject()
    public LookupRepository(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.lookup.ReverseLookupManager reverseLookupManager, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.db.PhoneProfileDao phoneProfileDao, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.db.PhoneInteractionDao interactionDao, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.ai.AiRiskScorer aiRiskScorer, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.service.ContactLookupService contactLookupService, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.service.ContactSyncService contactSyncService, @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineDispatcher ioDispatcher) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object performLookup(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.model.InteractionType type, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.model.InteractionDirection direction, @org.jetbrains.annotations.Nullable()
    java.lang.String messageBody, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.model.LookupOutcome> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateBlockMode(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.model.BlockMode blockMode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateContactInfo(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.Nullable()
    java.lang.Long contactId, boolean isExistingContact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Analyzes if a contact sync is needed and returns sync recommendations
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object analyzeContactSync(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.service.ContactSyncResult> $completion) {
        return null;
    }
    
    /**
     * Applies approved contact sync changes
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object applyContactSyncChanges(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.model.ContactInfo contactInfo, @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.callguardian.service.ContactChange> approvedChanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.callguardian.service.ContactInfoField> newInfo, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.callguardian.data.db.PhoneProfileEntity> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.callguardian.data.db.PhoneProfileEntity>> observeProfiles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.callguardian.data.db.PhoneInteractionEntity>> observeRecentInteractions(int limit) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object isExistingContact(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final com.example.callguardian.data.db.PhoneProfileEntity toEntity(com.example.callguardian.model.LookupResult $this$toEntity, java.lang.String normalizedNumber, com.example.callguardian.model.AiAssessment aiAssessment, com.example.callguardian.model.ContactInfo contactInfo) {
        return null;
    }
    
    private final com.example.callguardian.data.db.PhoneProfileEntity merge(com.example.callguardian.data.db.PhoneProfileEntity $this$merge, com.example.callguardian.model.LookupResult lookupResult, java.lang.String normalizedNumber, com.example.callguardian.model.AiAssessment aiAssessment, com.example.callguardian.model.ContactInfo contactInfo) {
        return null;
    }
    
    private final java.util.List<java.lang.String> withAiTag(java.util.List<java.lang.String> $this$withAiTag, com.example.callguardian.model.AiAssessment aiAssessment) {
        return null;
    }
    
    private final java.util.List<java.lang.String> withContactTag(java.util.List<java.lang.String> $this$withContactTag, com.example.callguardian.model.ContactInfo contactInfo) {
        return null;
    }
    
    private final java.lang.String buildSummary(com.example.callguardian.model.LookupResult lookupResult, com.example.callguardian.data.db.PhoneProfileEntity profile, com.example.callguardian.model.AiAssessment aiAssessment, com.example.callguardian.model.ContactInfo contactInfo) {
        return null;
    }
    
    private final com.example.callguardian.model.LookupResult mergeContactInfoWithLookupResult(com.example.callguardian.model.LookupResult lookupResult, com.example.callguardian.model.ContactInfo contactInfo) {
        return null;
    }
}