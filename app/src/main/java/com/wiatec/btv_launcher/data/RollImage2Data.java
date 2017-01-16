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
 * Created by patrick on 2017/1/14.
 */

public class RollImage2Data implements IRollImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(F.url.roll_image2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response == null){
                    return;
                }
                List<ImageInfo> list = new Gson().fromJson(String.valueOf(response) , new TypeToken<List<ImageInfo>>(){}.getType());
                if(list != null){
                    onLoadListener.onSuccess(list);
                }else{
                    onLoadListener.onFailure("roll image2 json parse error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonArrayRequest.setTag("rollimage2");
        Application.getRequestQueue().add(jsonArrayRequest);

    }
}
