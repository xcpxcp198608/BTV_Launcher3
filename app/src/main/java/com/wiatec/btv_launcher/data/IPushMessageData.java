package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.PushMessageInfo;

import java.util.List;

/**
 * Created by xuchengpeng on 27/04/2017.
 */

public interface IPushMessageData {

    void loadData(OnLoadListener onLoadListener);
    interface OnLoadListener{
        void onSuccess(List<PushMessageInfo> list);
        void onFailure(String e);
    }
}
