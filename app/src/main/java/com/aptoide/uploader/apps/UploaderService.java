package com.aptoide.uploader.apps;

import io.reactivex.Single;

public interface UploaderService {

  Single<Upload> getAppUpload(String md5, String packageName, String language,
      String storeName);
}
