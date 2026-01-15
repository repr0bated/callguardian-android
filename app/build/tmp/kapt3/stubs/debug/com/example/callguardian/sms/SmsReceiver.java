package com.example.callguardian.sms;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/example/callguardian/sms/SmsReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "lookupRepository", "Lcom/example/callguardian/data/repository/LookupRepository;", "getLookupRepository", "()Lcom/example/callguardian/data/repository/LookupRepository;", "setLookupRepository", "(Lcom/example/callguardian/data/repository/LookupRepository;)V", "notificationManager", "Lcom/example/callguardian/notifications/LookupNotificationManager;", "getNotificationManager", "()Lcom/example/callguardian/notifications/LookupNotificationManager;", "setNotificationManager", "(Lcom/example/callguardian/notifications/LookupNotificationManager;)V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_debug"})
public final class SmsReceiver extends android.content.BroadcastReceiver {
    @javax.inject.Inject()
    public com.example.callguardian.data.repository.LookupRepository lookupRepository;
    @javax.inject.Inject()
    public com.example.callguardian.notifications.LookupNotificationManager notificationManager;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    
    public SmsReceiver() {
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
    public void onReceive(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
}