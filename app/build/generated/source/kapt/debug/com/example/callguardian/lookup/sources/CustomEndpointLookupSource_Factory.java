package com.example.callguardian.lookup.sources;

import com.example.callguardian.data.settings.LookupPreferences;
import com.squareup.moshi.Moshi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
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
public final class CustomEndpointLookupSource_Factory implements Factory<CustomEndpointLookupSource> {
  private final Provider<LookupPreferences> preferencesProvider;

  private final Provider<OkHttpClient> clientProvider;

  private final Provider<Moshi> moshiProvider;

  public CustomEndpointLookupSource_Factory(Provider<LookupPreferences> preferencesProvider,
      Provider<OkHttpClient> clientProvider, Provider<Moshi> moshiProvider) {
    this.preferencesProvider = preferencesProvider;
    this.clientProvider = clientProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public CustomEndpointLookupSource get() {
    return newInstance(preferencesProvider.get(), clientProvider.get(), moshiProvider.get());
  }

  public static CustomEndpointLookupSource_Factory create(
      Provider<LookupPreferences> preferencesProvider, Provider<OkHttpClient> clientProvider,
      Provider<Moshi> moshiProvider) {
    return new CustomEndpointLookupSource_Factory(preferencesProvider, clientProvider, moshiProvider);
  }

  public static CustomEndpointLookupSource newInstance(LookupPreferences preferences,
      OkHttpClient client, Moshi moshi) {
    return new CustomEndpointLookupSource(preferences, client, moshi);
  }
}
