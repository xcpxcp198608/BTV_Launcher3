package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.px.common.http.Bean.DownloadInfo;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.DownloadListener;
import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.FileUtil;
import com.wiatec.btv_launcher.bean.UpdateInfo;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PX on 2016-11-14.
 */

public class UpdateActivity extends AppCompatActivity {
    @BindView(R.id.progress_update)
    ProgressBar pb_Update;
    @BindView(R.id.tv_progress)
    TextView tv_Progress;

    private UpdateInfo updateInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
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
                    AppUtil.installApk(F.path.download, fileName, "");
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                        pb_Update.setProgress(downloadInfo.getProgress());
                        tv_Progress.setText(downloadInfo.getProgress()+"%");
                    }

                    @Override
                    public void onPause(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {
                        pb_Update.setProgress(downloadInfo.getProgress());
                        tv_Progress.setText(downloadInfo.getProgress()+"%");
                    }

                    @Override
                    public void onFinished(DownloadInfo downloadInfo) {
                        pb_Update.setProgress(downloadInfo.getProgress());
                        tv_Progress.setText(downloadInfo.getProgress()+"%");
                        if(AppUtil.isApkCanInstall(F.path.download , updateInfo.getFileName())) {
                            AppUtil.installApk(F.path.download, updateInfo.getFileName(), "");
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
