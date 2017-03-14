package com.wiatec.btv_launcher.service_task;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.RxBus;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.UserInfo;
import com.wiatec.btv_launcher.rxevent.CheckLoginEvent;

import java.io.IOException;

/**
 * Created by patrick on 2017/3/14.
 */

public class CheckLogin implements Runnable {

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            check();
        }
    }

    public void check(){
        int currentLoginCount = (int) SPUtils.get(Application.getContext() , "currentLoginCount" , 1);
        String userName = (String) SPUtils.get(Application.getContext(),"userName" , "");
        if(TextUtils.isEmpty(userName)){
            return;
        }
        OkMaster.get(F.url.login_repeat_check)
                .parames("count", currentLoginCount)
                .parames("userInfo.userName",userName)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s==null){
                            return;
                        }
                        Result result = new Gson().fromJson(s,new TypeToken<Result>(){}.getType());
                        if(result ==null){
                            return;
                        }
                        if(result.getCode() == Result.CODE_OK){
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_NORMAL));
                        }else{
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_REPEAT));
                        }

                    }

                    @Override
                    public void onFailure(String e) {

                    }
                });

    }
}
