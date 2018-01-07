package com.wiatec.btv_launcher.presenter;

import com.px.common.utils.Logger;
import com.wiatec.btv_launcher.Activity.ISplashActivity;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.data.ISplashImageData;
import com.wiatec.btv_launcher.data.SplashImageData;

/**
 * Created by PX on 2016-11-14.
 */

public class SplashPresenter extends BasePresenter<ISplashActivity> {

    private ISplashActivity iSplashActivity;
    private ISplashImageData iSplashImageData;

    public SplashPresenter(ISplashActivity iSplashActivity) {
        this.iSplashActivity = iSplashActivity;
        iSplashImageData = new SplashImageData();
    }

    public void loadImage () {
        if(iSplashImageData != null){
            iSplashImageData.loadData(new ISplashImageData.OnLoadListener() {
                @Override
                public void onSuccess(ImageInfo imageInfo) {
                    iSplashActivity.loadImage(imageInfo);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
