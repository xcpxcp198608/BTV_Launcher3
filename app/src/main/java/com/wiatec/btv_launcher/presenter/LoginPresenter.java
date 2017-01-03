package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Activity.BaseActivity;
import com.wiatec.btv_launcher.Activity.ILoginActivity;
import com.wiatec.btv_launcher.Utils.Logger;
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

    public void login(String key ,String password){
        if(iLoginData != null){
            iLoginData.login(key, password, new ILoginData.OnLoginListener() {
                @Override
                public void onSuccess(String result) {
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
