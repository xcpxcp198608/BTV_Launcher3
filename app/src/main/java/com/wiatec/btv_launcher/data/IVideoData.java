package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.VideoInfo;

import java.util.List;

/**
 * Created by patrick on 2017/2/27.
 */

public interface IVideoData {
    void loadData (OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (List<VideoInfo> list);
        void onFailure (String e);
    }
}
