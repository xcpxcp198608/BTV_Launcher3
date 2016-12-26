package com.wiatec.btv_launcher.Utils.FileDownload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 依赖OkHttp
 * Created by PX on 2016/9/12.
 */
public class DownloadManager {

    private static final int MSG_START = 1;
    private static final int MSG_PAUSE = 2;
    private static final int MSG_PROGRESS = 3;
    private static final int MSG_COMPLETED = 4;

    private OnDownloadListener onDownloadListener;
    private Context context;
    private SQLiteDao sqLiteDao;
    private OkHttpClient okHttpClient;
    private boolean pauseDownload = false;
    private DownloadManager(Context context) {
        this.context = context;
        sqLiteDao = SQLiteDao.getInstance(context);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();

    }

    private volatile static DownloadManager instance;
    public static synchronized DownloadManager getInstance(Context context){
        if(instance ==null){
            synchronized (DownloadManager.class){
                if(instance ==null){
                    instance = new DownloadManager(context);
                }
            }
        }
        return instance;
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_START:
                    if(onDownloadListener!=null) {
                        onDownloadListener.onStart(0, true);
                    }
                    break;
                case MSG_PAUSE:
                    if(onDownloadListener!=null) {
                        onDownloadListener.onPause((Integer) msg.obj);
                    }
                    break;
                case MSG_PROGRESS:
                    if(onDownloadListener!=null) {
                        onDownloadListener.onProgressChange((Integer) msg.obj);
                    }
                    break;
                case MSG_COMPLETED:
                    if(onDownloadListener!=null) {
                        onDownloadListener.onCompleted(100);
                    }
                    break;
            }
        }
    };

    public void startDownload(String fileName ,String downloadUrl ,String filePath){
//        DownloadInfo downloadInfo =null ;
//        if(sqLiteDao.isExists(fileName)){
//            downloadInfo = sqLiteDao.queryDataByFileName(fileName);
//        }else{
//            downloadInfo = new DownloadInfo();
//            downloadInfo.setFileName(fileName);
//            downloadInfo.setDownloadUrl(downloadUrl);
//        }
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setFileName(fileName);
        downloadInfo.setDownloadUrl(downloadUrl);
        downloadTask(downloadInfo ,filePath);
    }

    public void pauseDownload(){
        pauseDownload = true;
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener){
        this.onDownloadListener = onDownloadListener;
    }

    public void downloadTask(final DownloadInfo downloadInfo, final String filePath){

        if(downloadInfo.getDownloadUrl() == null){
            return;
        }

        Request request = new Request.Builder().url(downloadInfo.getDownloadUrl()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    Log.d("----px----", e.getMessage());
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("----px----",downloadInfo.getFileName()+"start");
                long fileLength;
                int progress;
                long startLength = downloadInfo.getStartPosition() + downloadInfo.getCompletePosition();
                RandomAccessFile randomAccessFile = null;
                InputStream inputStream = null;
                pauseDownload=false;
                try {
                    if (response != null) {
                        fileLength = response.body().contentLength();
                        downloadInfo.setFileLength(fileLength);
                        downloadInfo.setStopPosition(fileLength);
                        File dir = new File(filePath);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        File file = new File(dir, downloadInfo.getFileName());
                        if(file.exists()){
                            long fileLenght = file.length();
                            if(fileLenght == downloadInfo.getFileLength()){
                                Log.d("----px----", downloadInfo.getFileName()+" is exists , do not need download");
                                return;
                            }
                        }
                        randomAccessFile = new RandomAccessFile(file, "rwd");
                        //randomAccessFile.setLength(downloadInfo.getFileLength());
                        randomAccessFile.seek(startLength);
                        handler.obtainMessage(MSG_START).sendToTarget();
                        inputStream = response.body().byteStream();
                        byte[] buffer = new byte[1024];
                        int length = -1;
                        long time = System.currentTimeMillis();
                        long completed = downloadInfo.getCompletePosition();
                        while ((length = inputStream.read(buffer)) != -1) {
                            randomAccessFile.write(buffer, 0, length);
                            completed += length;
                            if ((System.currentTimeMillis() - time) > 1000) {
                                time = System.currentTimeMillis();
                                progress = (int) (completed * 100L / downloadInfo.getFileLength());
                                handler.obtainMessage(MSG_PROGRESS, progress).sendToTarget();
                                Log.d("----px----",downloadInfo.getFileName()+"---"+progress);
                            }
                            if (pauseDownload) {
                                Log.d("----px----",downloadInfo.getFileName()+"pause");
                                downloadInfo.setCompletePosition(completed);
                                sqLiteDao.insertOrUpdateData(downloadInfo);
                                handler.obtainMessage(MSG_PAUSE, (int) (completed * 100L / downloadInfo.getFileLength())).sendToTarget();
                                return;
                            }
                        }
                        handler.obtainMessage(MSG_COMPLETED).sendToTarget();
                        Log.d("----px----",downloadInfo.getFileName()+"---"+"download completed");
                        if (sqLiteDao.isExists(downloadInfo.getFileName())) {
                            sqLiteDao.deleteDataByFileName(downloadInfo.getFileName());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(inputStream!= null){
                        inputStream.close();
                    }
                    if(randomAccessFile!= null){
                        randomAccessFile.close();
                    }
                }
            }
        });
    }
}
