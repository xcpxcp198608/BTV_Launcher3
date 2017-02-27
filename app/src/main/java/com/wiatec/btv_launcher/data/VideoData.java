package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.VideoInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by patrick on 2017/1/2.
 */

public class VideoData implements IVideoData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(F.url.video,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null){
                    List<VideoInfo> list = new Gson().fromJson(String.valueOf(response) , new TypeToken<List<VideoInfo>>(){}.getType());
                    if(list != null){
                        onLoadListener.onSuccess(list);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonArrayRequest.setTag("Video1");
        Application.getRequestQueue().add(jsonArrayRequest);
    }
}
