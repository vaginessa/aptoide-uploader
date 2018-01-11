package com.aptoide.uploader.account;

import com.aptoide.uploader.account.view.FacebookLoginResult;
import com.aptoide.uploader.account.view.FacebookSignUpAdapter;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class AptoideAccountManager {

  public static final String TYPE_FACEBOOK = "FACEBOOK";
  public static final String TYPE_APTOIDE = "APTOIDE";
  private final AccountService accountService;
  private final AccountPersistence accountPersistence;
  private final FacebookSignUpAdapter facebookSignUpAdapter;

  public AptoideAccountManager(AccountService accountService, AccountPersistence accountPersistence,
      FacebookSignUpAdapter facebookSignUpAdapter) {
    this.accountService = accountService;
    this.accountPersistence = accountPersistence;
    this.facebookSignUpAdapter = facebookSignUpAdapter;
  }

  public Completable login(String type, String username, String password) {
    return accountService.getAccount(type, username, password)
        .flatMapCompletable(account -> accountPersistence.save(account));
  }

  public Completable login(FacebookLoginResult result) {
    return facebookSignUpAdapter.signUp(result, this);
  }

  public Observable<AptoideAccount> getAccount() {
    return accountPersistence.getAccount();
  }

  public Completable create(String email, String password, String storeName) {
    return accountService.createAccount(email, password, storeName)
        .flatMapCompletable(account -> accountPersistence.save(account));
  }

  public Completable logout() {
    return accountPersistence.remove();
  }
}