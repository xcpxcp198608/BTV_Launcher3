package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.Message1Info;

import java.io.IOException;
import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class Message1Data implements IMessage1Data{

    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        HttpMaster.post(F.url.message1)
                .param("deviceInfo.countryCode", (String) SPUtil.get("countryCode" , ""))
                .param("deviceInfo.regionName", (String)SPUtil.get("regionName" , ""))
                .param("deviceInfo.timeZone", (String)SPUtil.get("timeZone" , ""))
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
