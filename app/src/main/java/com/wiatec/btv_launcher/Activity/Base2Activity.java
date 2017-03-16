package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.RxBus;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.bean.DeviceInfo;
import com.wiatec.btv_launcher.bean.UserInfo;
import com.wiatec.btv_launcher.presenter.BasePresenter;
import com.wiatec.btv_launcher.rxevent.CheckLoginEvent;
import com.wiatec.btv_launcher.service_task.CheckLogin;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by PX on 2016-11-14.
 */

public abstract class Base2Activity <V ,T extends BasePresenter> extends AppCompatActivity {
    protected T presenter;
    protected UserInfo userInfo;
    protected DeviceInfo deviceInfo;
    protected int currentLoginCount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView(this);
        userInfo = new UserInfo();
        deviceInfo = new DeviceInfo();
        String mac = SystemConfig.getWifiMac1(this);
        SPUtils.put(this ,"mac",mac);
        deviceInfo.setMac(mac);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentLoginCount = (int) SPUtils.get(this , "currentLoginCount" , 1);
        userInfo.setUserName((String) SPUtils.get(this , "userName" , " "));
        userInfo.setToken((String) SPUtils.get(this , "token" , " "));
        deviceInfo.setUserName((String) SPUtils.get(this , "userName" , " "));
        deviceInfo.setCity((String) SPUtils.get(this , "city" , "1"));
        deviceInfo.setCountry((String) SPUtils.get(this , "country" , "1"));
        deviceInfo.setCountryCode((String) SPUtils.get(this , "countryCode" , "1"));
        deviceInfo.setRegionName((String) SPUtils.get(this , "regionName" , "1"));
        deviceInfo.setTimeZone((String) SPUtils.get(this , "timezone" , "1"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();

    }

    protected abstract T createPresenter();


}
