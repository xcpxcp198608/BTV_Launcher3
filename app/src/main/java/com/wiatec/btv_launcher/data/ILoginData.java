package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.DeviceInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.UserInfo;

/**
 * Created by patrick on 2016/12/29.
 */

public interface ILoginData {
    void login(UserInfo userInfo , DeviceInfo deviceInfo, OnLoginListener onLoginListener );
    interface OnLoginListener {
        void onSuccess(Result result);
        void onFailure(String e);
    }
}
