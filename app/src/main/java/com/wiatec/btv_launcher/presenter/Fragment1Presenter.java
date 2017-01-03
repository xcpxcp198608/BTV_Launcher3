package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.CloudImageInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.RollImageInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.data.CloudImageData;
import com.wiatec.btv_launcher.data.ICloudImageData;
import com.wiatec.btv_launcher.data.IImageData;
import com.wiatec.btv_launcher.data.IRollImageData;
import com.wiatec.btv_launcher.data.IVideoData;
import com.wiatec.btv_launcher.data.ImageData;
import com.wiatec.btv_launcher.data.RollImageData;
import com.wiatec.btv_launcher.data.Video1Data;
import com.wiatec.btv_launcher.data.VideoData;
import com.wiatec.btv_launcher.fragment.IFragment1;

import java.io.File;
import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class Fragment1Presenter extends BasePresenter<IFragment1> {
    private IFragment1 iFragment1;
    private IImageData iImageData;
    private IRollImageData iRollImageData;
    private ICloudImageData iCloudImageData;
    private IVideoData iVideoData;

    public Fragment1Presenter(IFragment1 iFragment1) {
        this.iFragment1 = iFragment1;
        iImageData = new ImageData();
        iRollImageData = new RollImageData();
        iCloudImageData = new CloudImageData();
        iVideoData = new Video1Data();
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
                public void onSuccess(List<ImageInfo> list) {
                    iFragment1.loadRollImage(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }

    public void loadCloudData(){
        if(iCloudImageData != null){
            iCloudImageData.loadData(new ICloudImageData.OnLoadListener() {
                @Override
                public void onSuccess(File[] files) {
                    iFragment1.loadCloudImage(files);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }

    public void loadVideo (){
        if(iVideoData != null){
            iVideoData.loadData(new IVideoData.OnLoadListener() {
                @Override
                public void onSuccess(List<VideoInfo> list) {
                    iFragment1.loadVideo(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
