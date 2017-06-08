package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.User1Info;

import java.io.IOException;

/**
 * Created by patrick on 2016/12/29.
 */

public class LoginData implements ILoginData {
    @Override
    public void login(final User1Info user1Info, final OnLoginListener onLoginListener) {
        OkMaster.post(F.url.login)
                .parames("user1Info.userName" , user1Info.getUserName())
                .parames("user1Info.password" , user1Info.getPassword())
                .parames("user1Info.mac" ,user1Info.getMac())
                .parames("user1Info.ethernetMac",user1Info.getEthernetMac())
                .parames("user1Info.country" ,user1Info.getCountry())
                .parames("user1Info.region" ,user1Info.getRegion())
                .parames("user1Info.timeZone" ,user1Info.getTimeZone())
                .parames("user1Info.city" ,user1Info.getCity())
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
