package com.aptoide.uploader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.aptoide.uploader.account.view.LoginFragment;
import com.jakewharton.rxrelay2.PublishRelay;
import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity implements ActivityNavigator {

  private PublishRelay<Result> resultRelay;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.activity_main_container, LoginFragment.newInstance())
          .commit();
    }

    resultRelay = PublishRelay.create();
  }

  @Override public Observable<Result> results() {
    return resultRelay;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    resultRelay.accept(new Result(requestCode, resultCode, data));
  }
}
