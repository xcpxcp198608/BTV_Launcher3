package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.Message1Info;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public interface IMessage1Data {
    void loadData (OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (List<Message1Info> list);
        void onFailure (String e);
    }
}
