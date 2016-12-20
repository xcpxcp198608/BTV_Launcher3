package com.wiatec.btv_launcher.Utils.ImageDownload;

/**
 * Created by PX on 2016/9/12.
 */
public interface OnDownloadListener {
    void onPending(DownloadInfo downloadInfo);
    void onStart(DownloadInfo downloadInfo, boolean isStart);
    void onProgressChange(DownloadInfo downloadInfo, int progress);
    void onPause(DownloadInfo downloadInfo);
    void onFinished(DownloadInfo downloadInfo, int progress);
    void onError(DownloadInfo downloadInfo, String e);
    void onRemove(DownloadInfo downloadInfo);
}
