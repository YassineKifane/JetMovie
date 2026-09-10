package com.example.jetmovie.ui.account;

import com.google.firebase.auth.FirebaseAuth;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AccountViewModel_Factory implements Factory<AccountViewModel> {
  private final Provider<FirebaseAuth> authProvider;

  public AccountViewModel_Factory(Provider<FirebaseAuth> authProvider) {
    this.authProvider = authProvider;
  }

  @Override
  public AccountViewModel get() {
    return newInstance(authProvider.get());
  }

  public static AccountViewModel_Factory create(Provider<FirebaseAuth> authProvider) {
    return new AccountViewModel_Factory(authProvider);
  }

  public static AccountViewModel newInstance(FirebaseAuth auth) {
    return new AccountViewModel(auth);
  }
}
