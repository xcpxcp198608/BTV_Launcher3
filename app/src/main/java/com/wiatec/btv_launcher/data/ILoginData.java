package com.wiatec.btv_launcher.data;

/**
 * Created by patrick on 2016/12/29.
 */

public interface ILoginData {
    void login(String key ,String password ,OnLoginListener onLoginListener );
    interface OnLoginListener {
        void onSuccess(String result);
        void onFailure(String e);
    }
}
