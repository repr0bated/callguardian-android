package com.example.callguardian.sms;

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
public final class SmsReceiver_MembersInjector implements MembersInjector<SmsReceiver> {
  private final Provider<LookupRepository> lookupRepositoryProvider;

  private final Provider<LookupNotificationManager> notificationManagerProvider;

  public SmsReceiver_MembersInjector(Provider<LookupRepository> lookupRepositoryProvider,
      Provider<LookupNotificationManager> notificationManagerProvider) {
    this.lookupRepositoryProvider = lookupRepositoryProvider;
    this.notificationManagerProvider = notificationManagerProvider;
  }

  public static MembersInjector<SmsReceiver> create(
      Provider<LookupRepository> lookupRepositoryProvider,
      Provider<LookupNotificationManager> notificationManagerProvider) {
    return new SmsReceiver_MembersInjector(lookupRepositoryProvider, notificationManagerProvider);
  }

  @Override
  public void injectMembers(SmsReceiver instance) {
    injectLookupRepository(instance, lookupRepositoryProvider.get());
    injectNotificationManager(instance, notificationManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.callguardian.sms.SmsReceiver.lookupRepository")
  public static void injectLookupRepository(SmsReceiver instance,
      LookupRepository lookupRepository) {
    instance.lookupRepository = lookupRepository;
  }

  @InjectedFieldSignature("com.example.callguardian.sms.SmsReceiver.notificationManager")
  public static void injectNotificationManager(SmsReceiver instance,
      LookupNotificationManager notificationManager) {
    instance.notificationManager = notificationManager;
  }
}
