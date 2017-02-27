package com.wiatec.btv_launcher.service_task;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.FileDownload.DownloadManager;
import com.wiatec.btv_launcher.Utils.FileDownload.OnDownloadListener;

/**
 * Created by PX on 2016-11-25.
 */

public class DownloadAdVideo implements Runnable {

    private String url;
    private String name;

    public DownloadAdVideo(String url ,String name) {
        this.url = url;
        this.name = name;
    }

    @Override
    public void run() {
        DownloadManager downloadManager = DownloadManager.getInstance(Application.getContext());
        downloadManager.startDownload(name ,url , F.path.download);
        downloadManager.setOnDownloadListener(new OnDownloadListener() {
            @Override
            public void onStart(int progress, boolean isStart) {

            }

            @Override
            public void onProgressChange(int progress) {

            }

            @Override
            public void onPause(int progress) {

            }

            @Override
            public void onCompleted(int progress) {

            }
        });
    }
}
