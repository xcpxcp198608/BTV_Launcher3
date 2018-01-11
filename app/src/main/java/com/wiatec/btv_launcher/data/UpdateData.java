package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.UpdateInfo;

import java.io.IOException;


public class UpdateData implements IUpdateData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        HttpMaster.get(F.url.updater)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws Exception {
                        if(s!= null){
                            UpdateInfo updateInfo = new Gson().fromJson(s , new TypeToken<UpdateInfo>(){}.getType());
                            onLoadListener.onSuccess(updateInfo);
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onFailure(e);
                    }
                });
    }
}
