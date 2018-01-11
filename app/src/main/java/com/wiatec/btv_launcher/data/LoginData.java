package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;

import java.io.IOException;


public class LoginData implements ILoginData {
    @Override
    public void login(final AuthRegisterUserInfo authRegisterUserInfo, final OnLoginListener onLoginListener) {
//        Logger.d(user1Info.toString());
        HttpMaster.post(F.url.user_login)
                .param("username" , authRegisterUserInfo.getUsername())
                .param("password" , authRegisterUserInfo.getPassword())
                .param("mac" ,authRegisterUserInfo.getMac())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        ResultInfo<AuthRegisterUserInfo> resultInfo = new Gson().fromJson(s ,
                                new TypeToken<ResultInfo<AuthRegisterUserInfo>>(){}.getType());
                        onLoginListener.onSuccess(resultInfo);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoginListener.onFailure(e);
                    }
                });
    }
}
