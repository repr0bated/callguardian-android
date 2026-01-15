package com.example.callguardian.notifications;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0018\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u001e\u0010\u0012\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0014H\u0082@\u00a2\u0006\u0002\u0010\u0015R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/example/callguardian/notifications/NotificationActionReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "lookupRepository", "Lcom/example/callguardian/data/repository/LookupRepository;", "getLookupRepository", "()Lcom/example/callguardian/data/repository/LookupRepository;", "setLookupRepository", "(Lcom/example/callguardian/data/repository/LookupRepository;)V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "endCall", "", "context", "Landroid/content/Context;", "onReceive", "intent", "Landroid/content/Intent;", "saveContact", "phoneNumber", "", "(Landroid/content/Context;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class NotificationActionReceiver extends android.content.BroadcastReceiver {
    @javax.inject.Inject()
    public com.example.callguardian.data.repository.LookupRepository lookupRepository;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PHONE_NUMBER = "extra_phone_number";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_BLOCK = "com.example.callguardian.ACTION_BLOCK";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_ALLOW = "com.example.callguardian.ACTION_ALLOW";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_REJECT = "com.example.callguardian.ACTION_REJECT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_SAVE = "com.example.callguardian.ACTION_SAVE";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.callguardian.notifications.NotificationActionReceiver.Companion Companion = null;
    
    public NotificationActionReceiver() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.callguardian.data.repository.LookupRepository getLookupRepository() {
        return null;
    }
    
    public final void setLookupRepository(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.repository.LookupRepository p0) {
    }
    
    @java.lang.Override()
    public void onReceive(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
    
    private final void endCall(android.content.Context context) {
    }
    
    private final java.lang.Object saveContact(android.content.Context context, java.lang.String phoneNumber, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/example/callguardian/notifications/NotificationActionReceiver$Companion;", "", "()V", "ACTION_ALLOW", "", "ACTION_BLOCK", "ACTION_REJECT", "ACTION_SAVE", "EXTRA_PHONE_NUMBER", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}