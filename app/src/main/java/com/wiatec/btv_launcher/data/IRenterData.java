package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.bean.User1Info;

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
