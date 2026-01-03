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
public final class ContactLookupService_Factory implements Factory<ContactLookupService> {
  private final Provider<Context> contextProvider;

  public ContactLookupService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ContactLookupService get() {
    return newInstance(contextProvider.get());
  }

  public static ContactLookupService_Factory create(Provider<Context> contextProvider) {
    return new ContactLookupService_Factory(contextProvider);
  }

  public static ContactLookupService newInstance(Context context) {
    return new ContactLookupService(context);
  }
}
