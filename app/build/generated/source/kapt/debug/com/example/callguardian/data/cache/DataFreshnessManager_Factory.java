package com.example.callguardian.data.cache;

import com.example.callguardian.data.db.PhoneProfileDao;
import com.example.callguardian.lookup.ReverseLookupManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DataFreshnessManager_Factory implements Factory<DataFreshnessManager> {
  private final Provider<LookupCacheManager> cacheManagerProvider;

  private final Provider<PhoneProfileDao> profileDaoProvider;

  private final Provider<ReverseLookupManager> reverseLookupManagerProvider;

  public DataFreshnessManager_Factory(Provider<LookupCacheManager> cacheManagerProvider,
      Provider<PhoneProfileDao> profileDaoProvider,
      Provider<ReverseLookupManager> reverseLookupManagerProvider) {
    this.cacheManagerProvider = cacheManagerProvider;
    this.profileDaoProvider = profileDaoProvider;
    this.reverseLookupManagerProvider = reverseLookupManagerProvider;
  }

  @Override
  public DataFreshnessManager get() {
    return newInstance(cacheManagerProvider.get(), profileDaoProvider.get(), reverseLookupManagerProvider.get());
  }

  public static DataFreshnessManager_Factory create(
      Provider<LookupCacheManager> cacheManagerProvider,
      Provider<PhoneProfileDao> profileDaoProvider,
      Provider<ReverseLookupManager> reverseLookupManagerProvider) {
    return new DataFreshnessManager_Factory(cacheManagerProvider, profileDaoProvider, reverseLookupManagerProvider);
  }

  public static DataFreshnessManager newInstance(LookupCacheManager cacheManager,
      PhoneProfileDao profileDao, ReverseLookupManager reverseLookupManager) {
    return new DataFreshnessManager(cacheManager, profileDao, reverseLookupManager);
  }
}
