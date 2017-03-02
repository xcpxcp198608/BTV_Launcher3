package com.wiatec.btv_launcher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.service_task.DownloadAdVideo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PX on 2016-11-25.
 */

public class DownloadService extends Service {
    private ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Application.getThreadPool();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            String url = intent.getStringExtra("url");
            String name = intent.getStringExtra("name");
            executorService.execute(new DownloadAdVideo(url ,name));
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
