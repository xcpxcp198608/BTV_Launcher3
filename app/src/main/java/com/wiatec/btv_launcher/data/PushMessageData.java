package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.PushMessageInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by xuchengpeng on 27/04/2017.
 */

public class PushMessageData implements IPushMessageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        OkMaster.get(F.url.push_message)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s ==null){
                            return;
                        }
                        List<PushMessageInfo> list = new Gson().fromJson(s , new TypeToken<List<PushMessageInfo>>(){}.getType());
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
