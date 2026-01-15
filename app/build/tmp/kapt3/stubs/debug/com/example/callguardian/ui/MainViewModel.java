package com.example.callguardian.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J*\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u00182\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001aJ\u0006\u0010\u001e\u001a\u00020\u0013J\u000e\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\u0015J\u001e\u0010!\u001a\u00020\u00132\u0006\u0010\"\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u00152\u0006\u0010$\u001a\u00020\u0015J\u0016\u0010%\u001a\u00020\u00132\u0006\u0010&\u001a\u00020\u00152\u0006\u0010\'\u001a\u00020\u0015J\u000e\u0010(\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\u0015J\u0006\u0010)\u001a\u00020\u0013J\u0016\u0010*\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010+\u001a\u00020,R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000f\u00a8\u0006-"}, d2 = {"Lcom/example/callguardian/ui/MainViewModel;", "Landroidx/lifecycle/ViewModel;", "lookupRepository", "Lcom/example/callguardian/data/repository/LookupRepository;", "lookupPreferences", "Lcom/example/callguardian/data/settings/LookupPreferences;", "(Lcom/example/callguardian/data/repository/LookupRepository;Lcom/example/callguardian/data/settings/LookupPreferences;)V", "_contactSyncState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/callguardian/ui/ContactSyncUiState;", "_uiState", "Lcom/example/callguardian/ui/MainUiState;", "contactSyncState", "Lkotlinx/coroutines/flow/StateFlow;", "getContactSyncState", "()Lkotlinx/coroutines/flow/StateFlow;", "uiState", "getUiState", "analyzeContactSync", "", "phoneNumber", "", "applyContactSyncChanges", "contactInfo", "Lcom/example/callguardian/model/ContactInfo;", "approvedChanges", "", "Lcom/example/callguardian/service/ContactChange;", "newInfo", "Lcom/example/callguardian/service/ContactInfoField;", "resetContactSyncState", "saveAbstractKey", "value", "saveCustomEndpoint", "endpoint", "headerName", "headerValue", "saveHuggingFaceCredentials", "apiKey", "modelId", "saveNumLookupKey", "toggleDarkMode", "updateBlockMode", "blockMode", "Lcom/example/callguardian/model/BlockMode;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MainViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.data.repository.LookupRepository lookupRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.callguardian.data.settings.LookupPreferences lookupPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.callguardian.ui.MainUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.callguardian.ui.MainUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.callguardian.ui.ContactSyncUiState> _contactSyncState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.callguardian.ui.ContactSyncUiState> contactSyncState = null;
    
    @javax.inject.Inject()
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.repository.LookupRepository lookupRepository, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.data.settings.LookupPreferences lookupPreferences) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.callguardian.ui.MainUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.callguardian.ui.ContactSyncUiState> getContactSyncState() {
        return null;
    }
    
    public final void updateBlockMode(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    com.example.callguardian.model.BlockMode blockMode) {
    }
    
    public final void saveNumLookupKey(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void saveAbstractKey(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void saveCustomEndpoint(@org.jetbrains.annotations.NotNull()
    java.lang.String endpoint, @org.jetbrains.annotations.NotNull()
    java.lang.String headerName, @org.jetbrains.annotations.NotNull()
    java.lang.String headerValue) {
    }
    
    public final void saveHuggingFaceCredentials(@org.jetbrains.annotations.NotNull()
    java.lang.String apiKey, @org.jetbrains.annotations.NotNull()
    java.lang.String modelId) {
    }
    
    public final void analyzeContactSync(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber) {
    }
    
    public final void applyContactSyncChanges(@org.jetbrains.annotations.NotNull()
    com.example.callguardian.model.ContactInfo contactInfo, @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.callguardian.service.ContactChange> approvedChanges, @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.callguardian.service.ContactInfoField> newInfo) {
    }
    
    public final void resetContactSyncState() {
    }
    
    public final void toggleDarkMode() {
    }
}