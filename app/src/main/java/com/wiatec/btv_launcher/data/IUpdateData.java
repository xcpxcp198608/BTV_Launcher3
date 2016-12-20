package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.UpdateInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public interface IUpdateData {
    void loadData(OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (UpdateInfo updateInfo);
        void onFailure (String e);
    }
}
