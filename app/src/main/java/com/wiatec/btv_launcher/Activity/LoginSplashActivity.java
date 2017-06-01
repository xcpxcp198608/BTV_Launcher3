package com.wiatec.btv_launcher.Activity;

import android.app.ProgressDialog;
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
import com.wiatec.btv_launcher.Utils.ApkInstall;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.FileCheck;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Bean.DownloadInfo;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.DownloadListener;
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
            packageName = F.package_name.bplay;
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
            check();
        }
    }

    private void check(){
        if(!ApkCheck.isApkInstalled(this, packageName)){
            if(F.package_name.bplay.equals(packageName)){
                showLivePlayDownloadDialog();
            }else{
                Toast.makeText(Application.getContext(), Application.getContext().getString(R.string.download_guide),
                        Toast.LENGTH_LONG).show();
                ApkLaunch.launchApkByPackageName(this, F.package_name.market);
                finish();
            }
        }else{
            String l = (String) SPUtils.get(Application.getContext() , "userLevel" , "1");
            int level = Integer.parseInt(l);
            if(level >= 3 ){
                ApkLaunch.launchApkByPackageName(this, packageName);
                finish();
            }else if(level >= 1){
                if(packageName.equals(F.package_name.bplay)) {
                    Intent intent = new Intent(this, PlayAdActivity.class);
                    intent.putExtra("packageName", F.package_name.bplay);
                    startActivity(intent);
                    finish();
                }else{
                    ApkLaunch.launchApkByPackageName(this, packageName);
                    finish();
                }
            }else{
                Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.account_error) ,
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void showLivePlayDownloadDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginSplashActivity.this);
        progressDialog.setTitle(getString(R.string.download_add));
        progressDialog.setMessage(getString(R.string.download_wait));
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        OkMaster.download(LoginSplashActivity.this).url(F.url.live_play).path(F.path.download).name(F.file_name.live_play)
                .startDownload(new DownloadListener() {
                    @Override
                    public void onPending(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onStart(DownloadInfo downloadInfo) {
                        progressDialog.setProgress(downloadInfo.getProgress());
                    }

                    @Override
                    public void onPause(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {
                        progressDialog.setProgress(downloadInfo.getProgress());
                    }

                    @Override
                    public void onFinished(DownloadInfo downloadInfo) {
                        progressDialog.setProgress(100);
                        progressDialog.dismiss();
                        if(ApkCheck.isApkCanInstalled(LoginSplashActivity.this, F.path.download, F.file_name.live_play )){
                            ApkInstall.installApk(LoginSplashActivity.this, F.path.download, F.file_name.live_play);
                        }else{
                            if(FileCheck.isFileExists(F.path.download, F.file_name.live_play)){
                                FileCheck.delete(F.path.download, F.file_name.live_play);
                            }
                            Toast.makeText(Application.getContext(),getString(R.string.update_error),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancel(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onError(DownloadInfo downloadInfo) {

                    }
                });
    }
}
