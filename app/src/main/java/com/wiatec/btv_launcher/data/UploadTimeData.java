package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.UserDataInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by patrick on 2017/2/27.
 */

public class UploadTimeData {

    public void upload(final UserDataInfo userDataInfo){
        OkMaster.post(F.url.upload_data)
                .parames("userDataInfo.userName",userDataInfo.getUserName())
                .parames("userDataInfo.country",userDataInfo.getCountry())
                .parames("userDataInfo.city",userDataInfo.getCity())
                .parames("userDataInfo.mac",userDataInfo.getMac())
                .parames("userDataInfo.exitTime",userDataInfo.getExitTime())
                .parames("userDataInfo.stayTime",userDataInfo.getStayTime())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {

                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
