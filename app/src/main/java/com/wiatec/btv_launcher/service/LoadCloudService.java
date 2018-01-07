package com.wiatec.btv_launcher.service;

import android.app.IntentService;
import android.content.Intent;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.service_task.LoadCloud;

import java.util.concurrent.ExecutorService;

/**
 * Created by PX on 2016-12-01.
 */

public class LoadCloudService extends IntentService {

    private ExecutorService executorService;

    public LoadCloudService() {
        super("LoadCloudService");
        executorService = Application.getThreadPool();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        executorService.execute(new LoadCloud());
    }
}
