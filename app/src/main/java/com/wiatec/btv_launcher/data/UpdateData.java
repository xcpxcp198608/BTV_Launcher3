package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.UpdateInfo;

import org.json.JSONObject;

/**
 * Created by PX on 2016-11-14.
 */

public class UpdateData implements IUpdateData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(F.url.updater, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!= null){
                    UpdateInfo updateInfo = new Gson().fromJson(String.valueOf(response) , new TypeToken<UpdateInfo>(){}.getType());
                    onLoadListener.onSuccess(updateInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonObjectRequest.setTag("UpdateInfo");
        Application.getRequestQueue().add(jsonObjectRequest);
    }
}
