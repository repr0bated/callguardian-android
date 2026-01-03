package com.example.callguardian.service;

import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import error.NonExistentClass;
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
  private final Provider<NonExistentClass> notificationManagerProvider;

  public CallGuardianCallScreeningService_MembersInjector(
      Provider<NonExistentClass> notificationManagerProvider) {
    this.notificationManagerProvider = notificationManagerProvider;
  }

  public static MembersInjector<CallGuardianCallScreeningService> create(
      Provider<NonExistentClass> notificationManagerProvider) {
    return new CallGuardianCallScreeningService_MembersInjector(notificationManagerProvider);
  }

  @Override
  public void injectMembers(CallGuardianCallScreeningService instance) {
    injectNotificationManager(instance, notificationManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.callguardian.service.CallGuardianCallScreeningService.notificationManager")
  public static void injectNotificationManager(CallGuardianCallScreeningService instance,
      NonExistentClass notificationManager) {
    instance.notificationManager = notificationManager;
  }
}
