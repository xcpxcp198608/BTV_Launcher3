package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.RxBus;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.bean.User1Info;
import com.wiatec.btv_launcher.rxevent.CheckLoginEvent;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by patrick on 2017/3/16.
 */

public class BaseActivity extends AppCompatActivity {

    protected Subscription checkLoginSubscription;
    protected boolean isLoginChecking = false;
    protected User1Info user1Info;
    protected int currentLoginCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user1Info = new User1Info();
        String mac = SystemConfig.getWifiMac();
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
        if(!isLoginChecking && SystemConfig.isNetworkConnected(Application.getContext())) {
            checkLoginResult();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(checkLoginSubscription != null){
            checkLoginSubscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(checkLoginSubscription != null){
            checkLoginSubscription.unsubscribe();
        }
    }

    protected void checkLoginResult(){
        checkLoginSubscription = RxBus.getDefault().toObservable(CheckLoginEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CheckLoginEvent>() {
                    @Override
                    public void call(CheckLoginEvent checkLoginEvent) {
                        if(isLoginChecking){
                            return;
                        }
                        if(checkLoginEvent.getCode() == CheckLoginEvent.CODE_LOGIN_NORMAL){
                            //Logger.d("login normal");
                        }else{
                            //Logger.d("login repeat");
                            showLoginAgainDialog();
                            checkLoginSubscription.unsubscribe();
                            isLoginChecking = true;
                        }
                    }
                });
    }

    private void showLoginAgainDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this , R.style.dialog).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if(window == null){
            return;
        }
        window.setContentView(R.layout.dialog_login_repeat);
        Button btnConfirm = (Button) window.findViewById(R.id.bt_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoginChecking = false;
                alertDialog.dismiss();
                startActivity(new Intent(BaseActivity.this , LoginActivity.class));
            }
        });
    }
}
