package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.px.common.constant.CommonApplication;
import com.px.common.utils.NetUtil;
import com.px.common.utils.RxBus;
import com.px.common.utils.SPUtil;
import com.px.common.utils.SysUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.rxevent.CheckLoginEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BaseActivity extends AppCompatActivity {

    protected Disposable checkLoginDisposable;
    protected boolean isLoginChecking = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mac = SysUtil.getWifiMac();
        String ethernetMac = SysUtil.getEthernetMac();
        SPUtil.put(F.sp.mac, mac);
        SPUtil.put(F.sp.ethernet_mac, ethernetMac);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isLoginChecking && NetUtil.isConnected()) {
            checkLoginResult();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(checkLoginDisposable != null){
            checkLoginDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(checkLoginDisposable != null){
            checkLoginDisposable.dispose();
        }
    }

    protected void checkLoginResult(){
        checkLoginDisposable = RxBus.getDefault().subscribe(CheckLoginEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CheckLoginEvent>() {
                    @Override
                    public void accept(CheckLoginEvent checkLoginEvent) throws Exception {
                        if (isLoginChecking) {
                            return;
                        }
                        if (checkLoginEvent.getCode() == CheckLoginEvent.CODE_LOGIN_REPEAT) {
                            showLoginAgainDialog();
                            checkLoginDisposable.dispose();
                            isLoginChecking = true;
                        }
                    }
                });
    }

    protected void showLoginAgainDialog(){
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

    public int getLevel(){
        String l = (String) SPUtil.get(F.sp.level , "1");
        return Integer.parseInt(l);
    }

    public void showLimit() {
        Toast.makeText(CommonApplication.context, getString(R.string.account_error),
                Toast.LENGTH_LONG).show();
    }
}
