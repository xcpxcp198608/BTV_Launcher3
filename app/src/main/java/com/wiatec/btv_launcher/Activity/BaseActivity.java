package com.wiatec.btv_launcher.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.bean.DeviceInfo;
import com.wiatec.btv_launcher.bean.UserInfo;
import com.wiatec.btv_launcher.presenter.BasePresenter;

/**
 * Created by PX on 2016-11-14.
 */

public abstract class BaseActivity <V ,T extends BasePresenter> extends AppCompatActivity {
    protected T presenter;
    protected int currentLoginCount;
    protected UserInfo userInfo;
    protected DeviceInfo deviceInfo;

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
