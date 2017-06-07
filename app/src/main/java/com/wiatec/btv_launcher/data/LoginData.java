package com.wiatec.btv_launcher.data;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Activity.ILoginActivity;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.DeviceInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.UserInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by patrick on 2016/12/29.
 */

public class LoginData implements ILoginData {
    @Override
    public void login(final UserInfo userInfo, final DeviceInfo deviceInfo, final OnLoginListener onLoginListener) {
        OkMaster.post(F.url.login)
                .parames("userInfo.userName" , userInfo.getUserName())
                .parames("userInfo.password" , userInfo.getPassword())
                .parames("deviceInfo.mac" ,deviceInfo.getMac())
                .parames("deviceInfo.ethernetMac",deviceInfo.getEthernetMac())
                .parames("deviceInfo.country" ,deviceInfo.getCountry())
                .parames("deviceInfo.countryCode" ,deviceInfo.getCountryCode())
                .parames("deviceInfo.regionName" ,deviceInfo.getRegionName())
                .parames("deviceInfo.timeZone" ,deviceInfo.getTimeZone())
                .parames("deviceInfo.city" ,deviceInfo.getCity())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        Result result = new Gson().fromJson(s , new TypeToken<Result>(){}.getType());
                        onLoginListener.onSuccess(result);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoginListener.onFailure(e);
                    }
                });
    }
}
