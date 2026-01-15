package com.example.callguardian.lookup;

import com.example.callguardian.data.cache.LookupCacheManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Set;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineDispatcher;

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
public final class ReverseLookupManager_Factory implements Factory<ReverseLookupManager> {
  private final Provider<Set<ReverseLookupSource>> sourcesProvider;

  private final Provider<LookupCacheManager> cacheManagerProvider;

  private final Provider<ServiceHealthMonitor> healthMonitorProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public ReverseLookupManager_Factory(Provider<Set<ReverseLookupSource>> sourcesProvider,
      Provider<LookupCacheManager> cacheManagerProvider,
      Provider<ServiceHealthMonitor> healthMonitorProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.sourcesProvider = sourcesProvider;
    this.cacheManagerProvider = cacheManagerProvider;
    this.healthMonitorProvider = healthMonitorProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public ReverseLookupManager get() {
    return newInstance(sourcesProvider.get(), cacheManagerProvider.get(), healthMonitorProvider.get(), ioDispatcherProvider.get());
  }

  public static ReverseLookupManager_Factory create(
      Provider<Set<ReverseLookupSource>> sourcesProvider,
      Provider<LookupCacheManager> cacheManagerProvider,
      Provider<ServiceHealthMonitor> healthMonitorProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new ReverseLookupManager_Factory(sourcesProvider, cacheManagerProvider, healthMonitorProvider, ioDispatcherProvider);
  }

  public static ReverseLookupManager newInstance(Set<ReverseLookupSource> sources,
      LookupCacheManager cacheManager, ServiceHealthMonitor healthMonitor,
      CoroutineDispatcher ioDispatcher) {
    return new ReverseLookupManager(sources, cacheManager, healthMonitor, ioDispatcher);
  }
}
