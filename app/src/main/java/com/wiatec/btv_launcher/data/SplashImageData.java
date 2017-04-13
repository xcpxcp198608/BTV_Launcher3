package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.ImageInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by PX on 2016-11-15.
 */

public class SplashImageData implements ISplashImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        OkMaster.post(F.url.splash_image)
                .parames("deviceInfo.countryCode", (String)SPUtils.get(Application.getContext() , "countryCode" , ""))
                .parames("deviceInfo.regionName", (String)SPUtils.get(Application.getContext() , "regionName" , ""))
                .parames("deviceInfo.timeZone", (String)SPUtils.get(Application.getContext() , "timeZone" , ""))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        List<ImageInfo> list = new Gson().fromJson(s , new TypeToken<List<ImageInfo>>() {} .getType());
                        if(list == null || list.size() <= 0){
                            return;
                        }
                        onLoadListener.onSuccess(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onFailure(e);
                    }
                });
    }
}
