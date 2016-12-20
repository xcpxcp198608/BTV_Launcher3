package com.wiatec.btv_launcher.data;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.Message1Info;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class Message1Data implements IMessage1Data{

    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        String url = "";
        String language = Application.getContext().getSharedPreferences("language" , Context.MODE_PRIVATE).getString("language" ,"en");
        if("en_US".equals(language)){
            url = F.url.message1;
        }else if("zh_CN".equals(language)){
            url = F.url.message1_zh_CN;
        }else if("zh_TW".equals(language)){
            url = F.url.message1_zh_TW;
        }else if("es_ES".equals(language)){
            url = F.url.message1_es_ES;
        }else if("es_US".equals(language)){
            url = F.url.message1_es_US;
        }else if("it_IT".equals(language)){
            url = F.url.message1_it_IT;
        }else {
            url = F.url.message1;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null){
                    List<Message1Info> list = new Gson().fromJson(String.valueOf(response) , new TypeToken<List<Message1Info>>(){}.getType());
                    onLoadListener.onSuccess(list);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonArrayRequest.setTag("Message1Info");
        Application.getRequestQueue().add(jsonArrayRequest);

    }
}
