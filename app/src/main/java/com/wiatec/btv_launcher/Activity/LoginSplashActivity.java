package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.SPUtils;

/**
 * Created by patrick on 2017/3/31.
 */

public class LoginSplashActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userName = (String) SPUtils.get(Application.getContext() , "userName" ,"");
        if(TextUtils.isEmpty(userName)){
            startActivity(new Intent(this , LoginActivity.class));
        }else {
            if (ApkCheck.isApkInstalled(this, F.package_name.btv)) {
                ApkLaunch.launchApkByPackageName(this, F.package_name.btv);
            }
        }
        finish();
    }
}
