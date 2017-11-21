package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.bean.User1Info;

import java.io.IOException;

/**
 * Created by patrick on 2016/12/29.
 */

public class RenterData implements IRenterData {


    @Override
    public void login(AuthRentUserInfo authRentUserInfo, final OnLoginListener onLoginListener) {
        OkMaster.post(F.url.renter_login + authRentUserInfo.getClientKey() + "/" + authRentUserInfo.getMac())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            onLoginListener.onSuccess(null);
                            return;
                        }
                        ResultInfo<AuthRentUserInfo> resultInfo = new Gson().fromJson(s , new TypeToken<ResultInfo<AuthRentUserInfo>>(){}.getType());
                        onLoginListener.onSuccess(resultInfo);
                    }

                    @Override
                    public void onFailure(String e) {

                        onLoginListener.onFailure(e);
                    }
                });
    }

}
