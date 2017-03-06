package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by patrick on 2017/3/6.
 */

public interface IManualData {

    void loadData(OnLoadListener onLoadListener , String product, String language);
    interface OnLoadListener{
        void onSuccess (List<ImageInfo> list);
        void onFailure (String e);
    }
}
