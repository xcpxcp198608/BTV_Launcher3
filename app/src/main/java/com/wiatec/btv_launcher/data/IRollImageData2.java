package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.RollImageInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-21.
 */

public interface IRollImageData2 {

    void loadData(OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (List<RollImageInfo> list);
        void onFailure (String e);
    }
}
