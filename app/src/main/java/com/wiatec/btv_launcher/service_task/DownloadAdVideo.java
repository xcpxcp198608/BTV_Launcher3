package com.wiatec.btv_launcher.service_task;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.FileDownload.DownloadManager;
import com.wiatec.btv_launcher.Utils.FileDownload.OnDownloadListener;
import com.wiatec.btv_launcher.Utils.OkHttp.Bean.DownloadInfo;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.DownloadListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;

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
        OkMaster.download(Application.getContext())
                .path(F.path.download)
                .name(name)
                .url(url)
                .startDownload(new DownloadListener() {
                    @Override
                    public void onPending(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onStart(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onPause(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onFinished(DownloadInfo downloadInfo) {

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
