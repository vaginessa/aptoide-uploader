package com.aptoide.uploader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class AccountViewFragment extends Fragment implements AccountView {

  private EditText passwordEditText;
  private EditText usernameEditText;
  private Button loginButton;

  private Subject<LifecycleEvent> events;

  public static AccountViewFragment newInstance() {
    return new AccountViewFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    events = BehaviorSubject.create();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_login, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    passwordEditText = view.findViewById(R.id.fragment_login_password_edit_text);
    usernameEditText = view.findViewById(R.id.fragment_login_username_edit_text);
    loginButton = view.findViewById(R.id.fragment_login_button);

    events.onNext(LifecycleEvent.CREATE);
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

    final AptoideAccountManager accountManager = new AptoideAccountManager(
        new RetrofitAccountService(retrofitV3.create(RetrofitAccountService.ServiceV3.class),
            retrofitV7.create(RetrofitAccountService.ServiceV7.class),
            new AccountResponseMapper()));
    new AccountPresenter(this, accountManager, new AccountNavigator(getContext()),
        new CompositeDisposable(), AndroidSchedulers.mainThread()).present();
  }

  @Override public void onStart() {
    super.onStart();
    events.onNext(LifecycleEvent.START);
  }

  @Override public void onResume() {
    super.onResume();
    events.onNext(LifecycleEvent.RESUME);
  }

  @Override public void onPause() {
    events.onNext(LifecycleEvent.PAUSE);
    super.onPause();
  }

  @Override public void onStop() {
    events.onNext(LifecycleEvent.STOP);
    super.onStop();
  }

  @Override public void onDestroyView() {
    events.onNext(LifecycleEvent.DESTROY);
    super.onDestroyView();
  }

  @Override public Observable<LifecycleEvent> getLifecycleEvent() {
    return events;
  }

  @Override public Observable<CredentialsViewModel> getLoginEvent() {
    return RxView.clicks(loginButton)
        .map(__ -> new CredentialsViewModel(usernameEditText.getText()
            .toString(), passwordEditText.getText()
            .toString()));
  }
}
