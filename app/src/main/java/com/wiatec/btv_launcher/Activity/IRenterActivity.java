package com.wiatec.btv_launcher.Activity;

import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.ResultInfo;

/**
 * Created by patrick on 2016/12/29.
 */

public interface IRenterActivity {
    void onLogin(ResultInfo<AuthRentUserInfo> resultInfo);
}
