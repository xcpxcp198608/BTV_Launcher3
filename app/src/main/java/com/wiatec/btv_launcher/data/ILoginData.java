package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;

/**
 * Created by patrick on 2016/12/29.
 */

public interface ILoginData {
    void login(AuthRegisterUserInfo authRegisterUserInfo , OnLoginListener onLoginListener );
    interface OnLoginListener {
        void onSuccess(ResultInfo<AuthRegisterUserInfo> resultInfo);
        void onFailure(String e);
    }
}
