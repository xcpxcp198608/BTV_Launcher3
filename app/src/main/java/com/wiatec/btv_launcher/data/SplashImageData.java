package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.ImageInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by PX on 2016-11-15.
 */

public class SplashImageData implements ISplashImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(F.url.splash_image, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response !=null){
                    List<ImageInfo> list = new Gson().fromJson(String.valueOf(response) , new TypeToken<List<ImageInfo>>(){}.getType());
                    onLoadListener.onSuccess(list);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonArrayRequest.setTag("SplashImageInfo");
        Application.getRequestQueue().add(jsonArrayRequest);
    }
}
