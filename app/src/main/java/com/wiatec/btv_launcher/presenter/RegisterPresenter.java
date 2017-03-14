package com.wiatec.btv_launcher.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Activity.IRegisterActivity;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.DeviceInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.UserInfo;

import java.io.IOException;

/**
 * Created by patrick on 2017/3/10.
 */

public class RegisterPresenter extends BasePresenter<IRegisterActivity> {

    private IRegisterActivity iRegisterActivity;

    public RegisterPresenter(IRegisterActivity iRegisterActivity) {
        this.iRegisterActivity = iRegisterActivity;
    }

    public void register (UserInfo userInfo , DeviceInfo deviceInfo){
        OkMaster.get(F.url.register)
                .parames("userInfo.userName",userInfo.getUserName())
                .parames("userInfo.password",userInfo.getPassword())
                .parames("userInfo.email",userInfo.getEmail())
                .parames("deviceInfo.mac", deviceInfo.getMac())
                .parames("deviceInfo.city", deviceInfo.getCity())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        Result result = new Gson().fromJson(s , new TypeToken<Result>(){}.getType());
                        iRegisterActivity.register(result);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
