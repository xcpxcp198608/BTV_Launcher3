package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ChannelTypeInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.data.ChannelData;
import com.wiatec.btv_launcher.data.ChannelTypeData;
import com.wiatec.btv_launcher.data.IChannelData;
import com.wiatec.btv_launcher.data.IChannelTypeData;
import com.wiatec.btv_launcher.data.IImageData;
import com.wiatec.btv_launcher.data.IRollImageData;
import com.wiatec.btv_launcher.data.ImageData;
import com.wiatec.btv_launcher.data.RollImageData;
import com.wiatec.btv_launcher.data.RollOverImageData;
import com.wiatec.btv_launcher.fragment.IFragment2;

import java.util.List;

/**
 * Created by patrick on 2016/12/28.
 */

public class Fragment2Presenter extends BasePresenter<IFragment2> {
    private IFragment2 iFragment2;
    private IImageData iImageData;
    private IRollImageData iRollImageData;
    private IRollImageData rollOverImageData;
    private IChannelData iChannelData;
    private IChannelTypeData iChannelTyepData;

    public Fragment2Presenter(IFragment2 iFragment2) {
        this.iFragment2 = iFragment2;
        iImageData = new ImageData();
            iRollImageData = new RollImageData();
        rollOverImageData = new RollOverImageData();
        iChannelData = new ChannelData();
        iChannelTyepData = new ChannelTypeData();
    }

    public void bind (){
        try {
            if(iImageData != null){
                iImageData.loadData(new IImageData.OnLoadListener() {
                    @Override
                    public void onSuccess(List<ImageInfo> list) {
                        iFragment2.loadImage(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadChannelType (){
        try {
            if(iChannelTyepData != null){
                iChannelTyepData.loadData(new IChannelTypeData.OnLoadListener() {
                    @Override
                    public void onSuccess(List<ChannelTypeInfo> list) {
                        iFragment2.loadChannelType(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadRollImage(){
        try {
            if(iRollImageData != null){
                iRollImageData.loadData(new IRollImageData.OnLoadListener() {
                    @Override
                    public void onSuccess(List<ImageInfo> list) {
                        iFragment2.loadRollImage(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
            }

            if(rollOverImageData != null){
                rollOverImageData.loadData(new IRollImageData.OnLoadListener() {
                    @Override
                    public void onSuccess(List<ImageInfo> list) {
                        iFragment2.loadRollOverImage(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadChannel(){
        try {
            if(iChannelData != null){
                iChannelData.loadData(new IChannelData.OnLoadListener() {
                    @Override
                    public void onSuccess(List<ChannelInfo> list) {

                    }

                    @Override
                    public void onFailure(String e) {

                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showChannel(String selection,String where ,String order){
        try {
            if(iChannelData != null){
                iChannelData.showChannel(selection ,where ,order, new IChannelData.OnLoadListener() {
                    @Override
                    public void onSuccess(List<ChannelInfo> list) {
                        iFragment2.showChannel(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
