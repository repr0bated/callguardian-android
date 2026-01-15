package com.example.callguardian.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0007\u0003\u0004\u0005\u0006\u0007\b\tB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0007\n\u000b\f\r\u000e\u000f\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncUiState;", "", "()V", "Analyzing", "Applying", "Error", "Idle", "NoChanges", "Success", "SyncAvailable", "Lcom/example/callguardian/ui/ContactSyncUiState$Analyzing;", "Lcom/example/callguardian/ui/ContactSyncUiState$Applying;", "Lcom/example/callguardian/ui/ContactSyncUiState$Error;", "Lcom/example/callguardian/ui/ContactSyncUiState$Idle;", "Lcom/example/callguardian/ui/ContactSyncUiState$NoChanges;", "Lcom/example/callguardian/ui/ContactSyncUiState$Success;", "Lcom/example/callguardian/ui/ContactSyncUiState$SyncAvailable;", "app_debug"})
public abstract class ContactSyncUiState {
    
    private ContactSyncUiState() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncUiState$Analyzing;", "Lcom/example/callguardian/ui/ContactSyncUiState;", "()V", "app_debug"})
    public static final class Analyzing extends com.example.callguardian.ui.ContactSyncUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.callguardian.ui.ContactSyncUiState.Analyzing INSTANCE = null;
        
        private Analyzing() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncUiState$Applying;", "Lcom/example/callguardian/ui/ContactSyncUiState;", "()V", "app_debug"})
    public static final class Applying extends com.example.callguardian.ui.ContactSyncUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.callguardian.ui.ContactSyncUiState.Applying INSTANCE = null;
        
        private Applying() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncUiState$Error;", "Lcom/example/callguardian/ui/ContactSyncUiState;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class Error extends com.example.callguardian.ui.ContactSyncUiState {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String message = null;
        
        public Error(@org.jetbrains.annotations.NotNull()
        java.lang.String message) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMessage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.ui.ContactSyncUiState.Error copy(@org.jetbrains.annotations.NotNull()
        java.lang.String message) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncUiState$Idle;", "Lcom/example/callguardian/ui/ContactSyncUiState;", "()V", "app_debug"})
    public static final class Idle extends com.example.callguardian.ui.ContactSyncUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.callguardian.ui.ContactSyncUiState.Idle INSTANCE = null;
        
        private Idle() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncUiState$NoChanges;", "Lcom/example/callguardian/ui/ContactSyncUiState;", "()V", "app_debug"})
    public static final class NoChanges extends com.example.callguardian.ui.ContactSyncUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.callguardian.ui.ContactSyncUiState.NoChanges INSTANCE = null;
        
        private NoChanges() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncUiState$Success;", "Lcom/example/callguardian/ui/ContactSyncUiState;", "()V", "app_debug"})
    public static final class Success extends com.example.callguardian.ui.ContactSyncUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.callguardian.ui.ContactSyncUiState.Success INSTANCE = null;
        
        private Success() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncUiState$SyncAvailable;", "Lcom/example/callguardian/ui/ContactSyncUiState;", "syncResult", "Lcom/example/callguardian/service/ContactSyncResult$ChangesDetected;", "(Lcom/example/callguardian/service/ContactSyncResult$ChangesDetected;)V", "getSyncResult", "()Lcom/example/callguardian/service/ContactSyncResult$ChangesDetected;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class SyncAvailable extends com.example.callguardian.ui.ContactSyncUiState {
        @org.jetbrains.annotations.NotNull()
        private final com.example.callguardian.service.ContactSyncResult.ChangesDetected syncResult = null;
        
        public SyncAvailable(@org.jetbrains.annotations.NotNull()
        com.example.callguardian.service.ContactSyncResult.ChangesDetected syncResult) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.service.ContactSyncResult.ChangesDetected getSyncResult() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.service.ContactSyncResult.ChangesDetected component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.ui.ContactSyncUiState.SyncAvailable copy(@org.jetbrains.annotations.NotNull()
        com.example.callguardian.service.ContactSyncResult.ChangesDetected syncResult) {
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