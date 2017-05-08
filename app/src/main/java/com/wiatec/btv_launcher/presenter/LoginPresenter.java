package com.wiatec.btv_launcher.presenter;

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
import com.wiatec.btv_launcher.data.ILoginData;
import com.wiatec.btv_launcher.data.LoginData;

import java.io.IOException;

/**
 * Created by patrick on 2016/12/29.
 */

public class LoginPresenter extends BasePresenter<ILoginActivity> {

    private ILoginActivity iLoginActivity;
    private ILoginData iLoginData;

    public LoginPresenter(ILoginActivity iLoginActivity) {
        this.iLoginActivity = iLoginActivity;
        iLoginData = new LoginData();
    }

    public void login(UserInfo userInfo , DeviceInfo deviceInfo){
        try {
            if(iLoginData != null){
                iLoginData.login(userInfo, deviceInfo, new ILoginData.OnLoginListener() {
                    @Override
                    public void onSuccess(Result result) {
                        iLoginActivity.login(result);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resetPassword (UserInfo userInfo){
        OkMaster.post(F.url.reset_p)
                .parames("userInfo.userName" , userInfo.getUserName())
                .parames("userInfo.email" , userInfo.getEmail())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        Result result = new Gson().fromJson(s , new TypeToken<Result>(){}.getType());
                        iLoginActivity.resetp(result);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
