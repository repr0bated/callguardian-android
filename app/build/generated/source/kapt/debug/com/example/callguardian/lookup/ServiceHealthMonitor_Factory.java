package com.example.callguardian.lookup;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Set;
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
public final class ServiceHealthMonitor_Factory implements Factory<ServiceHealthMonitor> {
  private final Provider<Set<ReverseLookupSource>> sourcesProvider;

  public ServiceHealthMonitor_Factory(Provider<Set<ReverseLookupSource>> sourcesProvider) {
    this.sourcesProvider = sourcesProvider;
  }

  @Override
  public ServiceHealthMonitor get() {
    return newInstance(sourcesProvider.get());
  }

  public static ServiceHealthMonitor_Factory create(
      Provider<Set<ReverseLookupSource>> sourcesProvider) {
    return new ServiceHealthMonitor_Factory(sourcesProvider);
  }

  public static ServiceHealthMonitor newInstance(Set<ReverseLookupSource> sources) {
    return new ServiceHealthMonitor(sources);
  }
}
