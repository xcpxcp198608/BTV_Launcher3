package com.wiatec.btv_launcher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.wiatec.btv_launcher.Activity.LoginActivity;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.RxBus;
import com.wiatec.btv_launcher.rxevent.CheckLoginEvent;
import com.wiatec.btv_launcher.service.CheckLoginService;
import com.wiatec.btv_launcher.service.LoadCloudService;
import com.wiatec.btv_launcher.service.LoadWeatherService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by PX on 2016-11-14.
 */

public class Application extends android.app.Application {

    private static RequestQueue requestQueue;
    private static Context context;
    private static boolean isFirstBoot;
    private static ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("----px----");
        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        isFirstBoot = true;
        executorService = Executors.newCachedThreadPool();
        startLoginCheckService();
    }

    public static Context getContext (){
        return context;
    }

    public static RequestQueue getRequestQueue (){
        return requestQueue;
    }

    public static boolean getBootStatus(){
        return isFirstBoot;
    }

    public static void setBootStatus(boolean firstBoot){
        isFirstBoot = firstBoot;
    }

    public static ExecutorService getThreadPool (){
        return executorService;
    }

    public void startLoginCheckService(){
        startService(new Intent(context , CheckLoginService.class));
    }

}
