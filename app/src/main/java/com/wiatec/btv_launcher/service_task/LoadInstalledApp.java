package com.wiatec.btv_launcher.service_task;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class LoadInstalledApp implements Runnable {
    @Override
    public void run() {
        getInstalledApp();
    }

    private List<InstalledApp> getInstalledApp(){
        InstalledAppDao installedAppDao = InstalledAppDao.getInstance(Application.getContext());
        List<InstalledApp> list = null;
        PackageManager packageManager = Application.getContext().getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> localList = packageManager.queryIntentActivities(intent ,0);
        Iterator<ResolveInfo> iterator = null;
        if(localList != null) {
            iterator = localList.iterator();
            list = new ArrayList<>();
        }
//        installedAppDao.deleteAll();
        while (true) {
            if(!iterator.hasNext()){
                break;
            }
            ResolveInfo resolveInfo = iterator.next();
            InstalledApp installedApp = new InstalledApp();
            installedApp.setAppName(resolveInfo.loadLabel(packageManager).toString());
            installedApp.setAppPackageName(resolveInfo.activityInfo.packageName);
            installedApp.setLauncherName(resolveInfo.activityInfo.name);
            String packageName = resolveInfo.activityInfo.packageName;
            PackageInfo packageInfo;
            try {
                packageInfo = Application.getContext().getPackageManager().getPackageInfo(packageName,0);
                if((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM)>0){
                    installedApp.setSystemApp(true);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String noShowPackageName = installedApp.getAppPackageName();
            //通过包名过滤不需要显示的app
            if(!"com.wiatec.btv_launcher".equals(noShowPackageName) &&
                            !"com.android.tv.settings".equals(noShowPackageName)&&
                            !"com.euroandroid.xbox".equals(noShowPackageName)&&
                            !"com.explusalpha.Snes9xPlus".equals(noShowPackageName)&&
                            !"com.koushikdutta.superuser".equals(noShowPackageName)&&
                            !"com.droidlogic.appinstall".equals(noShowPackageName)&&
                            !"com.px.bmarket".equals(noShowPackageName)){
                // Logger.d(installedAppInfo.toString());
                if(!installedAppDao.isExists(installedApp)){
                    if(F.package_name.legacy_antivirus.equals(noShowPackageName)){
                        installedApp.setSequence(1);
                    }else if (F.package_name.legacy_privacy.equals(noShowPackageName)){
                        installedApp.setSequence(2);
                    }else if (F.package_name.cloud.equals(noShowPackageName)){
                        installedApp.setSequence(3);
                    }else{
                        installedApp.setSequence(30);
                    }
                    installedAppDao.insertOrUpdateData(installedApp,null);
                }
                list.add(installedApp);
            }
        }
        return list;
    }
}
