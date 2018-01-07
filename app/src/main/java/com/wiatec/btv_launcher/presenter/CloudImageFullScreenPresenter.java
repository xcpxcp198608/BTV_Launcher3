package com.wiatec.btv_launcher.presenter;

import com.px.common.utils.Logger;
import com.wiatec.btv_launcher.Activity.ICloudImageFullScreenActivity;
import com.wiatec.btv_launcher.data.CloudImageData;
import com.wiatec.btv_launcher.data.ICloudImageData;

import java.util.List;

/**
 * Created by patrick on 2017/2/22.
 */

public class CloudImageFullScreenPresenter extends BasePresenter<ICloudImageFullScreenActivity> {

    private ICloudImageFullScreenActivity iCloudImageFullScreenActivity ;
    private ICloudImageData iCloudImageData;

    public CloudImageFullScreenPresenter(ICloudImageFullScreenActivity iCloudImageFullScreenActivity) {
        this.iCloudImageFullScreenActivity = iCloudImageFullScreenActivity;
        iCloudImageData = new CloudImageData();
    }

    public void loadImages(){
        if(iCloudImageData != null){
            iCloudImageData.loadData(new ICloudImageData.OnLoadListener() {
                @Override
                public void onSuccess(List<String> list) {
                    iCloudImageFullScreenActivity.loadImages(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
