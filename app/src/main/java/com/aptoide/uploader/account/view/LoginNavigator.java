package com.aptoide.uploader.account.view;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import com.aptoide.uploader.ActivityNavigator;
import com.aptoide.uploader.R;
import com.aptoide.uploader.apps.view.MyStoreFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jakewharton.rxrelay2.PublishRelay;
import io.reactivex.Observable;
import java.util.Arrays;

public class LoginNavigator {

  private final Context context;
  private final FragmentManager fragmentManager;
  private final LoginManager facebookLoginManager;
  private final CallbackManager callbackManager;
  private final PublishRelay<FacebookLoginResult> facebookLoginSubject;
  private final FragmentActivity fragmentActivity;
  private final ActivityNavigator activityNavigator;

  public LoginNavigator(Context context, FragmentManager fragmentManager,
      LoginManager facebookLoginManager, CallbackManager callbackManager,
      PublishRelay<FacebookLoginResult> facebookLoginSubject, FragmentActivity fragmentActivity,
      ActivityNavigator activityNavigator) {
    this.context = context;
    this.fragmentManager = fragmentManager;
    this.facebookLoginManager = facebookLoginManager;
    this.callbackManager = callbackManager;
    this.facebookLoginSubject = facebookLoginSubject;
    this.fragmentActivity = fragmentActivity;
    this.activityNavigator = activityNavigator;
  }

  public void navigateToMyAppsView() {
    fragmentManager.beginTransaction()
        .replace(R.id.activity_main_container, MyStoreFragment.newInstance())
        .commit();
  }

  public void navigateToCreateStoreView() {
    Toast.makeText(context, "create store view not implemented", Toast.LENGTH_SHORT)
        .show();
  }

  public void navigateToCreateAccountView() {
    fragmentManager.beginTransaction()
        .replace(R.id.activity_main_container, CreateAccountFragment.newInstance())
        .commit();
  }

  public void navigateToFacebookSignUpForResult() {
    facebookLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override public void onSuccess(LoginResult loginResult) {
        facebookLoginSubject.accept(
            new FacebookLoginResult(loginResult, FacebookLoginResult.STATE_SUCCESS, null));
      }

      @Override public void onCancel() {
        facebookLoginSubject.accept(
            new FacebookLoginResult(null, FacebookLoginResult.STATE_CANCELLED, null));
      }

      @Override public void onError(FacebookException error) {
        facebookLoginSubject.accept(
            new FacebookLoginResult(null, FacebookLoginResult.STATE_ERROR, error));
      }
    });
    facebookLoginManager.logInWithReadPermissions(fragmentActivity, Arrays.asList("email"));
  }

  public Observable<FacebookLoginResult> facebookSignUpResults() {
    return Observable.combineLatest(activityNavigator.results()
            .filter(result -> callbackManager.onActivityResult(result.getRequestCode(),
                result.getResultCode(), result.getData())), facebookLoginSubject,
        (result, loginResult) -> loginResult);
  }
}
