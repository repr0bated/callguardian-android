package com.example.callguardian.ui;

import com.example.callguardian.data.repository.LookupRepository;
import com.example.callguardian.data.settings.LookupPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<LookupRepository> lookupRepositoryProvider;

  private final Provider<LookupPreferences> lookupPreferencesProvider;

  public MainViewModel_Factory(Provider<LookupRepository> lookupRepositoryProvider,
      Provider<LookupPreferences> lookupPreferencesProvider) {
    this.lookupRepositoryProvider = lookupRepositoryProvider;
    this.lookupPreferencesProvider = lookupPreferencesProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(lookupRepositoryProvider.get(), lookupPreferencesProvider.get());
  }

  public static MainViewModel_Factory create(Provider<LookupRepository> lookupRepositoryProvider,
      Provider<LookupPreferences> lookupPreferencesProvider) {
    return new MainViewModel_Factory(lookupRepositoryProvider, lookupPreferencesProvider);
  }

  public static MainViewModel newInstance(LookupRepository lookupRepository,
      LookupPreferences lookupPreferences) {
    return new MainViewModel(lookupRepository, lookupPreferences);
  }
}
