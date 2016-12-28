package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.ChannelInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public interface IChannelData {
    void loadData(OnLoadListener onLoadListener);
    void showData(String country ,OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (List<ChannelInfo> list);
        void onFailure (String e);
    }
}
