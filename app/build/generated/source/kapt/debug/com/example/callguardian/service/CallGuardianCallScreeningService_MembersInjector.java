package com.example.callguardian.service;

import com.example.callguardian.data.repository.LookupRepository;
import com.example.callguardian.notifications.LookupNotificationManager;
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
public final class CallGuardianCallScreeningService_MembersInjector implements MembersInjector<CallGuardianCallScreeningService> {
  private final Provider<LookupRepository> lookupRepositoryProvider;

  private final Provider<LookupNotificationManager> notificationManagerProvider;

  public CallGuardianCallScreeningService_MembersInjector(
      Provider<LookupRepository> lookupRepositoryProvider,
      Provider<LookupNotificationManager> notificationManagerProvider) {
    this.lookupRepositoryProvider = lookupRepositoryProvider;
    this.notificationManagerProvider = notificationManagerProvider;
  }

  public static MembersInjector<CallGuardianCallScreeningService> create(
      Provider<LookupRepository> lookupRepositoryProvider,
      Provider<LookupNotificationManager> notificationManagerProvider) {
    return new CallGuardianCallScreeningService_MembersInjector(lookupRepositoryProvider, notificationManagerProvider);
  }

  @Override
  public void injectMembers(CallGuardianCallScreeningService instance) {
    injectLookupRepository(instance, lookupRepositoryProvider.get());
    injectNotificationManager(instance, notificationManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.callguardian.service.CallGuardianCallScreeningService.lookupRepository")
  public static void injectLookupRepository(CallGuardianCallScreeningService instance,
      LookupRepository lookupRepository) {
    instance.lookupRepository = lookupRepository;
  }

  @InjectedFieldSignature("com.example.callguardian.service.CallGuardianCallScreeningService.notificationManager")
  public static void injectNotificationManager(CallGuardianCallScreeningService instance,
      LookupNotificationManager notificationManager) {
    instance.notificationManager = notificationManager;
  }
}
