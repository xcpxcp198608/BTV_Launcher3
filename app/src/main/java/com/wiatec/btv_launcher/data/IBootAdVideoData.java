package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.VideoInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public interface IBootAdVideoData {
    void loadData (OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (VideoInfo videoInfo);
        void onFailure (String e);
    }
}
