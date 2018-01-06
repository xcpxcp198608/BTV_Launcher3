package com.wiatec.btv_launcher.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Activity.IRegisterActivity;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.bean.User1Info;

import java.io.IOException;

/**
 * Created by patrick on 2017/3/10.
 */

public class RegisterPresenter extends BasePresenter<IRegisterActivity> {

    private IRegisterActivity iRegisterActivity;

    public RegisterPresenter(IRegisterActivity iRegisterActivity) {
        this.iRegisterActivity = iRegisterActivity;
    }

    public void register (AuthRegisterUserInfo authRegisterUserInfo , String language){
        try {
            Logger.d(authRegisterUserInfo.toString());
            OkMaster.post(F.url.user_register)
                    .parames("username",authRegisterUserInfo.getUsername())
                    .parames("firstName",authRegisterUserInfo.getFirstName())
                    .parames("lastName",authRegisterUserInfo.getLastName())
                    .parames("password",authRegisterUserInfo.getPassword())
                    .parames("email",authRegisterUserInfo.getEmail())
                    .parames("phone",authRegisterUserInfo.getPhone())
                    .parames("mac",authRegisterUserInfo.getMac())
                    .parames("language", language)
                    .enqueue(new StringListener() {
                        @Override
                        public void onSuccess(String s) throws IOException {
                            if(s == null){
                                return;
                            }
                            ResultInfo<AuthRegisterUserInfo> resultInfo = new Gson().fromJson(s , new TypeToken<ResultInfo<AuthRegisterUserInfo>>(){}.getType());
                            iRegisterActivity.register(resultInfo);
                        }

                        @Override
                        public void onFailure(String e) {
                            ResultInfo<AuthRegisterUserInfo> resultInfo = new ResultInfo<>();
                            resultInfo.setCode(501);
                            resultInfo.setMessage("network communication error");
                            iRegisterActivity.register(resultInfo);
                            Logger.d(e);

                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
