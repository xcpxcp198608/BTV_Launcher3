package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.RollImageInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public interface IRollImageData {
    void loadData(OnLoadListener onLoadListener);
    interface OnLoadListener{
        void onSuccess (List<ImageInfo> list);
        void onFailure (String e);
    }
}
