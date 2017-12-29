package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.Message1Info;
import com.wiatec.btv_launcher.bean.VideoInfo;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class Message1Data implements IMessage1Data{

    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        OkMaster.post(F.url.message1)
                .parames("deviceInfo.countryCode", (String)SPUtils.get(Application.getContext() , "countryCode" , ""))
                .parames("deviceInfo.regionName", (String)SPUtils.get(Application.getContext() , "regionName" , ""))
                .parames("deviceInfo.timeZone", (String)SPUtils.get(Application.getContext() , "timeZone" , ""))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s != null){
                            List<Message1Info> list = new Gson().fromJson(s , new TypeToken<List<Message1Info>>(){}.getType());
                            onLoadListener.onSuccess(list);
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onFailure(e);

                    }
                });
    }
}
