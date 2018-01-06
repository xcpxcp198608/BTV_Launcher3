package com.wiatec.btv_launcher.Activity;

import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.ResultInfo;

/**
 * Created by patrick on 2016/12/29.
 */

public interface ILoginActivity {
    void login (ResultInfo<AuthRegisterUserInfo> resultInfo);
    void resetp (ResultInfo<AuthRegisterUserInfo> resultInfo);
}
