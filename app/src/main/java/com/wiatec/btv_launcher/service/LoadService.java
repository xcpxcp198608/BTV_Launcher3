package com.wiatec.btv_launcher.service;

import android.app.IntentService;
import android.content.Intent;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.service_task.LoadInstalledApp;
import com.wiatec.btv_launcher.service_task.LoadMessage;
import com.wiatec.btv_launcher.service_task.LoadWeather;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PX on 2016-11-14.
 */

public class LoadService extends IntentService {

    private ExecutorService executorService;

    public LoadService() {
        super("LoadService");
        executorService = Application.getThreadPool();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if("loadWeather".equals(intent.getAction())){
            executorService.execute(new LoadWeather());
        }
    }


}
