package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.VideoInfo;

/**
 * Created by PX on 2016-11-25.
 */

public interface IAdVideoData {
    void loadData (OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (VideoInfo videoInfo);
        void onFailure (String e);
    }
}
