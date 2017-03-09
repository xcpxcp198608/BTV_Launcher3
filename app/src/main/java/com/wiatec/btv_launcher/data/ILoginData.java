package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.Result;

/**
 * Created by patrick on 2016/12/29.
 */

public interface ILoginData {
    void login(String userName ,String password ,OnLoginListener onLoginListener );
    interface OnLoginListener {
        void onSuccess(Result result);
        void onFailure(String e);
    }
}
