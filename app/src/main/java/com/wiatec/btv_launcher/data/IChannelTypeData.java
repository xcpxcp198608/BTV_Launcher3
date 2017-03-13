package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.ChannelTypeInfo;
import com.wiatec.btv_launcher.bean.DeviceInfo;

import java.util.List;

/**
 * Created by patrick on 2017/3/13.
 */

public interface IChannelTypeData {
    void loadData(OnLoadListener onLoadListener , DeviceInfo deviceInfo);
    interface OnLoadListener{
        void onSuccess(List<ChannelTypeInfo> list);
        void onFailure(String e);
    }
}
