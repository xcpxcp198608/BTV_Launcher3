package com.wiatec.btv_launcher.data;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
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

    public void upload(UserDataInfo userDataInfo){
        OkMaster.get(F.url.upload_data)
                .parames("userData.userName",userDataInfo.getUserName())
                .parames("userData.country",userDataInfo.getCountry())
                .parames("userData.city",userDataInfo.getCity())
                .parames("userData.mac",userDataInfo.getMac())
                .parames("userData.exitTime",userDataInfo.getExitTime())
                .parames("userData.stayTime",userDataInfo.getStayTime())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {

                    }

                    @Override
                    public void onFailure(String e) {

                    }
                });
    }
}
