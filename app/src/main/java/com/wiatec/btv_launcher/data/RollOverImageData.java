package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.ImageInfo;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by patrick on 2017/2/25.
 */

public class RollOverImageData implements IRollImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(F.url.roll_over_image, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null){
                    List<ImageInfo> list = new Gson().fromJson(String.valueOf(response) , new TypeToken<List<ImageInfo>>() {} .getType());
                    onLoadListener.onSuccess(list);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonArrayRequest.setTag("RollOverImage");
        Application.getRequestQueue().add(jsonArrayRequest);
    }
}
