package com.wiatec.btv_launcher.service_task;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.RxBus;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.Utils.SystemConfig;
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
        if(!SystemConfig.isNetworkConnected(Application.getContext())){
            return;
        }
        int currentLoginCount = (int) SPUtils.get(Application.getContext() , "currentLoginCount" , 0);
        String userName = (String) SPUtils.get(Application.getContext(),"userName" , "");
        String mac = (String) SPUtils.get(Application.getContext() , "mac" , "");
        String ethernetMac = (String) SPUtils.get(Application.getContext() , "ethernetMac" , "");
        if(TextUtils.isEmpty(userName)){
           // Logger.d("no userName do not execute check");
            return;
        }
        if(currentLoginCount == 0){
            return;
        }
        OkMaster.post(F.url.login_repeat_check)
                .parames("count", currentLoginCount+"")
                .parames("userInfo.userName",userName)
                .parames("deviceInfo.mac", mac)
                .parames("deviceInfo.ethernetMac", ethernetMac)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s==null){
                            return;
                        }
//                        Logger.d(s);
                        Result result = new Gson().fromJson(s,new TypeToken<Result>(){}.getType());
                        if(result == null){
                            return;
                        }
//                        Logger.d(result.toString());
                        if(result.getCode() == Result.CODE_LOGIN_SUCCESS){
                            SPUtils.put(Application.getContext() , "userLevel" ,result.getUserLevel()+"");
                            SPUtils.put(Application.getContext(), "experience", result.getExtra());
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_NORMAL));
                        }else{
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_REPEAT));
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
