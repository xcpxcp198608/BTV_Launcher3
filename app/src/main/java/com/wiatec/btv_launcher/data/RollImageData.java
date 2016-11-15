package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.RollImageInfo;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class RollImageData implements IRollImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(F.url.roll_image, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null){
                    List<RollImageInfo> list = new Gson().fromJson(String.valueOf(response) , new TypeToken<List<RollImageInfo>>() {} .getType());
                    onLoadListener.onSuccess(list);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonArrayRequest.setTag("RollImageInfo");
        Application.getRequestQueue().add(jsonArrayRequest);
    }
}
