package com.aptoide.uploader;

import android.app.Application;
import android.preference.PreferenceManager;
import com.aptoide.uploader.account.AptoideAccountManager;
import com.aptoide.uploader.account.network.AccountResponseMapper;
import com.aptoide.uploader.account.network.RetrofitAccountService;
import com.aptoide.uploader.account.persistence.SharedPreferencesAccountPersistence;
import com.aptoide.uploader.apps.AccountStoreNameProvider;
import com.aptoide.uploader.apps.AppsManager;
import com.aptoide.uploader.apps.PackageManagerProvider;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class UploaderApplication extends Application {

  private AptoideAccountManager accountManager;
  private AppsManager appsManager;

  public AptoideAccountManager getAccountManager() {
    if (accountManager == null) {
      final Retrofit retrofitV3 = new Retrofit.Builder().addCallAdapterFactory(
          RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
          .client(new OkHttpClient())
          .baseUrl("http://webservices.aptoide.com/")
          .addConverterFactory(MoshiConverterFactory.create())
          .build();

      final Retrofit retrofitV7 = new Retrofit.Builder().addCallAdapterFactory(
          RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
          .client(new OkHttpClient())
          .baseUrl("http://ws75.aptoide.com/")
          .addConverterFactory(MoshiConverterFactory.create())
          .build();

      accountManager = new AptoideAccountManager(
          new RetrofitAccountService(retrofitV3.create(RetrofitAccountService.ServiceV3.class),
              retrofitV7.create(RetrofitAccountService.ServiceV7.class),
              new AccountResponseMapper()),
          new SharedPreferencesAccountPersistence(PublishSubject.create(),
              PreferenceManager.getDefaultSharedPreferences(this), Schedulers.io()));
    }
    return accountManager;
  }

  public AppsManager getAppsManager() {
    if (appsManager == null) {
      appsManager = new AppsManager(new PackageManagerProvider(getPackageManager()),
          new AccountStoreNameProvider(getAccountManager()));
    }
    return appsManager;
  }
}