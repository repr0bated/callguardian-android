package com.example.callguardian.ai;

import com.example.callguardian.data.settings.LookupPreferences;
import com.example.callguardian.util.network.NetworkManager;
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
public final class AiRiskScorer_Factory implements Factory<AiRiskScorer> {
  private final Provider<LookupPreferences> preferencesProvider;

  private final Provider<OkHttpClient> clientProvider;

  private final Provider<NetworkManager> networkManagerProvider;

  private final Provider<Moshi> moshiProvider;

  public AiRiskScorer_Factory(Provider<LookupPreferences> preferencesProvider,
      Provider<OkHttpClient> clientProvider, Provider<NetworkManager> networkManagerProvider,
      Provider<Moshi> moshiProvider) {
    this.preferencesProvider = preferencesProvider;
    this.clientProvider = clientProvider;
    this.networkManagerProvider = networkManagerProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public AiRiskScorer get() {
    return newInstance(preferencesProvider.get(), clientProvider.get(), networkManagerProvider.get(), moshiProvider.get());
  }

  public static AiRiskScorer_Factory create(Provider<LookupPreferences> preferencesProvider,
      Provider<OkHttpClient> clientProvider, Provider<NetworkManager> networkManagerProvider,
      Provider<Moshi> moshiProvider) {
    return new AiRiskScorer_Factory(preferencesProvider, clientProvider, networkManagerProvider, moshiProvider);
  }

  public static AiRiskScorer newInstance(LookupPreferences preferences, OkHttpClient client,
      NetworkManager networkManager, Moshi moshi) {
    return new AiRiskScorer(preferences, client, networkManager, moshi);
  }
}
