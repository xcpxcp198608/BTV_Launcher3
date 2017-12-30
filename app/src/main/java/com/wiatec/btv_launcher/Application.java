package com.wiatec.btv_launcher;

import android.content.Context;
import android.content.Intent;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.exception.CrashHandler;
import com.wiatec.btv_launcher.service.CheckLoginService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PX on 2016-11-14.
 */

public class Application extends android.app.Application {

    private static Context context;
    private static boolean isFirstBoot;
    private static ExecutorService executorService;
    private CrashHandler crashHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("----px----");
        context = getApplicationContext();
        isFirstBoot = true;
        executorService = Executors.newCachedThreadPool();
        startLoginCheckService();

        crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    public static Context getContext (){
        return context;
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
