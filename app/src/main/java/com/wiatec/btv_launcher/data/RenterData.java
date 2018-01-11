package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;

import java.io.IOException;

public class RenterData implements IRenterData {


    @Override
    public void login(AuthRentUserInfo authRentUserInfo, final OnLoginListener onLoginListener) {
        HttpMaster.post(F.url.renter_login + authRentUserInfo.getClientKey() + "/" + authRentUserInfo.getMac())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws Exception {
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
