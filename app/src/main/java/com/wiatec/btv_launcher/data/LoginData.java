package com.wiatec.btv_launcher.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.bean.User1Info;

import java.io.IOException;

/**
 * Created by patrick on 2016/12/29.
 */

public class LoginData implements ILoginData {
    @Override
    public void login(final AuthRegisterUserInfo authRegisterUserInfo, final OnLoginListener onLoginListener) {
//        Logger.d(user1Info.toString());
        OkMaster.post(F.url.user_login)
                .parames("username" , authRegisterUserInfo.getUsername())
                .parames("password" , authRegisterUserInfo.getPassword())
                .parames("mac" ,authRegisterUserInfo.getMac())
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
