package com.example.callguardian.notifications;

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
public final class LookupNotificationManager_Factory implements Factory<LookupNotificationManager> {
  private final Provider<Context> contextProvider;

  public LookupNotificationManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LookupNotificationManager get() {
    return newInstance(contextProvider.get());
  }

  public static LookupNotificationManager_Factory create(Provider<Context> contextProvider) {
    return new LookupNotificationManager_Factory(contextProvider);
  }

  public static LookupNotificationManager newInstance(Context context) {
    return new LookupNotificationManager(context);
  }
}
