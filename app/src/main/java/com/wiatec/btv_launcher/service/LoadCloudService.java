package com.wiatec.btv_launcher.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.TokenInfo;
import com.wiatec.btv_launcher.service_task.LoadCloud;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PX on 2016-12-01.
 */

public class LoadCloudService extends IntentService {

    private ExecutorService executorService;

    public LoadCloudService() {
        super("LoadCloudService");
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        executorService.execute(new LoadCloud());
    }
}
