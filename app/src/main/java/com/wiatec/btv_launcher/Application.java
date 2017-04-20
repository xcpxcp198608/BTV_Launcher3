package com.wiatec.btv_launcher;

import android.content.Context;
import android.content.Intent;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.exception.CrashHandler;
import com.wiatec.btv_launcher.service.CheckLoginService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PX on 2016-11-14.
 */

public class Application extends android.app.Application {

    private static RequestQueue requestQueue;
    private static Context context;
    private static boolean isFirstBoot;
    private static ExecutorService executorService;
    private CrashHandler crashHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("----px----");
        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        isFirstBoot = true;
        executorService = Executors.newCachedThreadPool();
        startLoginCheckService();

        crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
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
