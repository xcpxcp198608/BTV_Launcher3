package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.activity.ISplashActivity;
import com.wiatec.btv_launcher.bean.ImageInfo;

import org.json.JSONObject;

/**
 * Created by PX on 2016-11-15.
 */

public class SplashImageData implements ISplashImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(F.url.splash_image, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response !=null){
                    ImageInfo imageInfo = new Gson().fromJson(String.valueOf(response) , new TypeToken<ImageInfo>(){}.getType());
                    onLoadListener.onSuccess(imageInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonObjectRequest.setTag("SplashImageInfo");
        Application.getRequestQueue().add(jsonObjectRequest);
    }
}
