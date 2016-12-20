package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.CloudImageInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by PX on 2016-12-01.
 */

public interface ICloudImageData {

    void loadData(OnLoadListener onLoadListener);
    interface OnLoadListener{
        void onSuccess (List<CloudImageInfo> list);
        void onFailure (String e);
    }
}
