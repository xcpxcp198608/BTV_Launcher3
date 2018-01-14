package com.px.common.utils;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.px.common.crash.CrashHandler;
import com.px.common.utils.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * common application
 */

public class CommonApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Logger.init("----px----");
        CrashHandler.getInstance().init();
//        ARouter.init(this);
        //LeakCanary 初始化
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
    }

}
