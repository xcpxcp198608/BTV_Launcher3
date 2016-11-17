package com.wiatec.btv_launcher.service_task;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.MessageDao;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.MessageInfo;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by PX on 2016-11-15.
 */

public class LoadMessage implements Runnable {

    @Override
    public void run() {
        loadMessage();
    }

    private void loadMessage (){
        final MessageDao messageDao = MessageDao.getInstance(Application.getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(F.url.message, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null) {
                    List<MessageInfo> list = new Gson().fromJson(String.valueOf(response), new TypeToken<List<MessageInfo>>() {
                    }.getType());
                    for (MessageInfo messageInfo:list){
                       // Logger.d(messageInfo.toString());
                        messageDao.insertMessage(messageInfo);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(error.getMessage());
            }
        });
        jsonArrayRequest.setTag("MessageInfo");
        Application.getRequestQueue().add(jsonArrayRequest);
    }
}
