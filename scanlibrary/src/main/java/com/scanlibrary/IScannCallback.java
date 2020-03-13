package com.scanlibrary;

import android.net.Uri;

/**
 * Created by cecano@compartamos.com
 * on 10/03/2020.
 */
public interface IScannCallback {

    void onBitmapSelect(Uri uri);

    void onScanFinish(Uri uri);
}
