package com.wiatec.btv_launcher.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.service_task.CheckLogin;

public class CheckLoginService extends IntentService {


    public CheckLoginService() {
        super("CheckLoginService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Application.getThreadPool().execute(new CheckLogin());
    }

}
