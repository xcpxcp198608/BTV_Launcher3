package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.VideoInfo;

import java.io.IOException;

/**
 * Created by PX on 2016-11-14.
 */

public class BootAdVideoData implements IBootAdVideoData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        HttpMaster.post(F.url.boot_ad_video)
                .param("deviceInfo.countryCode", (String)SPUtil.get(F.sp.country_code , ""))
                .param("deviceInfo.regionName", (String) SPUtil.get(F.sp.region_name , ""))
                .param("deviceInfo.timeZone", (String)SPUtil.get(F.sp.time_zone , ""))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s != null){
                            VideoInfo videoInfo = new Gson().fromJson(s , new TypeToken<VideoInfo>(){}.getType());
                            onLoadListener.onSuccess(videoInfo);
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onFailure(e);
                    }
                });
    }
}
