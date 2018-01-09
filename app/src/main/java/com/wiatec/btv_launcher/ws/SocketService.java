package com.wiatec.btv_launcher.ws;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.wiatec.btv_launcher.Application;

/**
 * Created by patrick on 08/01/2018.
 * create time : 2:41 PM
 */

public class SocketService extends IntentService {


    public SocketService() {
        super("SocketService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Application.getThreadPool().execute(new SocketTask());
    }
}
