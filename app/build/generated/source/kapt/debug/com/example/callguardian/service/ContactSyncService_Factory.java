package com.example.callguardian.service;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ContactSyncService_Factory implements Factory<ContactSyncService> {
  private final Provider<Context> contextProvider;

  private final Provider<ContactLookupService> contactLookupServiceProvider;

  public ContactSyncService_Factory(Provider<Context> contextProvider,
      Provider<ContactLookupService> contactLookupServiceProvider) {
    this.contextProvider = contextProvider;
    this.contactLookupServiceProvider = contactLookupServiceProvider;
  }

  @Override
  public ContactSyncService get() {
    return newInstance(contextProvider.get(), contactLookupServiceProvider.get());
  }

  public static ContactSyncService_Factory create(Provider<Context> contextProvider,
      Provider<ContactLookupService> contactLookupServiceProvider) {
    return new ContactSyncService_Factory(contextProvider, contactLookupServiceProvider);
  }

  public static ContactSyncService newInstance(Context context,
      ContactLookupService contactLookupService) {
    return new ContactSyncService(context, contactLookupService);
  }
}
