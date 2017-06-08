package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.User1Info;

/**
 * Created by patrick on 2016/12/29.
 */

public interface ILoginData {
    void login(User1Info user1Info , OnLoginListener onLoginListener );
    interface OnLoginListener {
        void onSuccess(Result result);
        void onFailure(String e);
    }
}
