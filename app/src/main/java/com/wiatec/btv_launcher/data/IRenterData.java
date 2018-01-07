package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;

/**
 * Created by patrick on 2016/12/29.
 */

public interface IRenterData {
    void login(AuthRentUserInfo authRentUserInfo, OnLoginListener onLoginListener);
    interface OnLoginListener {
        void onSuccess(ResultInfo<AuthRentUserInfo> resultInfo);
        void onFailure(String e);
    }
}
