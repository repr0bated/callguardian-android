package com.example.callguardian.data.cache;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class LookupCacheManager_Factory implements Factory<LookupCacheManager> {
  @Override
  public LookupCacheManager get() {
    return newInstance();
  }

  public static LookupCacheManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static LookupCacheManager newInstance() {
    return new LookupCacheManager();
  }

  private static final class InstanceHolder {
    private static final LookupCacheManager_Factory INSTANCE = new LookupCacheManager_Factory();
  }
}
