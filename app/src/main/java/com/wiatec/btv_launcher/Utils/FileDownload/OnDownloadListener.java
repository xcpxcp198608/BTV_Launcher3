package com.wiatec.btv_launcher.Utils.FileDownload;

/**
 * Created by PX on 2016/9/12.
 */
public interface OnDownloadListener {

    void onStart(int progress, boolean isStart);
    void onProgressChange(int progress);
    void onPause(int progress);
    void onCompleted(int progress);
}
