package com.example.callguardian.service;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u0015\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0017H\u0016\u00a2\u0006\u0002\u0010\u0018R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/example/callguardian/service/CallGuardianCallScreeningService;", "Landroid/telecom/CallScreeningService;", "()V", "job", "Lkotlinx/coroutines/CompletableJob;", "lookupRepository", "Lcom/example/callguardian/data/repository/LookupRepository;", "getLookupRepository", "()Lcom/example/callguardian/data/repository/LookupRepository;", "setLookupRepository", "(Lcom/example/callguardian/data/repository/LookupRepository;)V", "notificationManager", "Lcom/example/callguardian/notifications/LookupNotificationManager;", "getNotificationManager", "()Lcom/example/callguardian/notifications/LookupNotificationManager;", "setNotificationManager", "(Lcom/example/callguardian/notifications/LookupNotificationManager;)V", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "onDestroy", "", "onScreenCall", "callDetails", "error/NonExistentClass", "(Lerror/NonExistentClass;)V", "Companion", "app_debug"})
public final class CallGuardianCallScreeningService extends android.telecom.CallScreeningService {
    @javax.inject.Inject()
    public com.example.callguardian.data.repository.LookupRepository lookupRepository;
    @javax.inject.Inject()
    public com.example.callguardian.notifications.LookupNotificationManager notificationManager;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CompletableJob job = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    private static final long LOOKUP_TIMEOUT_MS = 2500L;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.callguardian.service.CallGuardianCallScreeningService.Companion Companion = null;
    
    public CallGuardianCallScreeningService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.data.repository.LookupRepository getLookupRepository() {
        return null;
    }
    
    public final void setLookupRepository(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.repository.LookupRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.notifications.LookupNotificationManager getNotificationManager() {
        return null;
    }
    
    public final void setNotificationManager(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.notifications.LookupNotificationManager p0) {
    }
    
    @java.lang.Override()
    public void onScreenCall(@org.jetbrains.annotations.NotNull()
    error.NonExistentClass callDetails) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/callguardian/service/CallGuardianCallScreeningService$Companion;", "", "()V", "LOOKUP_TIMEOUT_MS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}