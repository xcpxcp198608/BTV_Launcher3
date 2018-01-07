package com.wiatec.btv_launcher.service_task;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.RxBus;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
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
            boolean isRenter = (boolean) SPUtil.get("isRenter", false);
            if(isRenter){
                rentValidate();
            }else {
                check();
            }
        }
    }

    private void rentValidate(){
        if(!NetUtil.isConnected()){
            return;
        }
        String key = (String) SPUtil.get("userName" , "");
        String ethernetMac = (String) SPUtil.get("ethernetMac" , "");
        if(TextUtils.isEmpty(key)){
            return;
        }
        HttpMaster.post(F.url.renter_validate + key + "/" + ethernetMac)
                .param("country", (String) SPUtil.get("country",""))
                .param("region", (String) SPUtil.get("regionName",""))
                .param("city", (String) SPUtil.get("city",""))
                .param("timeZone", (String) SPUtil.get("timeZone",""))
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
                            SPUtil.put("userLevel", "0");
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
                        SPUtil.put("userLevel", level);
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
        if(!NetUtil.isConnected()){
            return;
        }
        String userName = (String) SPUtil.get("userName" , "");
        if(TextUtils.isEmpty(userName)){
            return;
        }
        HttpMaster.post(F.url.user_validate)
                .param("username",userName)
                .param("country", (String) SPUtil.get("country",""))
                .param("region", (String) SPUtil.get("regionName",""))
                .param("city", (String) SPUtil.get("city",""))
                .param("timeZone", (String) SPUtil.get("timeZone",""))
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
                            SPUtil.put("userLevel", "0");
                            return;
                        }
                        AuthRegisterUserInfo authRegisterUserInfo = resultInfo.getData();
                        String l = authRegisterUserInfo.getLevel()+"";
                        SPUtil.put("userLevel", l);
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
