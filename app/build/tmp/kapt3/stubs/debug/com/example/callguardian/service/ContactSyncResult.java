package com.example.callguardian.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0003\u0004B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0002\u0005\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/callguardian/service/ContactSyncResult;", "Landroid/os/Parcelable;", "()V", "ChangesDetected", "NoChanges", "Lcom/example/callguardian/service/ContactSyncResult$ChangesDetected;", "Lcom/example/callguardian/service/ContactSyncResult$NoChanges;", "app_debug"})
public abstract class ContactSyncResult implements android.os.Parcelable {
    
    private ContactSyncResult() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B6\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u000b\u0010\t\u001a\u00070\n\u00a2\u0006\u0002\b\u000b\u00a2\u0006\u0002\u0010\fJ\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\bH\u00c6\u0003J\u000e\u0010\u0017\u001a\u00070\n\u00a2\u0006\u0002\b\u000bH\u00c6\u0003JB\u0010\u0018\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\r\b\u0002\u0010\t\u001a\u00070\n\u00a2\u0006\u0002\b\u000bH\u00c6\u0001J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u00d6\u0003J\t\u0010\u001f\u001a\u00020\u001aH\u00d6\u0001J\t\u0010 \u001a\u00020!H\u00d6\u0001J\u0019\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u001aH\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\t\u001a\u00070\n\u00a2\u0006\u0002\b\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000e\u00a8\u0006\'"}, d2 = {"Lcom/example/callguardian/service/ContactSyncResult$ChangesDetected;", "Lcom/example/callguardian/service/ContactSyncResult;", "changes", "", "Lcom/example/callguardian/service/ContactChange;", "newInfo", "Lcom/example/callguardian/service/ContactInfoField;", "existingContact", "Lcom/example/callguardian/service/ExistingContactDetails;", "contactInfo", "Lcom/example/callguardian/model/ContactInfo;", "Lkotlinx/parcelize/RawValue;", "(Ljava/util/List;Ljava/util/List;Lcom/example/callguardian/service/ExistingContactDetails;Lcom/example/callguardian/model/ContactInfo;)V", "getChanges", "()Ljava/util/List;", "getContactInfo", "()Lcom/example/callguardian/model/ContactInfo;", "getExistingContact", "()Lcom/example/callguardian/service/ExistingContactDetails;", "getNewInfo", "component1", "component2", "component3", "component4", "copy", "describeContents", "", "equals", "", "other", "", "hashCode", "toString", "", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
    @kotlinx.parcelize.Parcelize()
    public static final class ChangesDetected extends com.example.callguardian.service.ContactSyncResult {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.example.callguardian.service.ContactChange> changes = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.example.callguardian.service.ContactInfoField> newInfo = null;
        @org.jetbrains.annotations.NotNull()
        private final com.example.callguardian.service.ExistingContactDetails existingContact = null;
        @org.jetbrains.annotations.NotNull()
        private final com.example.callguardian.model.ContactInfo contactInfo = null;
        
        public ChangesDetected(@org.jetbrains.annotations.NotNull()
        java.util.List<com.example.callguardian.service.ContactChange> changes, @org.jetbrains.annotations.NotNull()
        java.util.List<com.example.callguardian.service.ContactInfoField> newInfo, @org.jetbrains.annotations.NotNull()
        com.example.callguardian.service.ExistingContactDetails existingContact, @org.jetbrains.annotations.NotNull()
        com.example.callguardian.model.ContactInfo contactInfo) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.example.callguardian.service.ContactChange> getChanges() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.example.callguardian.service.ContactInfoField> getNewInfo() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.service.ExistingContactDetails getExistingContact() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.model.ContactInfo getContactInfo() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.example.callguardian.service.ContactChange> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.example.callguardian.service.ContactInfoField> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.service.ExistingContactDetails component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.model.ContactInfo component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.service.ContactSyncResult.ChangesDetected copy(@org.jetbrains.annotations.NotNull()
        java.util.List<com.example.callguardian.service.ContactChange> changes, @org.jetbrains.annotations.NotNull()
        java.util.List<com.example.callguardian.service.ContactInfoField> newInfo, @org.jetbrains.annotations.NotNull()
        com.example.callguardian.service.ExistingContactDetails existingContact, @org.jetbrains.annotations.NotNull()
        com.example.callguardian.model.ContactInfo contactInfo) {
            return null;
        }
        
        @java.lang.Override()
        public int describeContents() {
            return 0;
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
        
        @java.lang.Override()
        public void writeToParcel(@org.jetbrains.annotations.NotNull()
        android.os.Parcel parcel, int flags) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\t\u0010\u0003\u001a\u00020\u0004H\u00d6\u0001J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004H\u00d6\u0001\u00a8\u0006\n"}, d2 = {"Lcom/example/callguardian/service/ContactSyncResult$NoChanges;", "Lcom/example/callguardian/service/ContactSyncResult;", "()V", "describeContents", "", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
    @kotlinx.parcelize.Parcelize()
    public static final class NoChanges extends com.example.callguardian.service.ContactSyncResult {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.callguardian.service.ContactSyncResult.NoChanges INSTANCE = null;
        
        private NoChanges() {
        }
        
        @java.lang.Override()
        public int describeContents() {
            return 0;
        }
        
        @java.lang.Override()
        public void writeToParcel(@org.jetbrains.annotations.NotNull()
        android.os.Parcel parcel, int flags) {
        }
    }
}