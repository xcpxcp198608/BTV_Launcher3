package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.Result;

import java.io.IOException;

/**
 * Created by patrick on 2017/3/31.
 */

public class LoginSplashActivity extends AppCompatActivity{

    private String packageName ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        packageName = getIntent().getStringExtra("packageName");
        if(TextUtils.isEmpty(packageName)){
            packageName = F.package_name.btv;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userName = (String) SPUtils.get(Application.getContext() , "userName" ,"");
        String token = (String) SPUtils.get(Application.getContext() , "token" ,"");
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(token)){
            startActivity(new Intent(this , LoginActivity.class));
        }else {
            check(userName);
        }
    }

    private void check(String userName){
        OkMaster.post(F.url.level_check)
                .parames("userInfo.userName" , userName)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        Result result = new Gson().fromJson(s , new TypeToken<Result>(){}.getType());
                        if(result.getCode() == Result.CODE_REQUEST_SUCCESS){
                            if(result.getUserLevel() >= 3){
                                if (ApkCheck.isApkInstalled(LoginSplashActivity.this,packageName)) {
                                    ApkLaunch.launchApkByPackageName(LoginSplashActivity.this, packageName);
                                }
                                finish();
                            }else if(result.getUserLevel() >= 1){
                                if(packageName.equals(F.package_name.btv)) {
                                    Intent intent = new Intent(LoginSplashActivity.this, PlayAdActivity.class);
                                    intent.putExtra("packageName", F.package_name.btv);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    if (ApkCheck.isApkInstalled(LoginSplashActivity.this,packageName)) {
                                        ApkLaunch.launchApkByPackageName(LoginSplashActivity.this, packageName);
                                    }
                                    finish();
                                }
                            }else{
                                Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.account_error) ,
                                        Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(Application.getContext() , getString(R.string.account_error) ,
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
