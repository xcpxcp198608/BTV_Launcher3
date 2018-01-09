package com.wiatec.btv_launcher.service;

import android.app.IntentService;
import android.content.Intent;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.service_task.LoadWeather;

import java.util.concurrent.ExecutorService;

public class LoadWeatherService extends IntentService {

    private ExecutorService executorService;

    public LoadWeatherService() {
        super("LoadWeatherService");
        executorService = Application.getThreadPool();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if("loadWeather".equals(intent.getAction())){
            executorService.execute(new LoadWeather());
        }
    }

}
