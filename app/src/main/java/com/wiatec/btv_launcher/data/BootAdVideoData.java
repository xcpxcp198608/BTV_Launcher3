package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.VideoInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class BootAdVideoData implements IBootAdVideoData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        OkMaster.post(F.url.boot_ad_video)
                .parames("deviceInfo.countryCode", (String)SPUtils.get(Application.getContext() , "countryCode" , ""))
                .parames("deviceInfo.regionName", (String)SPUtils.get(Application.getContext() , "regionName" , ""))
                .parames("deviceInfo.timeZone", (String)SPUtils.get(Application.getContext() , "timeZone" , ""))
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
