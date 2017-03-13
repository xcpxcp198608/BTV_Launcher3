package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.ChannelTypeInfo;
import com.wiatec.btv_launcher.bean.DeviceInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by patrick on 2017/3/13.
 */

public class ChannelTypeData implements IChannelTypeData {
    @Override
    public void loadData(final OnLoadListener onLoadListener , DeviceInfo deviceInfo) {
        OkMaster.get(F.url.channel_type)
                .parames("deviceInfo",deviceInfo)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        List<ChannelTypeInfo> list = new Gson().fromJson(s , new TypeToken<List<ChannelTypeInfo>>(){}.getType());
                        if(list ==null || list.size() <=0){
                            return;
                        }
                        onLoadListener.onSuccess(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onFailure(e);
                    }
                });
    }
}
