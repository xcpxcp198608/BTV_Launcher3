package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by patrick on 2017/3/4.
 */

public interface IOpportunityData {

    void loadData(OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (List<ImageInfo> list);
        void onFailure (String e);
    }
}
