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
public final class NumLookupApiSource_Factory implements Factory<NumLookupApiSource> {
  private final Provider<LookupPreferences> preferencesProvider;

  private final Provider<OkHttpClient> clientProvider;

  private final Provider<Moshi> moshiProvider;

  public NumLookupApiSource_Factory(Provider<LookupPreferences> preferencesProvider,
      Provider<OkHttpClient> clientProvider, Provider<Moshi> moshiProvider) {
    this.preferencesProvider = preferencesProvider;
    this.clientProvider = clientProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public NumLookupApiSource get() {
    return newInstance(preferencesProvider.get(), clientProvider.get(), moshiProvider.get());
  }

  public static NumLookupApiSource_Factory create(Provider<LookupPreferences> preferencesProvider,
      Provider<OkHttpClient> clientProvider, Provider<Moshi> moshiProvider) {
    return new NumLookupApiSource_Factory(preferencesProvider, clientProvider, moshiProvider);
  }

  public static NumLookupApiSource newInstance(LookupPreferences preferences, OkHttpClient client,
      Moshi moshi) {
    return new NumLookupApiSource(preferences, client, moshi);
  }
}
