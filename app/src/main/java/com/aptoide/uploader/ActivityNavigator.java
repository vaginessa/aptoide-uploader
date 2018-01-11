package com.aptoide.uploader;

import io.reactivex.Observable;

/**
 * Created by diogoloureiro on 08/01/2018.
 */

public interface ActivityNavigator {

  Observable<Result> results();
}
