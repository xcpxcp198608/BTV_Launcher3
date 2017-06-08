package com.wiatec.btv_launcher.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Activity.ILoginActivity;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.User1Info;
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

    public void login(User1Info user1Info){
        try {
            if(iLoginData != null){
                iLoginData.login(user1Info, new ILoginData.OnLoginListener() {
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

    public void resetPassword (User1Info user1Info){
        OkMaster.post(F.url.reset_p)
                .parames("user1Info.userName" , user1Info.getUserName())
                .parames("user1Info.email" , user1Info.getEmail())
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
                        Result result = new Result();
                        result.setCode(Result.CODE_REQUEST_FAILURE);
                        result.setStatus(Result.STATUS_REQUEST_FAILURE);
                        iLoginActivity.resetp(result);
                        Logger.d(e);
                    }
                });
    }
}
