package com.aptoide.uploader.account.view;

import com.aptoide.uploader.account.AptoideAccountManager;
import io.reactivex.Completable;

public interface SignUpAdapter<T> {

  Completable signUp(T data, AptoideAccountManager aptoideAccountManager);

  Completable logout();

  boolean isEnabled();
}