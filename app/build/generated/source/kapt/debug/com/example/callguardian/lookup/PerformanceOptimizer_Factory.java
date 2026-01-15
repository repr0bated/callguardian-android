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
public final class PerformanceOptimizer_Factory implements Factory<PerformanceOptimizer> {
  private final Provider<Set<ReverseLookupSource>> sourcesProvider;

  public PerformanceOptimizer_Factory(Provider<Set<ReverseLookupSource>> sourcesProvider) {
    this.sourcesProvider = sourcesProvider;
  }

  @Override
  public PerformanceOptimizer get() {
    return newInstance(sourcesProvider.get());
  }

  public static PerformanceOptimizer_Factory create(
      Provider<Set<ReverseLookupSource>> sourcesProvider) {
    return new PerformanceOptimizer_Factory(sourcesProvider);
  }

  public static PerformanceOptimizer newInstance(Set<ReverseLookupSource> sources) {
    return new PerformanceOptimizer(sources);
  }
}
