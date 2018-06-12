package com.wiatec.btv_launcher.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.px.common.http.HttpMaster;
import com.px.common.http.listener.DownloadListener;
import com.px.common.http.pojo.DownloadInfo;
import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.FileUtil;
import com.wiatec.btv_launcher.bean.UpdateInfo;
import com.wiatec.btv_launcher.databinding.ActivityUpdateBinding;

import java.io.File;

public class UpdateActivity extends AppCompatActivity {

    private ActivityUpdateBinding binding;
    private UpdateInfo updateInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update);
        updateInfo = getIntent().getParcelableExtra("updateInfo");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(updateInfo == null){
            return;
        }
        String fileName = updateInfo.getFileName();
        if(FileUtil.isExists(F.path.download ,fileName)){
            if(AppUtil.isApkCanInstall(F.path.download , fileName)){
                int localFileCode = AppUtil.getApkVersionCode(F.path.download , fileName);
                boolean isNeedDownload = updateInfo.getCode() > localFileCode;
                if(isNeedDownload){
                    File file = new File(F.path.download , fileName);
                    file.delete();
                    download(updateInfo);
                }else {
                    AppUtil.installApk(F.path.download, fileName, "com.wiatec.btv_launcher.fileprovider");
                }
            }else{
                File file = new File(F.path.download , fileName);
                file.delete();
                download(updateInfo);
            }
        }else{
            download(updateInfo);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    private void download(final UpdateInfo updateInfo){
        HttpMaster.download(UpdateActivity.this)
                .url(updateInfo.getUrl())
                .name(updateInfo.getFileName())
                .path(F.path.download)
                .startDownload(new DownloadListener() {
                    @Override
                    public void onPending(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onStart(DownloadInfo downloadInfo) {
                        binding.progressUpdate.setProgress(downloadInfo.getProgress());
                        binding.tvProgress.setText(downloadInfo.getProgress()+"%");
                    }

                    @Override
                    public void onPause(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {
                        binding.progressUpdate.setProgress(downloadInfo.getProgress());
                        binding.tvProgress.setText(downloadInfo.getProgress()+"%");
                    }

                    @Override
                    public void onFinished(DownloadInfo downloadInfo) {
                        binding.progressUpdate.setProgress(downloadInfo.getProgress());
                        binding.tvProgress.setText(downloadInfo.getProgress()+"%");
                        if(AppUtil.isApkCanInstall(F.path.download , updateInfo.getFileName())) {
                            AppUtil.installApk(F.path.download, updateInfo.getFileName(),
                                    "com.wiatec.btv_launcher.fileprovider");
                        }else{
                            Toast.makeText(UpdateActivity.this ,getString(R.string.update_error) , Toast.LENGTH_LONG).show();
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
