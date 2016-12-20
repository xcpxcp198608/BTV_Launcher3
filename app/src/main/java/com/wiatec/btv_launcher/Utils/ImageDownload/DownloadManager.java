package com.wiatec.btv_launcher.Utils.ImageDownload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 依赖OkHttp
 * Created by PX on 2016/9/12.
 */
public class DownloadManager {
    public static final int DOWNLOAD_NONE = 0;
    public static final int DOWNLOAD_PENDING =1;
    public static final int DOWNLOAD_START =2;
    public static final int DOWNLOAD_PROGRESS =3;
    public static final int DOWNLOAD_PAUSE =4;
    public static final int DOWNLOAD_FINISHED =5;
    public static final int DOWNLOAD_ERROR =6;
    public static final int DOWNLOAD_REMOVE =7;
    //监听回调集合
    private Map<String ,OnDownloadListener> mListenerMap = new ConcurrentHashMap<>();
    //任务集合
    private Map<String ,DownloadTask> mTaskMap = new ConcurrentHashMap<>();
    //全局当前任务信息
    private DownloadInfo mDownloadInfo;
    private SQLiteDao sqLiteDao ;
    private Context context;
    private DownloadManager(Context context){
        sqLiteDao = SQLiteDao.getInstance(context);
        this.context = context;
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
            DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
            if(mListenerMap.containsKey(downloadInfo.getName())){
                OnDownloadListener onDownloadListener = mListenerMap.get(downloadInfo.getName());
                switch (downloadInfo.getStatus()){
                    case DOWNLOAD_START:
                        onDownloadListener.onStart(downloadInfo,true);
                        break;
                    case DOWNLOAD_PENDING:
                        onDownloadListener.onPending(downloadInfo);
                        break;
                    case DOWNLOAD_PROGRESS:
                        onDownloadListener.onProgressChange(downloadInfo ,downloadInfo.getProgress());
                        break;
                    case DOWNLOAD_PAUSE:
                        onDownloadListener.onPause(downloadInfo);
                        break;
                    case DOWNLOAD_FINISHED:
                        onDownloadListener.onFinished(downloadInfo,100);
                        break;
                    case DOWNLOAD_ERROR:
                        onDownloadListener.onError(downloadInfo,"download error");
                        break;
                    case DOWNLOAD_REMOVE:
                        onDownloadListener.onRemove(downloadInfo);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    //注册监听器
    public void setDownloadListener (String name , OnDownloadListener onDownloadListener){
        if(!mListenerMap.containsKey(name)){
            mListenerMap.put(name , onDownloadListener);
        }
    }
    //清除监听器
    public void clearListener (){
        mListenerMap.clear();
    }
    //执行下载
    public void startDownload (String name ,String url ,String path){
        DownloadInfo downloadInfo = new DownloadInfo();
        if(TextUtils.isEmpty(name)){
            name = url.split("/")[url.split("/").length-1];
            downloadInfo.setName(name);
        }else {
            downloadInfo.setName(name);
        }
        downloadInfo.setUrl(url);
        downloadInfo.setPath(path);
        if(sqLiteDao.isExists(downloadInfo)){
            downloadInfo = sqLiteDao.queryDataByName(name);
        }else {
            sqLiteDao.insertData(downloadInfo);
        }
        if(downloadInfo.getStatus() == DOWNLOAD_NONE || downloadInfo.getStatus() == DOWNLOAD_PAUSE ||
                downloadInfo.getStatus() == DOWNLOAD_REMOVE || downloadInfo.getStatus() == DOWNLOAD_ERROR){
            downloadInfo.setStatus(DOWNLOAD_PENDING);
            sqLiteDao.updateData(downloadInfo);
            notifyDownloadStatusChange(downloadInfo);
            DownloadTask downloadTask = new DownloadTask(downloadInfo);
            mTaskMap.put(downloadInfo.getName() , downloadTask);
            DownloadExecutors.execute(downloadTask);
        }else if(downloadInfo.getStatus() == DOWNLOAD_START || downloadInfo.getStatus() == DOWNLOAD_PROGRESS ||
                downloadInfo.getStatus() == DOWNLOAD_PENDING){
            if(mTaskMap.containsKey(downloadInfo.getName())){
                DownloadTask downloadTask = mTaskMap.get(downloadInfo.getName());
                downloadTask.downloadInfo.setStatus(DOWNLOAD_PAUSE);
                sqLiteDao.updateData(downloadInfo);
                if(DownloadExecutors.cancel(downloadTask)){
                    mListenerMap.get(downloadInfo.getName()).onPause(downloadInfo);
                }
            }
        }
    }
    //移除当前下载任务
    public void removeCurrentTask(DownloadInfo downloadInfo){
        if(mTaskMap.containsKey(downloadInfo.getName())){
            DownloadTask downloadTask = mTaskMap.get(downloadInfo.getName());
            downloadTask.downloadInfo.setStatus(DOWNLOAD_REMOVE);
            downloadInfo.setStatus(DOWNLOAD_REMOVE);
            notifyDownloadStatusChange(downloadInfo);
            sqLiteDao.deleteDataByName(downloadInfo.getName());
            File file = new File(downloadInfo.getPath()+downloadInfo.getName());
            if(file.exists()){
                file.delete();
            }
            file = null;
        }
    }
    //下载状态改变时回调
    private void notifyDownloadStatusChange(DownloadInfo downloadInfo){
        Message message = handler.obtainMessage();
        message.obj = downloadInfo;
        handler.sendMessage(message);
    }
    //销毁，关闭线程池和当前执行的线程，清空数据，保存当前下载状态进数据库
    public void destory (){
        DownloadExecutors.stop();
        mListenerMap.clear();
        mTaskMap.clear();
        if(mDownloadInfo != null){
            mDownloadInfo.setStatus(DOWNLOAD_PAUSE);
            sqLiteDao.updateData(mDownloadInfo);
        }
    }
    //实际下载
    public class DownloadTask implements Runnable {
        public DownloadInfo downloadInfo;

        public DownloadTask (DownloadInfo downloadInfo){
            this.downloadInfo = downloadInfo;
        }
        @Override
        public void run() {
            Log.d("----px----" ,"run start");
            if(downloadInfo.getStatus() == DOWNLOAD_PAUSE){
                downloadInfo.setStatus(DOWNLOAD_PAUSE);
                sqLiteDao.updateData(downloadInfo);
                return;
            }else if (downloadInfo.getStatus() == DOWNLOAD_REMOVE){
                downloadInfo.setStatus(DOWNLOAD_REMOVE);
                notifyDownloadStatusChange(downloadInfo);
                mTaskMap.remove(downloadInfo.getName());
                return;
            }
            long finishedSize = 0;
            finishedSize = downloadInfo.getFinishedPosition();
            File dir = new File (downloadInfo.getPath());
            if(!dir.exists()){
                dir.mkdir();
            }
            File file = new File (dir ,downloadInfo.getName());
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            RandomAccessFile randomAccessFile = null;
            try {
                URL url = new URL(downloadInfo.getUrl());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(30*1000);
                httpURLConnection.setRequestMethod("GET");
                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    long netLength = httpURLConnection.getContentLength();
                    Log.d("----px----" ,"http ok" + netLength);
                    if(netLength <= 0){
                        return;
                    }
                    downloadInfo.setLength(netLength);
                    if(file.exists()){
                        long fileLength = file.length();
                        if(fileLength == downloadInfo.getLength()){
                            Log.d("----px----", downloadInfo.getName()+" is exists , do not need download");
                            return;
                        }
                    }
                    downloadInfo.setStatus(DOWNLOAD_START);
                    sqLiteDao.updateData(downloadInfo);
                    notifyDownloadStatusChange(downloadInfo);
                    randomAccessFile = new RandomAccessFile(file ,"rwd");
                    randomAccessFile.setLength(downloadInfo.getLength());
                    randomAccessFile.seek(finishedSize);
                    inputStream = httpURLConnection.getInputStream();
                    byte[] buffer = new byte[1024*1024];
                    int length = -1;
                    while ((length = inputStream.read(buffer))!= -1){
                        if(downloadInfo.getStatus() == DOWNLOAD_PAUSE){
                            downloadInfo.setStatus(DOWNLOAD_PAUSE);
                            sqLiteDao.updateData(downloadInfo);
                            notifyDownloadStatusChange(downloadInfo);
                            return;
                        }else if (downloadInfo.getStatus() == DOWNLOAD_REMOVE){
                            downloadInfo.setStatus(DOWNLOAD_REMOVE);
                            sqLiteDao.updateData(downloadInfo);
                            notifyDownloadStatusChange(downloadInfo);
                            mTaskMap.remove(downloadInfo.getName());
                            return;
                        }
                        mDownloadInfo = downloadInfo;
                        randomAccessFile.write(buffer ,0 , length);
                        finishedSize += length;
                        downloadInfo.setFinishedPosition(finishedSize);
                        int progress = (int) (finishedSize*100L / downloadInfo.getLength());
                        downloadInfo.setStatus(DOWNLOAD_PROGRESS);
                        downloadInfo.setProgress(progress);
                        notifyDownloadStatusChange(downloadInfo);
                    }
                    if(downloadInfo.getFinishedPosition() == downloadInfo.getLength()){
                        downloadInfo.setStatus(DOWNLOAD_FINISHED);
                        notifyDownloadStatusChange(downloadInfo);
                        sqLiteDao.deleteDataByName(downloadInfo.getName());
                    }else {
                        Log.d("----px----" ,"error");
                        downloadInfo.setStatus(DOWNLOAD_ERROR);
                        sqLiteDao.updateData(downloadInfo);
                        notifyDownloadStatusChange(downloadInfo);
                        downloadInfo.setFinishedPosition(0);
                        file.delete();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("----px----" ,e.getMessage());
                downloadInfo.setStatus(DOWNLOAD_ERROR);
                sqLiteDao.updateData(downloadInfo);
                notifyDownloadStatusChange(downloadInfo);
                downloadInfo.setFinishedPosition(0);
                file.delete();
            }finally {
                mTaskMap.remove(downloadInfo.getName());
                try {
                    if(randomAccessFile!= null) {
                        randomAccessFile.close();
                    }
                    if(inputStream!= null){
                        inputStream.close();
                    }
                    if(httpURLConnection!=null){
                        httpURLConnection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
