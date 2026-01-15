package com.example.callguardian.di;

import com.example.callguardian.data.db.CallGuardianDatabase;
import com.example.callguardian.data.db.PhoneProfileDao;
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
public final class DatabaseModule_ProvidePhoneProfileDaoFactory implements Factory<PhoneProfileDao> {
  private final Provider<CallGuardianDatabase> dbProvider;

  public DatabaseModule_ProvidePhoneProfileDaoFactory(Provider<CallGuardianDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PhoneProfileDao get() {
    return providePhoneProfileDao(dbProvider.get());
  }

  public static DatabaseModule_ProvidePhoneProfileDaoFactory create(
      Provider<CallGuardianDatabase> dbProvider) {
    return new DatabaseModule_ProvidePhoneProfileDaoFactory(dbProvider);
  }

  public static PhoneProfileDao providePhoneProfileDao(CallGuardianDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePhoneProfileDao(db));
  }
}
