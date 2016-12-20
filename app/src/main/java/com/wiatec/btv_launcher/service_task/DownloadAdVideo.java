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

    public DownloadAdVideo(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        DownloadManager downloadManager = DownloadManager.getInstance(Application.getContext());
        downloadManager.startDownload("btvad.mp4" ,url , F.path.download);
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
