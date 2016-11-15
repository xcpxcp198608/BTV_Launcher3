package com.wiatec.btv_launcher.Utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ApkLaunch {

    //通过包名启动已经安装的apk
    public static void launchApkByPackageName (Context context ,String apkPackageName){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(apkPackageName);
        if(intent!= null){
            context.startActivity(intent);
        }
    }
}
