package com.wiatec.btv_launcher.service_task;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.FileCheck;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by patrick on 2017/3/2.
 */

public class LoadKodiData implements Runnable {

    @Override
    public void run() {
        loadImageData();
        loadVideoData();
    }

    private void loadImageData() {
        OkMaster.get(F.url.kodi_image_data).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response ==null){
                    return;
                }
                String jsonString = response.body().string();
                List<ImageInfo> list = new Gson().fromJson(jsonString ,new TypeToken<List<ImageInfo>>(){}.getType());
                if(list ==null || list.size() <=0){
                    return;
                }
                for(ImageInfo imageInfo : list){
                    OkMaster.download(Application.getContext())
                            .name(imageInfo.getName())
                            .path(F.path.kodi_image_path)
                            .url(imageInfo.getUrl())
                            .startDownload(null);
                }
            }
        });
    }

    private void loadVideoData() {
        OkMaster.get(F.url.kodi_video_data).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response ==null){
                    return;
                }
                String jsonString = response.body().string();
                VideoInfo videoInfo = new Gson().fromJson(jsonString ,new TypeToken<VideoInfo>(){}.getType());
                if(videoInfo ==null ){
                    return;
                }
                if(!FileCheck.isFileExists(F.path.kodi_video_path , videoInfo.getName())){
                    OkMaster.download(Application.getContext())
                            .name(videoInfo.getName())
                            .path(F.path.kodi_video_path)
                            .url(videoInfo.getUrl())
                            .startDownload(null);
                }else if (!FileCheck.isFileIntact(F.path.kodi_video_path ,videoInfo.getName() ,videoInfo.getMd5())){
                    OkMaster.download(Application.getContext())
                            .name(videoInfo.getName())
                            .path(F.path.kodi_video_path)
                            .url(videoInfo.getUrl())
                            .startDownload(null);
                }else{
                    Logger.d("kodi video do not need download");
                }
            }
        });
    }
}
