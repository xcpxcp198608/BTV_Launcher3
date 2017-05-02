package com.wiatec.btv_launcher.Utils.OkHttp.Listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Bean.DownloadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by patrick on 2016/12/20.
 */

public class DownloadCallback implements Callback {

    public static final int STATUS_PENDING =0;
    public static final int STATUS_START =1;
    public static final int STATUS_PAUSE =2;
    public static final int STATUS_PROGRESS =3;
    public static final int STATUS_FINISHED =4;
    public static final int STATUS_CANCEL =5;
    public static final int STATUS_ERROR =6;

    private DownloadInfo downloadInfo;
    private DownloadListener downloadListener;

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
            if(downloadListener == null){
                return;
            }
            switch (msg.what){
                case STATUS_PENDING:
                    downloadListener.onPending(downloadInfo);
                    break;
                case STATUS_START:
                    downloadListener.onStart(downloadInfo);
                    break;
                case STATUS_PAUSE:
                    downloadListener.onPause(downloadInfo);
                    break;
                case STATUS_PROGRESS:
                    downloadListener.onProgress(downloadInfo);
                    break;
                case STATUS_FINISHED:
                    downloadListener.onFinished(downloadInfo);
                    break;
                case STATUS_CANCEL:
                    downloadListener.onCancel(downloadInfo);
                    break;
                case STATUS_ERROR:
                    downloadListener.onError(downloadInfo);
                    break;
                default:
                    break;
            }
        }
    };

    public DownloadCallback(DownloadInfo downloadInfo ,DownloadListener downloadListener){
        this.downloadInfo = downloadInfo;
        this.downloadListener = downloadListener;
        downloadInfo.setStatus(STATUS_PENDING);
        downloadInfo.setProgress(0);
        downloadInfo.setMessage("download is preparing ,please wait");
        handler.obtainMessage(STATUS_PENDING ,downloadInfo).sendToTarget();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        downloadInfo = new DownloadInfo();
        if(e != null){
            downloadInfo.setMessage(e.getMessage());
        }
        downloadInfo.setStatus(STATUS_ERROR);
        handler.obtainMessage(STATUS_ERROR ,downloadInfo).sendToTarget();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response == null){
            downloadInfo.setStatus(STATUS_ERROR);
            downloadInfo.setMessage("download response is empty");
            handler.obtainMessage(STATUS_ERROR ,downloadInfo).sendToTarget();
            return;
        }
        if(response.body().contentLength() <=0 ){
            downloadInfo.setStatus(STATUS_ERROR);
            downloadInfo.setMessage("file length read error");
            handler.obtainMessage(STATUS_ERROR ,downloadInfo).sendToTarget();
            return;
        }
        downloadInfo.setLength(response.body().contentLength());
        InputStream inputStream =null;
        RandomAccessFile randomAccessFile = null;
        try {
            File dir = new File(downloadInfo.getPath());
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, downloadInfo.getName());
            if(file.exists() && file.length() == downloadInfo.getLength()){
                downloadInfo.setStatus(STATUS_FINISHED);
                downloadInfo.setProgress(100);
                downloadInfo.setMessage("file is exists , no need download");
                handler.obtainMessage(STATUS_FINISHED , downloadInfo).sendToTarget();
                return;
            }
            randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(downloadInfo.getStartPosition());
            inputStream = response.body().byteStream();
            downloadInfo.setStatus(STATUS_START);
            downloadInfo.setMessage("download is start");
            handler.obtainMessage(STATUS_START, downloadInfo).sendToTarget();
            int length = -1;
            byte[] buffer = new byte[1024 * 1024];
            long finished = 0;
            long currentTime =System.currentTimeMillis();
            while ((length = inputStream.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, length);
                finished += length;
                if(System.currentTimeMillis() - currentTime > 2000) {
                    downloadInfo.setFinishedPosition(finished);
                    downloadInfo.setProgress((int) (finished * 100l / downloadInfo.getLength()));
                    downloadInfo.setStatus(STATUS_PROGRESS);
                    downloadInfo.setMessage("downloading");
                    handler.obtainMessage(STATUS_PROGRESS, downloadInfo).sendToTarget();
                   // Log.d("----px----",downloadInfo.getName() + "-->" + downloadInfo.getProgress());
                    currentTime = System.currentTimeMillis();
                }
            }
            if (file.length() == downloadInfo.getLength()) {
                downloadInfo.setProgress(100);
                downloadInfo.setStatus(STATUS_FINISHED);
                downloadInfo.setMessage("download was finished");
                handler.obtainMessage(STATUS_FINISHED, downloadInfo).sendToTarget();
            } else {
                downloadInfo.setStatus(STATUS_ERROR);
                downloadInfo.setMessage("file check error after download");
                handler.obtainMessage(STATUS_ERROR, downloadInfo).sendToTarget();
            }
        }catch (Exception e){
            e.printStackTrace();
            downloadInfo.setStatus(STATUS_ERROR);
            downloadInfo.setMessage(e.getMessage());
            handler.obtainMessage(STATUS_ERROR, downloadInfo).sendToTarget();
        }finally {
            try {
                if(randomAccessFile !=null) {
                    randomAccessFile.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
