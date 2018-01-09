package com.wiatec.btv_launcher.presenter;

import android.content.Context;
import android.widget.Toast;

import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.UserLogInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.data.CloudImageData;
import com.wiatec.btv_launcher.data.ICloudImageData;
import com.wiatec.btv_launcher.data.IPushMessageData;
import com.wiatec.btv_launcher.data.IRollImageData;
import com.wiatec.btv_launcher.data.IVideoData;
import com.wiatec.btv_launcher.data.RollImageData;
import com.wiatec.btv_launcher.data.RollOverImageData;
import com.wiatec.btv_launcher.data.UserLogData;
import com.wiatec.btv_launcher.data.VideoData;
import com.wiatec.btv_launcher.fragment.IFragment1;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class Fragment1Presenter extends BasePresenter<IFragment1> {
    private IFragment1 iFragment1;
    private IRollImageData iRollImageData;
    private IRollImageData iRollOverImage;
    private ICloudImageData iCloudImageData;
    private IVideoData iVideoData;
    private IPushMessageData iPushMessageData;
    private UserLogData uploadTimeData;

    public Fragment1Presenter(IFragment1 iFragment1) {
        this.iFragment1 = iFragment1;
        iRollImageData = new RollImageData();
        iRollOverImage = new RollOverImageData();
        iCloudImageData = new CloudImageData();
        iVideoData = new VideoData();
        uploadTimeData = new UserLogData();
    }

    public void loadRollImageData(){
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadCloudData(){
        try {
            if(iCloudImageData != null){
                iCloudImageData.loadData(new ICloudImageData.OnLoadListener() {
                    @Override
                    public void onSuccess(List<String> list) {
                        iFragment1.loadCloudImage(list);
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

    public void loadRollOverImage(){
        try {
            if(iRollOverImage != null){
                iRollOverImage.loadData(new IRollImageData.OnLoadListener() {
                    @Override
                    public void onSuccess(List<ImageInfo> list) {
                        iFragment1.loadRollOverImage(list);
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

    public void loadVideo (){
        try {
            if(iVideoData != null){
                iVideoData.loadData(new IVideoData.OnLoadListener() {
                    @Override
                    public void onSuccess(List<VideoInfo> list) {
                        iFragment1.loadVideo(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        iFragment1.loadVideo(null);
                        Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void uploadHoldTime(UserLogInfo userDataInfo){
        try {
            if(uploadTimeData!= null) {
                uploadTimeData.upload(userDataInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void check(final String packageName , final Context context){
        if(!AppUtil.isInstalled(packageName)){
            if(F.package_name.bplay.equals(packageName)){
                iFragment1.showLivePlayDownloadDialog();
            }else{
                Toast.makeText(CommonApplication.context, CommonApplication.context.getString(R.string.download_guide),
                        Toast.LENGTH_LONG).show();
                AppUtil.launchApp(context, F.package_name.market);
            }
        }else{
            String l = (String) SPUtil.get("userLevel" , "1");
            int level = Integer.parseInt(l);
            if(level >= 1 ){
                AppUtil.launchApp(context, packageName);
            }else{
                Toast.makeText(CommonApplication.context , CommonApplication.context.getString(R.string.account_error) ,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
