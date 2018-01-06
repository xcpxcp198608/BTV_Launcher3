package com.wiatec.btv_launcher.Activity;

import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.ResultInfo;

/**
 * Created by patrick on 2017/3/10.
 */

public interface IRegisterActivity {

    void register (ResultInfo<AuthRegisterUserInfo> resultInfo);
}
