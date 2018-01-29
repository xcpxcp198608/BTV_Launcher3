package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.VideoInfo;


public class AdVideoData implements IAdVideoData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        HttpMaster.post(F.url.ad_video)
                .param("deviceInfo.countryCode", (String)SPUtil.get(F.sp.country_code , ""))
                .param("deviceInfo.regionName", (String)SPUtil.get(F.sp.region_name , ""))
                .param("deviceInfo.timeZone", (String) SPUtil.get(F.sp.time_zone , ""))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws Exception {
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
