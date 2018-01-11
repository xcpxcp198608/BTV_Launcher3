package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
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
                .param("deviceInfo.countryCode", (String) SPUtil.get(F.sp.country_code , ""))
                .param("deviceInfo.regionName", (String)SPUtil.get(F.sp.region_name , ""))
                .param("deviceInfo.timeZone", (String)SPUtil.get(F.sp.time_zone , ""))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws Exception {
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
