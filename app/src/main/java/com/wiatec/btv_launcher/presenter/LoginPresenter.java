package com.wiatec.btv_launcher.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.Logger;
import com.wiatec.btv_launcher.Activity.ILoginActivity;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;
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

    public void login(AuthRegisterUserInfo authRegisterUserInfo){
        try {
            if(iLoginData != null){
                iLoginData.login(authRegisterUserInfo, new ILoginData.OnLoginListener() {
                    @Override
                    public void onSuccess(ResultInfo<AuthRegisterUserInfo> resultInfo) {
                        iLoginActivity.login(resultInfo);
                    }

                    @Override
                    public void onFailure(String e) {
                        ResultInfo<AuthRegisterUserInfo> resultInfo = new ResultInfo<>();
                        resultInfo.setCode(501);
                        resultInfo.setMessage("network communication error");
                        iLoginActivity.login(resultInfo);
                        Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resetPassword (AuthRegisterUserInfo authRegisterUserInfo){
        HttpMaster.post(F.url.user_reset_p)
                .param("username" , authRegisterUserInfo.getUsername())
                .param("email" , authRegisterUserInfo.getEmail())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        ResultInfo<AuthRegisterUserInfo> resultInfo = new Gson().fromJson(s , new TypeToken<ResultInfo<AuthRegisterUserInfo>>(){}.getType());
                        iLoginActivity.resetp(resultInfo);
                    }

                    @Override
                    public void onFailure(String e) {
                        ResultInfo<AuthRegisterUserInfo> resultInfo = new ResultInfo<>();
                        resultInfo.setCode(501);
                        resultInfo.setMessage("network communication error");
                        iLoginActivity.login(resultInfo);
                        Logger.d(e);
                    }
                });
    }
}
