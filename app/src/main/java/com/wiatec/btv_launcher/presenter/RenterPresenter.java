package com.wiatec.btv_launcher.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Activity.ILoginActivity;
import com.wiatec.btv_launcher.Activity.IRenterActivity;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.bean.User1Info;
import com.wiatec.btv_launcher.data.ILoginData;
import com.wiatec.btv_launcher.data.IRenterData;
import com.wiatec.btv_launcher.data.LoginData;
import com.wiatec.btv_launcher.data.RenterData;

import java.io.IOException;

/**
 * Created by patrick on 2016/12/29.
 */

public class RenterPresenter extends BasePresenter<IRenterActivity> {

    private IRenterActivity iRenterActivity;
    private IRenterData iRenterData;

    public RenterPresenter(IRenterActivity iRenterActivity) {
        this.iRenterActivity = iRenterActivity;
        iRenterData = new RenterData();
    }

    public void login(AuthRentUserInfo authRentUserInfo){
        try {
            if(iRenterData != null){
                iRenterData.login(authRentUserInfo, new IRenterData.OnLoginListener() {
                    @Override
                    public void onSuccess(ResultInfo<AuthRentUserInfo> resultInfo) {
                        iRenterActivity.onLogin(resultInfo);
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
}
