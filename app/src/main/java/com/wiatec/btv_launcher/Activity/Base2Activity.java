package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.bean.User1Info;
import com.wiatec.btv_launcher.presenter.BasePresenter;

/**
 * Created by PX on 2016-11-14.
 */

public abstract class Base2Activity <V ,T extends BasePresenter> extends AppCompatActivity {
    protected T presenter;
    protected User1Info user1Info;
    protected int currentLoginCount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView(this);

        user1Info = new User1Info();
        String mac = SystemConfig.getWifiMac1(this);
        String ethernetMac = SystemConfig.getEthernetMac();
        SPUtils.put(this, "mac", mac);
        SPUtils.put(this, "ethernetMac", ethernetMac);
        user1Info.setMac(mac);
        user1Info.setEthernetMac(ethernetMac);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentLoginCount = (int) SPUtils.get(this , "currentLoginCount" , 1);
        user1Info.setUserName((String) SPUtils.get(this , "userName" , " "));
        user1Info.setToken((String) SPUtils.get(this , "token" , " "));
        user1Info.setCity((String) SPUtils.get(this , "city" , "1"));
        user1Info.setCountry((String) SPUtils.get(this , "country" , "1"));
        user1Info.setRegion((String) SPUtils.get(this , "regionName" , "1"));
        user1Info.setTimeZone((String) SPUtils.get(this , "timeZone" , "1"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();

    }

    protected abstract T createPresenter();


}
