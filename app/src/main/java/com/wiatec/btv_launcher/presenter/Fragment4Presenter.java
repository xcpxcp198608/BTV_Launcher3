package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.data.ChannelData;
import com.wiatec.btv_launcher.data.IChannelData;
import com.wiatec.btv_launcher.data.IImage2Data;
import com.wiatec.btv_launcher.data.IImageData;
import com.wiatec.btv_launcher.data.IRollImageData;
import com.wiatec.btv_launcher.data.Image2Data;
import com.wiatec.btv_launcher.data.ImageData;
import com.wiatec.btv_launcher.data.RollImage2Data;
import com.wiatec.btv_launcher.data.RollImageData;
import com.wiatec.btv_launcher.fragment.IFragment4;

import java.util.List;

/**
 * Created by patrick on 2016/12/28.
 */

public class Fragment4Presenter extends BasePresenter<IFragment4> {
    private IFragment4 iFragment4;
    private IImageData iImageData;
    private IImage2Data iImage2Data;
    private IRollImageData iRollImageData;
    private IRollImageData iRollImageData2;
    private IChannelData iChannelData;


    public Fragment4Presenter(IFragment4 iFragment4) {
        this.iFragment4 = iFragment4;
        iImageData = new ImageData();
        iImage2Data = new Image2Data();
        iRollImageData = new RollImageData();
        iRollImageData2 = new RollImage2Data();
        iChannelData = new ChannelData();
    }

    public void bind (){
        if(iImageData != null){
            iImageData.loadData(new IImageData.OnLoadListener() {
                @Override
                public void onSuccess(List<ImageInfo> list) {
                    iFragment4.loadImage(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }

        if(iImage2Data != null){
            iImage2Data.loadData(new IImage2Data.OnLoadListener() {
                @Override
                public void onSuccess(List<ImageInfo> list) {
                    iFragment4.loadImage2(list);
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

                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }

    public void loadRollImage(){
        if(iRollImageData != null){
            iRollImageData.loadData(new IRollImageData.OnLoadListener() {
                @Override
                public void onSuccess(List<ImageInfo> list) {
                    iFragment4.loadRollImage(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }

        if(iRollImageData2 != null){
            iRollImageData2.loadData(new IRollImageData.OnLoadListener() {
                @Override
                public void onSuccess(List<ImageInfo> list) {
                    iFragment4.loadRollImage2(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }

    public void showChannel(String selection,String where ,String order){
        if(iChannelData != null){
            iChannelData.showChannel(selection ,where ,order, new IChannelData.OnLoadListener() {
                @Override
                public void onSuccess(List<ChannelInfo> list) {
                    iFragment4.showChannel(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }

}
