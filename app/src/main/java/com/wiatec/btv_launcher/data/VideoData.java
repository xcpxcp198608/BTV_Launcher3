package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.VideoInfo;

import java.io.IOException;
import java.util.List;

public class VideoData implements IVideoData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        HttpMaster.post(F.url.video)
                .param("deviceInfo.countryCode", (String)SPUtil.get( F.sp.country_code , ""))
                .param("deviceInfo.regionName", (String)SPUtil.get(F.sp.region_name , ""))
                .param("deviceInfo.timeZone", (String) SPUtil.get(F.sp.time_zone , ""))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s != null){
                            List<VideoInfo> list = new Gson().fromJson(s , new TypeToken<List<VideoInfo>>(){}.getType());
                            if(list != null){
                                onLoadListener.onSuccess(list);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onFailure(e);
                    }
                });
    }
}
