package com.example.callguardian.ui;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010#\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0007\u0018\u0000 \"2\u00020\u0001:\u0005 !\"#$B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\u0012\u0010\u0016\u001a\u00020\u00152\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\u0012\u0010\u0019\u001a\u00020\u001a2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\b\u0010\u001b\u001a\u00020\u0015H\u0016J\b\u0010\u001c\u001a\u00020\u0015H\u0002J\b\u0010\u001d\u001a\u00020\u0015H\u0002J\b\u0010\u001e\u001a\u00020\u0015H\u0002J\b\u0010\u001f\u001a\u00020\u0015H\u0002R\u0010\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u0005\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006%"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncDialogFragment;", "Landroidx/fragment/app/DialogFragment;", "()V", "_binding", "error/NonExistentClass", "Lerror/NonExistentClass;", "binding", "getBinding", "()Lerror/NonExistentClass;", "selectedChanges", "", "", "selectedNewInfo", "syncResult", "Lcom/example/callguardian/service/ContactSyncResult$ChangesDetected;", "viewModel", "Lcom/example/callguardian/ui/MainViewModel;", "getViewModel", "()Lcom/example/callguardian/ui/MainViewModel;", "viewModel$delegate", "applySelectedChanges", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateDialog", "Landroid/app/Dialog;", "onDestroyView", "populateData", "setupButtons", "setupRecyclerViews", "updateButtonStates", "ChangeViewHolder", "ChangesAdapter", "Companion", "NewInfoAdapter", "NewInfoViewHolder", "app_debug"})
public final class ContactSyncDialogFragment extends androidx.fragment.app.DialogFragment {
    @org.jetbrains.annotations.NotNull()
    private final error.NonExistentClass viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private error.NonExistentClass _binding;
    private com.example.callguardian.service.ContactSyncResult.ChangesDetected syncResult;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.Integer> selectedChanges = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.Integer> selectedNewInfo = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ARG_SYNC_RESULT = "sync_result";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.callguardian.ui.ContactSyncDialogFragment.Companion Companion = null;
    
    public ContactSyncDialogFragment() {
        super();
    }
    
    private final com.example.callguardian.ui.MainViewModel getViewModel() {
        return null;
    }
    
    private final error.NonExistentClass getBinding() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.app.Dialog onCreateDialog(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    private final void setupRecyclerViews() {
    }
    
    private final void setupButtons() {
    }
    
    private final void populateData() {
    }
    
    private final void updateButtonStates() {
    }
    
    private final void applySelectedChanges() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u0010\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0005\u00a8\u0006\f"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncDialogFragment$ChangeViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "error/NonExistentClass", "(Lcom/example/callguardian/ui/ContactSyncDialogFragment;Lerror/NonExistentClass;)V", "Lerror/NonExistentClass;", "bind", "", "change", "Lcom/example/callguardian/service/ContactChange;", "position", "", "app_debug"})
    final class ChangeViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final error.NonExistentClass binding = null;
        
        public ChangeViewHolder(@org.jetbrains.annotations.NotNull()
        error.NonExistentClass binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.example.callguardian.service.ContactChange change, int position) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00030\u0001B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u001c\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u00060\u0002R\u00020\u00032\u0006\u0010\n\u001a\u00020\u0006H\u0016J\u001c\u0010\u000b\u001a\u00060\u0002R\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0006H\u0016\u00a8\u0006\u000f"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncDialogFragment$ChangesAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/callguardian/ui/ContactSyncDialogFragment$ChangeViewHolder;", "Lcom/example/callguardian/ui/ContactSyncDialogFragment;", "(Lcom/example/callguardian/ui/ContactSyncDialogFragment;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "app_debug"})
    final class ChangesAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.callguardian.ui.ContactSyncDialogFragment.ChangeViewHolder> {
        
        public ChangesAdapter() {
            super();
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.example.callguardian.ui.ContactSyncDialogFragment.ChangeViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override()
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.callguardian.ui.ContactSyncDialogFragment.ChangeViewHolder holder, int position) {
        }
        
        @java.lang.Override()
        public int getItemCount() {
            return 0;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncDialogFragment$Companion;", "", "()V", "ARG_SYNC_RESULT", "", "newInstance", "Lcom/example/callguardian/ui/ContactSyncDialogFragment;", "syncResult", "Lcom/example/callguardian/service/ContactSyncResult$ChangesDetected;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.callguardian.ui.ContactSyncDialogFragment newInstance(@org.jetbrains.annotations.NotNull()
        com.example.callguardian.service.ContactSyncResult.ChangesDetected syncResult) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00030\u0001B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u001c\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u00060\u0002R\u00020\u00032\u0006\u0010\n\u001a\u00020\u0006H\u0016J\u001c\u0010\u000b\u001a\u00060\u0002R\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0006H\u0016\u00a8\u0006\u000f"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncDialogFragment$NewInfoAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/callguardian/ui/ContactSyncDialogFragment$NewInfoViewHolder;", "Lcom/example/callguardian/ui/ContactSyncDialogFragment;", "(Lcom/example/callguardian/ui/ContactSyncDialogFragment;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "app_debug"})
    final class NewInfoAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.callguardian.ui.ContactSyncDialogFragment.NewInfoViewHolder> {
        
        public NewInfoAdapter() {
            super();
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.example.callguardian.ui.ContactSyncDialogFragment.NewInfoViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override()
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.callguardian.ui.ContactSyncDialogFragment.NewInfoViewHolder holder, int position) {
        }
        
        @java.lang.Override()
        public int getItemCount() {
            return 0;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u0010\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0005\u00a8\u0006\f"}, d2 = {"Lcom/example/callguardian/ui/ContactSyncDialogFragment$NewInfoViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "error/NonExistentClass", "(Lcom/example/callguardian/ui/ContactSyncDialogFragment;Lerror/NonExistentClass;)V", "Lerror/NonExistentClass;", "bind", "", "infoField", "Lcom/example/callguardian/service/ContactInfoField;", "position", "", "app_debug"})
    final class NewInfoViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final error.NonExistentClass binding = null;
        
        public NewInfoViewHolder(@org.jetbrains.annotations.NotNull()
        error.NonExistentClass binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.example.callguardian.service.ContactInfoField infoField, int position) {
        }
    }
}