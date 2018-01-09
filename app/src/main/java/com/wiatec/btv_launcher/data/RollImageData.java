package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.ImageInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class RollImageData implements IRollImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        HttpMaster.post(F.url.roll_image)
                .param("deviceInfo.countryCode", (String)SPUtil.get("countryCode" , ""))
                .param("deviceInfo.regionName", (String)SPUtil.get("regionName" , ""))
                .param("deviceInfo.timeZone", (String) SPUtil.get("timeZone" , ""))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        List<ImageInfo> list = new Gson().fromJson(s , new TypeToken<List<ImageInfo>>() {} .getType());
                        if(list == null || list.size() <= 0){
                            return;
                        }
                        onLoadListener.onSuccess(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onFailure(e);
                        Logger.d(e);
                    }
                });
    }
}
