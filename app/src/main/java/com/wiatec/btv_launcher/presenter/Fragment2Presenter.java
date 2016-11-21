package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.RollImageInfo;
import com.wiatec.btv_launcher.data.ChannelData;
import com.wiatec.btv_launcher.data.IChannelData;
import com.wiatec.btv_launcher.data.IImage2Data;
import com.wiatec.btv_launcher.data.IRollImageData2;
import com.wiatec.btv_launcher.data.Image2Data;
import com.wiatec.btv_launcher.data.RollImageData2;
import com.wiatec.btv_launcher.fragment.IFragment2;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class Fragment2Presenter extends BasePresenter<IFragment2>{
    private IFragment2 iFragment2;
    private IChannelData iChannelData;
    private IImage2Data iImage2Data;
    private IRollImageData2 iRollImageData2;

    public Fragment2Presenter(IFragment2 iFragment2) {
        this.iFragment2 = iFragment2;
        iChannelData = new ChannelData();
        iImage2Data = new Image2Data();
        iRollImageData2 = new RollImageData2();
    }

    public void loadData(){
        if(iChannelData !=null){
            iChannelData.loadData(new IChannelData.OnLoadListener() {
                @Override
                public void onSuccess(List<ChannelInfo> list) {
                    iFragment2.loadChannel(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }

        if(iImage2Data !=null ){
            iImage2Data.loadData(new IImage2Data.OnLoadListener() {
                @Override
                public void onSuccess(List<ImageInfo> list) {
                    iFragment2.loadImage2(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }

        if(iRollImageData2 != null){
            iRollImageData2.loadData(new IRollImageData2.OnLoadListener() {
                @Override
                public void onSuccess(List<RollImageInfo> list) {
                    iFragment2.loadRollImage2(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
