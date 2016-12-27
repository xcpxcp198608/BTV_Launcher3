package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.data.ChannelData;
import com.wiatec.btv_launcher.data.IChannelData;
import com.wiatec.btv_launcher.data.IImage2Data;
import com.wiatec.btv_launcher.data.Image2Data;
import com.wiatec.btv_launcher.fragment.IFragment3;

import java.util.List;

/**
 * Created by patrick on 2016/12/27.
 */

public class Fragment3Presenter extends BasePresenter<IFragment3> {
    private IFragment3 iFragment3;
    private IChannelData iChannelData;
    private IImage2Data iImage2Data;

    public Fragment3Presenter(IFragment3 iFragment3) {
        this.iFragment3 = iFragment3;
        iImage2Data = new Image2Data();
        iChannelData = new ChannelData();
    }

    public void bind(){
        if(iImage2Data != null){
            iImage2Data.loadData(new IImage2Data.OnLoadListener() {
                @Override
                public void onSuccess(List<ImageInfo> list) {
                    iFragment3.loadImage2(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }

        if(iChannelData != null){
            iChannelData.loadData(new IChannelData.OnLoadListener() {
                @Override
                public void onSuccess(List<ChannelInfo> list) {
                    iFragment3.loadChannel(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
