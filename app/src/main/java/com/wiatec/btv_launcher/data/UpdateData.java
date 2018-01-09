package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.UpdateInfo;

import java.io.IOException;

/**
 * Created by PX on 2016-11-14.
 */

public class UpdateData implements IUpdateData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        HttpMaster.get(F.url.updater)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
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
