package com.wiatec.btv_launcher.data;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.ChannelDao;
import com.wiatec.btv_launcher.bean.ChannelInfo;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-14.
 */

public class ChannelData implements IChannelData {

    private ChannelDao channelDao;
    public ChannelData() {
        channelDao = ChannelDao.getInstance();
    }

    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(F.url.channel, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null) {
                    channelDao.delete();
                    List<ChannelInfo> list = new Gson().fromJson(String.valueOf(response), new TypeToken<List<ChannelInfo>>(){}.getType());
                    Observable.from(list)
                            .subscribeOn(Schedulers.io())
                            .map(new Func1<ChannelInfo, Object>() {
                                @Override
                                public Object call(ChannelInfo channelInfo) {
                                    channelDao.insertOrUpdate(channelInfo);
                                    return null;
                                }
                            })
                            .subscribe(new Action1<Object>() {
                                @Override
                                public void call(Object o) {

                                }
                            });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoadListener.onFailure(error.getMessage());
            }
        });
        jsonArrayRequest.setTag("ChannelInfo");
        Application.getRequestQueue().add(jsonArrayRequest);
    }

    @Override
    public void showData(String country ,OnLoadListener onLoadListener) {
        if(!TextUtils.isEmpty(country)){
            List<ChannelInfo> list = channelDao.queryByCountry(country);
            onLoadListener.onSuccess(list);
        }else{
            onLoadListener.onFailure("country error");
        }
    }
}
