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

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * Created by PX on 2016-11-25.
 */

public class AdVideoData implements IAdVideoData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        OkMaster.get(F.url.ad_video)
                .parames("deviceInfo.countryCode", SPUtils.get(Application.getContext() , "countryCode" , ""))
                .parames("deviceInfo.timeZone", SPUtils.get(Application.getContext() , "timeZone" , ""))
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
