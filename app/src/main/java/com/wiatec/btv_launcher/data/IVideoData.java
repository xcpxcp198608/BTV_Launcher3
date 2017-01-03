package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.VideoInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public interface IVideoData {
    void loadData (OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (List<VideoInfo> list);
        void onFailure (String e);
    }
}
