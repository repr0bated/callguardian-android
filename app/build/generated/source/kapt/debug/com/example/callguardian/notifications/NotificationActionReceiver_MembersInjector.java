package com.example.callguardian.notifications;

import com.example.callguardian.data.repository.LookupRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class NotificationActionReceiver_MembersInjector implements MembersInjector<NotificationActionReceiver> {
  private final Provider<LookupRepository> lookupRepositoryProvider;

  public NotificationActionReceiver_MembersInjector(
      Provider<LookupRepository> lookupRepositoryProvider) {
    this.lookupRepositoryProvider = lookupRepositoryProvider;
  }

  public static MembersInjector<NotificationActionReceiver> create(
      Provider<LookupRepository> lookupRepositoryProvider) {
    return new NotificationActionReceiver_MembersInjector(lookupRepositoryProvider);
  }

  @Override
  public void injectMembers(NotificationActionReceiver instance) {
    injectLookupRepository(instance, lookupRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.example.callguardian.notifications.NotificationActionReceiver.lookupRepository")
  public static void injectLookupRepository(NotificationActionReceiver instance,
      LookupRepository lookupRepository) {
    instance.lookupRepository = lookupRepository;
  }
}
