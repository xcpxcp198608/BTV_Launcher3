package com.wiatec.btv_launcher.service_task;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Activity.RenterActivity;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.RxBus;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.ResultInfo;
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
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isRenter = (boolean) SPUtils.get(Application.getContext(),"isRenter", false);
            if(isRenter){
                rentValidate();
            }else {
                check();
            }
        }
    }

    private void rentValidate(){
        if(!SystemConfig.isNetworkConnected(Application.getContext())){
            return;
        }
        String key = (String) SPUtils.get(Application.getContext(),"userName" , "");
        String ethernetMac = (String) SPUtils.get(Application.getContext() , "ethernetMac" , "");
        if(TextUtils.isEmpty(key)){
            return;
        }
        OkMaster.post(F.url.renter_validate + key + "/" + ethernetMac)
                .parames("country", (String) SPUtils.get(Application.getContext(), "country",""))
                .parames("region", (String) SPUtils.get(Application.getContext(), "regionName",""))
                .parames("city", (String) SPUtils.get(Application.getContext(), "city",""))
                .parames("timeZone", (String) SPUtils.get(Application.getContext(), "timeZone",""))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s==null){
                            return;
                        }
//                        Logger.d(s);
                        ResultInfo<AuthRentUserInfo> resultInfo = new Gson().fromJson(s,new TypeToken<ResultInfo<AuthRentUserInfo>>(){}.getType());
                        if(resultInfo == null){
                            return;
                        }
                        if(resultInfo.getCode() != 200){
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_REPEAT));
                            SPUtils.put(Application.getContext(), "userLevel", "0");
                            return;
                        }
                        AuthRentUserInfo authRentUserInfo = resultInfo.getData();
                        String level = "";
                        if("activate".equals(authRentUserInfo.getStatus())){
                            if("B1".equals(authRentUserInfo.getCategory())){
                                level = "2";
                            }else if("P1".equals(authRentUserInfo.getCategory())){
                                level = "4";
                            }else if("P2".equals(authRentUserInfo.getCategory())){
                                level = "4";
                            }
                        }else{
                            level = "0";
                        }
                        SPUtils.put(Application.getContext(), "userLevel", level);
                        if("0".equals(level)){
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_REPEAT));
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });

    }

    public void check(){
        if(!SystemConfig.isNetworkConnected(Application.getContext())){
            return;
        }
        String userName = (String) SPUtils.get(Application.getContext(),"userName" , "");
        if(TextUtils.isEmpty(userName)){
            return;
        }
        OkMaster.post(F.url.user_validate)
                .parames("username",userName)
                .parames("country", (String) SPUtils.get(Application.getContext(), "country",""))
                .parames("region", (String) SPUtils.get(Application.getContext(), "regionName",""))
                .parames("city", (String) SPUtils.get(Application.getContext(), "city",""))
                .parames("timeZone", (String) SPUtils.get(Application.getContext(), "timeZone",""))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s==null){
                            return;
                        }
                        Logger.d(s);
                        ResultInfo<AuthRegisterUserInfo> resultInfo = new Gson().fromJson(s,new TypeToken<ResultInfo<AuthRegisterUserInfo>>(){}.getType());
                        if(resultInfo == null){
                            return;
                        }
                        if(resultInfo.getCode() != 200){
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_REPEAT));
                            SPUtils.put(Application.getContext(), "userLevel", "0");
                            return;
                        }
                        AuthRegisterUserInfo authRegisterUserInfo = resultInfo.getData();
                        String l = authRegisterUserInfo.getLevel()+"";
                        SPUtils.put(Application.getContext(), "userLevel", l);
                        if("0".equals(l)){
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
