package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkInstall;
import com.wiatec.btv_launcher.Utils.FileDownload.DownloadManager;
import com.wiatec.btv_launcher.Utils.FileDownload.OnDownloadListener;
import com.wiatec.btv_launcher.bean.UpdateInfo;

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
        if(updateInfo != null){
            DownloadManager downloadManager = DownloadManager.getInstance(getApplicationContext());
            downloadManager.startDownload(updateInfo.getFileName() ,updateInfo.getUrl() , F.path.download);
            downloadManager.setOnDownloadListener(new OnDownloadListener() {
                @Override
                public void onStart(int progress, boolean isStart) {
                    pb_Update.setProgress(progress);
                    tv_Progress.setText(progress+"%");
                }

                @Override
                public void onProgressChange(int progress) {
                    pb_Update.setProgress(progress);
                    tv_Progress.setText(progress+"%");
                }

                @Override
                public void onPause(int progress) {

                }

                @Override
                public void onCompleted(int progress) {
                    pb_Update.setProgress(progress);
                    tv_Progress.setText(progress+"%");
                    if(ApkCheck.isApkCanInstalled(UpdateActivity.this ,F.path.download , updateInfo.getFileName())) {
                        ApkInstall.installApk(UpdateActivity.this, F.path.download, updateInfo.getFileName());
                    }else{
                        Toast.makeText(UpdateActivity.this ,getString(R.string.update_error) , Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
