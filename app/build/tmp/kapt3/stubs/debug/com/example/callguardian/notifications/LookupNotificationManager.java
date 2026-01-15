package com.example.callguardian.notifications;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\nH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\u0018\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014JD\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\n2\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u00142\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u001eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/example/callguardian/notifications/LookupNotificationManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "notificationManager", "Landroidx/core/app/NotificationManagerCompat;", "actionPendingIntent", "Landroid/app/PendingIntent;", "action", "", "phoneNumber", "baseBuilder", "Landroidx/core/app/NotificationCompat$Builder;", "ensureChannel", "", "mutableFlag", "", "notifyBlockedCall", "profile", "Lcom/example/callguardian/data/db/PhoneProfileEntity;", "notifyLookupResult", "lookupResult", "Lcom/example/callguardian/model/LookupResult;", "existingProfile", "aiAssessment", "Lcom/example/callguardian/model/AiAssessment;", "contactInfo", "Lcom/example/callguardian/model/ContactInfo;", "includeRejectAction", "", "Companion", "app_debug"})
public final class LookupNotificationManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.core.app.NotificationManagerCompat notificationManager = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ID = "lookup_results";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.callguardian.notifications.LookupNotificationManager.Companion Companion = null;
    
    @javax.inject.Inject()
    public LookupNotificationManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void notifyLookupResult(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.Nullable()
    com.example.callguardian.model.LookupResult lookupResult, @org.jetbrains.annotations.Nullable()
    com.example.callguardian.data.db.PhoneProfileEntity existingProfile, @org.jetbrains.annotations.Nullable()
    com.example.callguardian.model.AiAssessment aiAssessment, @org.jetbrains.annotations.Nullable()
    com.example.callguardian.model.ContactInfo contactInfo, boolean includeRejectAction) {
    }
    
    public final void notifyBlockedCall(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.Nullable()
    com.example.callguardian.data.db.PhoneProfileEntity profile) {
    }
    
    private final androidx.core.app.NotificationCompat.Builder baseBuilder(java.lang.String phoneNumber) {
        return null;
    }
    
    private final android.app.PendingIntent actionPendingIntent(java.lang.String action, java.lang.String phoneNumber) {
        return null;
    }
    
    private final void ensureChannel() {
    }
    
    private final int mutableFlag() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/callguardian/notifications/LookupNotificationManager$Companion;", "", "()V", "CHANNEL_ID", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}