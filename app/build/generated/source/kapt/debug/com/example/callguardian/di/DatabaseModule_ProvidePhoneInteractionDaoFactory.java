package com.example.callguardian.di;

import com.example.callguardian.data.db.CallGuardianDatabase;
import com.example.callguardian.data.db.PhoneInteractionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvidePhoneInteractionDaoFactory implements Factory<PhoneInteractionDao> {
  private final Provider<CallGuardianDatabase> dbProvider;

  public DatabaseModule_ProvidePhoneInteractionDaoFactory(
      Provider<CallGuardianDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PhoneInteractionDao get() {
    return providePhoneInteractionDao(dbProvider.get());
  }

  public static DatabaseModule_ProvidePhoneInteractionDaoFactory create(
      Provider<CallGuardianDatabase> dbProvider) {
    return new DatabaseModule_ProvidePhoneInteractionDaoFactory(dbProvider);
  }

  public static PhoneInteractionDao providePhoneInteractionDao(CallGuardianDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePhoneInteractionDao(db));
  }
}
