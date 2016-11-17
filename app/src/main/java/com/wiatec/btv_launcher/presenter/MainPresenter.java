package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Activity.IMainActivity;
import com.wiatec.btv_launcher.bean.Message1Info;
import com.wiatec.btv_launcher.bean.UpdateInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.data.IMessage1Data;
import com.wiatec.btv_launcher.data.IUpdateData;
import com.wiatec.btv_launcher.data.IVideoData;
import com.wiatec.btv_launcher.data.Message1Data;
import com.wiatec.btv_launcher.data.UpdateData;
import com.wiatec.btv_launcher.data.VideoData;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class MainPresenter extends BasePresenter<IMainActivity> {

    private IMainActivity iMainActivity;
    private IMessage1Data iMessage1Data;
    private IUpdateData iUpdateData;
    private IVideoData iVideoData;

    public MainPresenter(IMainActivity iMainActivity) {
        this.iMainActivity = iMainActivity;
        iMessage1Data = new Message1Data();
        iUpdateData = new UpdateData();
        iVideoData = new VideoData();
    }

    public void loadMessage (){
        if(iUpdateData != null){
            iUpdateData.loadData(new IUpdateData.OnLoadListener() {
                @Override
                public void onSuccess(UpdateInfo updateInfo) {
                    iMainActivity.loadUpdate(updateInfo);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }

        if(iVideoData != null){
            iVideoData.loadData(new IVideoData.OnLoadListener() {
                @Override
                public void onSuccess(VideoInfo videoInfo) {
                    iMainActivity.loadVideo(videoInfo);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }

        if(iMessage1Data != null){
            iMessage1Data.loadData(new IMessage1Data.OnLoadListener() {
                @Override
                public void onSuccess(List<Message1Info> list) {
                    iMainActivity.loadMessage1(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
