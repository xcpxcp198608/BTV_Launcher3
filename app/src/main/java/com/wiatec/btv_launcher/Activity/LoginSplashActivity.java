package com.wiatec.btv_launcher.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.px.common.http.Bean.DownloadInfo;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.DownloadListener;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.AppUtil;
import com.px.common.utils.FileUtil;

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
        String userName = (String) SPUtil.get(F.sp.username ,"");
        String token = (String) SPUtil.get(F.sp.token ,"");
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(token)){
            startActivity(new Intent(this , LoginActivity.class));
        }else {
            check();
        }
    }

    private void check(){
        if(!AppUtil.isInstalled(packageName)){
            if(F.package_name.bplay.equals(packageName)){
                showLivePlayDownloadDialog();
            }else{
                Toast.makeText(CommonApplication.context, CommonApplication.context.getString(R.string.download_guide),
                        Toast.LENGTH_LONG).show();
                AppUtil.launchApp(this, F.package_name.market);
                finish();
            }
        }else{
            String l = (String) SPUtil.get(F.sp.level , "1");
            int level = Integer.parseInt(l);
            if(level >= 1) {
                AppUtil.launchApp(this, packageName);
                finish();
            } else{
                Toast.makeText(CommonApplication.context , CommonApplication.context.getString(R.string.account_error) ,
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
        HttpMaster.download(LoginSplashActivity.this).url(F.url.live_play).path(F.path.download).name(F.file_name.live_play)
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
                        if(AppUtil.isApkCanInstall(F.path.download, F.file_name.live_play )){
                            AppUtil.installApk(F.path.download, F.file_name.live_play, "");
                        }else{
                            if(FileUtil.isExists(F.path.download, F.file_name.live_play)){
                                FileUtil.delete(F.path.download, F.file_name.live_play);
                            }
                            Toast.makeText(CommonApplication.context, getString(R.string.update_error),Toast.LENGTH_LONG).show();
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
