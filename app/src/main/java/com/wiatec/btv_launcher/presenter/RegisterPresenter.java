package com.wiatec.btv_launcher.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Activity.IRegisterActivity;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.Result;
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

    public void register (User1Info user1Info , String language){
        try {
            Logger.d(user1Info.toString());
            OkMaster.post(F.url.register)
                    .parames("user1Info.userName",user1Info.getUserName())
                    .parames("user1Info.firstName",user1Info.getFirstName())
                    .parames("user1Info.lastName",user1Info.getLastName())
                    .parames("user1Info.password",user1Info.getPassword())
                    .parames("user1Info.email",user1Info.getEmail())
                    .parames("user1Info.phone",user1Info.getPhone())
                    .parames("user1Info.mac",user1Info.getMac())
                    .parames("user1Info.ethernetMac", user1Info.getEthernetMac())
                    .parames("user1Info.country", user1Info.getCountry())
                    .parames("user1Info.region", user1Info.getRegion())
                    .parames("user1Info.city", user1Info.getCity())
                    .parames("user1Info.timeZone", user1Info.getTimeZone())
                    .parames("language", language)
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
                            Result result = new Result();
                            result.setCode(Result.CODE_REGISTER_FAILURE);
                            result.setStatus(Result.STATUS_REGISTER_FAILURE);
                            iRegisterActivity.register(result);
                            Logger.d(e);

                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
