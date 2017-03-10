package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Activity.BaseActivity;
import com.wiatec.btv_launcher.Activity.ILoginActivity;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.DeviceInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.UserInfo;
import com.wiatec.btv_launcher.data.ILoginData;
import com.wiatec.btv_launcher.data.LoginData;

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
    }
}
