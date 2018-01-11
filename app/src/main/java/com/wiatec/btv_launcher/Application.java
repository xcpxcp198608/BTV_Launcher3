package com.wiatec.btv_launcher;

import android.content.Intent;

import com.px.common.utils.CommonApplication;
import com.wiatec.btv_launcher.service.CheckLoginService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Application extends CommonApplication {

    private static boolean isFirstBoot;
    private static ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        isFirstBoot = true;
        executorService = Executors.newCachedThreadPool();
        startLoginCheckService();
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
//        startService(new Intent(context , SocketService.class));
    }

}
