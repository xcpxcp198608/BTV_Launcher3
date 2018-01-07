package com.wiatec.btv_launcher.service_task;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.wiatec.btv_launcher.F;
import com.px.common.utils.FileUtil;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;

import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoadKodiData implements Runnable {

    @Override
    public void run() {
        if(!NetUtil.isConnected()){
            return;
        }
        try {
            loadImageData();
            loadVideoData();
        }catch (Exception e){
            Logger.d(e.getMessage());
        }
    }

    private void loadImageData() {
        HttpMaster.get(F.url.kodi_image).enqueue(new Callback() {
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
                List<ImageInfo> list = null;
                try {
                    list = new Gson().fromJson(jsonString ,new TypeToken<List<ImageInfo>>(){}.getType());
                }catch (Exception e){
                   Logger.d(e.getMessage());
                }

                if(list ==null || list.size() <=0){
                    return;
                }
                for(ImageInfo imageInfo : list){
                    HttpMaster.download(CommonApplication.context)
                            .name(imageInfo.getName())
                            .path(F.path.kodi_image_path)
                            .url(imageInfo.getUrl())
                            .startDownload(null);
                }
            }
        });
    }

    private void loadVideoData() {
        HttpMaster.get(F.url.kodi_video).enqueue(new Callback() {
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
                VideoInfo videoInfo = null;
                try {
                    videoInfo = new Gson().fromJson(jsonString ,new TypeToken<VideoInfo>(){}.getType());
                }catch (Exception e){
                    Logger.d(e.getMessage());
                }
                if(videoInfo ==null ){
                    return;
                }
                if(!FileUtil.isExists(F.path.kodi_video_path , videoInfo.getName())){
                    HttpMaster.download(CommonApplication.context)
                            .name(videoInfo.getName())
                            .path(F.path.kodi_video_path)
                            .url(videoInfo.getUrl())
                            .startDownload(null);
                }else if (!FileUtil.isIntact(F.path.kodi_video_path ,videoInfo.getName() ,videoInfo.getMd5())){
                    HttpMaster.download(CommonApplication.context)
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
