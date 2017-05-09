package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-15.
 */

public interface ISplashImageData {
    void loadData(OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (ImageInfo imageInfo);
        void onFailure (String e);
    }
}
