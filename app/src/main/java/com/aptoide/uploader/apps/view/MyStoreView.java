package com.aptoide.uploader.apps.view;

import com.aptoide.uploader.apps.App;
import com.aptoide.uploader.view.View;
import io.reactivex.Observable;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface MyStoreView extends View {

  void showApps(@NotNull List<App> appsList);

  void showStoreName(@NotNull String storeName);

  Observable<List<App>> submitAppEvent();

}