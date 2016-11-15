package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.RollImageInfo;
import com.wiatec.btv_launcher.data.IImageData;
import com.wiatec.btv_launcher.data.IRollImageData;
import com.wiatec.btv_launcher.data.ImageData;
import com.wiatec.btv_launcher.data.RollImageData;
import com.wiatec.btv_launcher.fragment.IFragment1;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class Fragment1Presenter extends BasePresenter<IFragment1> {
    private IFragment1 iFragment1;
    private IImageData iImageData;
    private IRollImageData iRollImageData;

    public Fragment1Presenter(IFragment1 iFragment1) {
        this.iFragment1 = iFragment1;
        iImageData = new ImageData();
        iRollImageData = new RollImageData();
    }

    public void loadData(){
        if(iImageData !=null){
            iImageData.loadData(new IImageData.OnLoadListener() {
                @Override
                public void onSuccess(List<ImageInfo> list) {
                    iFragment1.loadImage(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }

        if(iRollImageData != null){
            iRollImageData.loadData(new IRollImageData.OnLoadListener() {
                @Override
                public void onSuccess(List<RollImageInfo> list) {
                    iFragment1.loadRollImage(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
