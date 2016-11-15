package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.VideoInfo;

import org.json.JSONObject;

/**
 * Created by PX on 2016-11-14.
 */

public class VideoData implements IVideoData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(F.url.video, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    VideoInfo videoInfo = new Gson().fromJson(String.valueOf(response) , new TypeToken<VideoInfo>(){}.getType());
                    onLoadListener.onSuccess(videoInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonObjectRequest.setTag("VideoInfo");
        Application.getRequestQueue().add(jsonObjectRequest);
    }
}
