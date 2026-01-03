package com.example.callguardian.data.settings;

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
public final class LookupPreferences_Factory implements Factory<LookupPreferences> {
  private final Provider<Context> contextProvider;

  private final Provider<EncryptedPreferences> encryptedPreferencesProvider;

  public LookupPreferences_Factory(Provider<Context> contextProvider,
      Provider<EncryptedPreferences> encryptedPreferencesProvider) {
    this.contextProvider = contextProvider;
    this.encryptedPreferencesProvider = encryptedPreferencesProvider;
  }

  @Override
  public LookupPreferences get() {
    return newInstance(contextProvider.get(), encryptedPreferencesProvider.get());
  }

  public static LookupPreferences_Factory create(Provider<Context> contextProvider,
      Provider<EncryptedPreferences> encryptedPreferencesProvider) {
    return new LookupPreferences_Factory(contextProvider, encryptedPreferencesProvider);
  }

  public static LookupPreferences newInstance(Context context,
      EncryptedPreferences encryptedPreferences) {
    return new LookupPreferences(context, encryptedPreferences);
  }
}
