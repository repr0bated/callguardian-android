package com.example.callguardian.data.repository;

import com.example.callguardian.ai.AiRiskScorer;
import com.example.callguardian.data.db.PhoneInteractionDao;
import com.example.callguardian.data.db.PhoneProfileDao;
import com.example.callguardian.lookup.ReverseLookupManager;
import com.example.callguardian.service.ContactLookupService;
import com.example.callguardian.service.ContactSyncService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class LookupRepository_Factory implements Factory<LookupRepository> {
  private final Provider<ReverseLookupManager> reverseLookupManagerProvider;

  private final Provider<PhoneProfileDao> phoneProfileDaoProvider;

  private final Provider<PhoneInteractionDao> interactionDaoProvider;

  private final Provider<AiRiskScorer> aiRiskScorerProvider;

  private final Provider<ContactLookupService> contactLookupServiceProvider;

  private final Provider<ContactSyncService> contactSyncServiceProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public LookupRepository_Factory(Provider<ReverseLookupManager> reverseLookupManagerProvider,
      Provider<PhoneProfileDao> phoneProfileDaoProvider,
      Provider<PhoneInteractionDao> interactionDaoProvider,
      Provider<AiRiskScorer> aiRiskScorerProvider,
      Provider<ContactLookupService> contactLookupServiceProvider,
      Provider<ContactSyncService> contactSyncServiceProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.reverseLookupManagerProvider = reverseLookupManagerProvider;
    this.phoneProfileDaoProvider = phoneProfileDaoProvider;
    this.interactionDaoProvider = interactionDaoProvider;
    this.aiRiskScorerProvider = aiRiskScorerProvider;
    this.contactLookupServiceProvider = contactLookupServiceProvider;
    this.contactSyncServiceProvider = contactSyncServiceProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public LookupRepository get() {
    return newInstance(reverseLookupManagerProvider.get(), phoneProfileDaoProvider.get(), interactionDaoProvider.get(), aiRiskScorerProvider.get(), contactLookupServiceProvider.get(), contactSyncServiceProvider.get(), ioDispatcherProvider.get());
  }

  public static LookupRepository_Factory create(
      Provider<ReverseLookupManager> reverseLookupManagerProvider,
      Provider<PhoneProfileDao> phoneProfileDaoProvider,
      Provider<PhoneInteractionDao> interactionDaoProvider,
      Provider<AiRiskScorer> aiRiskScorerProvider,
      Provider<ContactLookupService> contactLookupServiceProvider,
      Provider<ContactSyncService> contactSyncServiceProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new LookupRepository_Factory(reverseLookupManagerProvider, phoneProfileDaoProvider, interactionDaoProvider, aiRiskScorerProvider, contactLookupServiceProvider, contactSyncServiceProvider, ioDispatcherProvider);
  }

  public static LookupRepository newInstance(ReverseLookupManager reverseLookupManager,
      PhoneProfileDao phoneProfileDao, PhoneInteractionDao interactionDao,
      AiRiskScorer aiRiskScorer, ContactLookupService contactLookupService,
      ContactSyncService contactSyncService, CoroutineDispatcher ioDispatcher) {
    return new LookupRepository(reverseLookupManager, phoneProfileDao, interactionDao, aiRiskScorer, contactLookupService, contactSyncService, ioDispatcher);
  }
}
