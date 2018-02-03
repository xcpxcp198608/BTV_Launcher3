package com.wiatec.btv_launcher.service_task;

import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.AppUtil;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.RxBus;
import com.px.common.utils.SPUtil;
import com.px.common.utils.TimeUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.constant.EnumLevel;
import com.wiatec.btv_launcher.rxevent.CheckLoginEvent;

import java.io.IOException;

public class CheckLogin implements Runnable {

    @Override
    public void run() {
        try {
            while(true) {
                Thread.sleep(20000);
                boolean isRenter = (boolean) SPUtil.get(F.sp.is_renter, false);
                if(isRenter){
                    rentValidate();
                }else {
                    check();
                }
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    private void rentValidate(){
        if(!NetUtil.isConnected()){
            return;
        }
        String key = (String) SPUtil.get(F.sp.username , "");
        String ethernetMac = (String) SPUtil.get(F.sp.ethernet_mac , "");
        if(TextUtils.isEmpty(key)){
            return;
        }
        HttpMaster.post(F.url.renter_validate + key + "/" + ethernetMac)
                .param("country", (String) SPUtil.get(F.sp.country,""))
                .param("region", (String) SPUtil.get(F.sp.region_name,""))
                .param("city", (String) SPUtil.get(F.sp.city,""))
                .param("timeZone", (String) SPUtil.get(F.sp.time_zone,""))
                .param("deviceModel", Build.MODEL)
                .param("romVersion", Build.DISPLAY)
                .param("uiVersion", AppUtil.getVersionName(CommonApplication.context.getPackageName()))
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
                        if(resultInfo.getCode() != 200 && resultInfo.getCode() != 500){
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_REPEAT));
                            SPUtil.put(F.sp.level, EnumLevel.L0.getL());
                            return;
                        }
                        AuthRentUserInfo authRentUserInfo = resultInfo.getData();
                        if(authRentUserInfo == null) return;
                        String level = "";
                        if(AuthRentUserInfo.STATUS_ACTIVATE.equals(authRentUserInfo.getStatus())){
                            if(AuthRentUserInfo.CATEGORY_B1.equals(authRentUserInfo.getCategory())){
                                level = EnumLevel.L2.getL();
                            }else if(AuthRentUserInfo.CATEGORY_P1.equals(authRentUserInfo.getCategory())){
                                level = EnumLevel.L4.getL();
                            }else if(AuthRentUserInfo.CATEGORY_P2.equals(authRentUserInfo.getCategory())){
                                level = EnumLevel.L4.getL();
                            }
                        }else{
                            level = EnumLevel.L0.getL();
                        }
                        SPUtil.put(F.sp.level, level);
                        SPUtil.put(F.sp.rental_category, authRentUserInfo.getCategory());
                        if(EnumLevel.L0.getL().equals(level)){
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_REPEAT));
                        }
                        String leftTime = TimeUtil.getLeftTimeToExpires(authRentUserInfo.getExpiresTime());
                        long leftMillsSeconds = TimeUtil.getUnixFromStr(authRentUserInfo.getExpiresTime()) - System.currentTimeMillis();
                        SPUtil.put(F.sp.left_time, leftTime);
                        SPUtil.put(F.sp.left_mills_second, leftMillsSeconds);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }

    private void check(){
        if(!NetUtil.isConnected()){
            return;
        }
        String userName = (String) SPUtil.get(F.sp.username , "");
        if(TextUtils.isEmpty(userName)){
            return;
        }
        String token = (String) SPUtil.get(F.sp.token , "");
        if(TextUtils.isEmpty(token)){
            return;
        }
        HttpMaster.post(F.url.user_validate)
                .param("username",userName)
                .param("country", (String) SPUtil.get(F.sp.country,""))
                .param("region", (String) SPUtil.get(F.sp.region_name,""))
                .param("city", (String) SPUtil.get(F.sp.city,""))
                .param("timeZone", (String) SPUtil.get(F.sp.time_zone,""))
                .param("deviceModel", Build.MODEL)
                .param("romVersion", Build.DISPLAY)
                .param("uiVersion", AppUtil.getVersionName(CommonApplication.context.getPackageName()))
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if (s == null) {
                            return;
                        }
//                        Logger.d(s);
                        ResultInfo<AuthRegisterUserInfo> resultInfo = null;
                        try {
                            resultInfo = new Gson().fromJson(s, new TypeToken<ResultInfo<AuthRegisterUserInfo>>() {}.getType());
                        }catch (Exception e){
                            Logger.e(e.getCause().getMessage());
                        }
                        if(resultInfo == null){
                            return;
                        }
                        if(resultInfo.getCode() != 200 && resultInfo.getCode() != 500){
                            RxBus.getDefault().post(new CheckLoginEvent(CheckLoginEvent.CODE_LOGIN_REPEAT));
                            SPUtil.put(F.sp.level, EnumLevel.L0.getL());
                            return;
                        }
                        AuthRegisterUserInfo authRegisterUserInfo = resultInfo.getData();
                        if(authRegisterUserInfo == null) {
                            return;
                        }
                        String l = authRegisterUserInfo.getLevel() + "";
                        SPUtil.put(F.sp.level, l);
                        SPUtil.put(F.sp.is_experience, authRegisterUserInfo.isExperience() + "");
                        if(EnumLevel.L0.getL().equals(l)){
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
