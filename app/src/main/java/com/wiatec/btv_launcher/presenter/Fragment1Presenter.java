package com.wiatec.btv_launcher.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Activity.PlayAdActivity;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.PushMessageInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.UserDataInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.data.CloudImageData;
import com.wiatec.btv_launcher.data.ICloudImageData;
import com.wiatec.btv_launcher.data.IPushMessageData;
import com.wiatec.btv_launcher.data.IRollImageData;
import com.wiatec.btv_launcher.data.IVideoData;
import com.wiatec.btv_launcher.data.PushMessageData;
import com.wiatec.btv_launcher.data.RollImageData;
import com.wiatec.btv_launcher.data.RollOverImageData;
import com.wiatec.btv_launcher.data.UploadTimeData;
import com.wiatec.btv_launcher.data.VideoData;
import com.wiatec.btv_launcher.fragment.IFragment1;

import java.io.IOException;
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
    private UploadTimeData uploadTimeData;

    public Fragment1Presenter(IFragment1 iFragment1) {
        this.iFragment1 = iFragment1;
        iRollImageData = new RollImageData();
        iRollOverImage = new RollOverImageData();
        iCloudImageData = new CloudImageData();
        iVideoData = new VideoData();
        uploadTimeData = new UploadTimeData();
        iPushMessageData = new PushMessageData();
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

    public void loadPushMessage(){
        if(iPushMessageData != null){
            iPushMessageData.loadData(new IPushMessageData.OnLoadListener() {
                @Override
                public void onSuccess(List<PushMessageInfo> list) {
                    iFragment1.loadPushMessage(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
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
                        Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void uploadHoldTime(UserDataInfo userDataInfo){
        try {
            if(uploadTimeData!= null) {
                uploadTimeData.upload(userDataInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void check(String userName , final String packageName , final Context context){
        OkMaster.post(F.url.level_check)
                .parames("userInfo.userName" , userName)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        Result result = new Gson().fromJson(s , new TypeToken<Result>(){}.getType());
                        if(result.getCode() == Result.CODE_OK){
                            if(result.getCount() > 1){
                                if (ApkCheck.isApkInstalled(context,packageName)) {
                                    ApkLaunch.launchApkByPackageName(context, packageName);
                                }else{
                                    Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.download_guide),
                                            Toast.LENGTH_LONG).show();
                                    ApkLaunch.launchApkByPackageName(context, F.package_name.market);
                                }
                            }else if(result.getCount() == 1){
                                if(packageName.equals(F.package_name.btv)) {
                                    Intent intent = new Intent(context, PlayAdActivity.class);
                                    intent.putExtra("packageName", F.package_name.btv);
                                    context.startActivity(intent);
                                }else{
                                    if (ApkCheck.isApkInstalled(Application.getContext(),packageName)) {
                                        ApkLaunch.launchApkByPackageName(context, packageName);
                                    }else{
                                        Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.download_guide),
                                                Toast.LENGTH_LONG).show();
                                        ApkLaunch.launchApkByPackageName(context, F.package_name.market);
                                    }
                                }
                            }else{
                                Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.account_error) ,
                                        Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.account_error) ,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
