package com.wiatec.btv_launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.Logger;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-11.
 */

public class AppInstallReceiver extends BroadcastReceiver {

    private InstalledAppDao installedAppDao;
    private Context mContext;

    public AppInstallReceiver() {
        mContext = Application.getContext();
        installedAppDao = InstalledAppDao.getInstance(mContext);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){
            //Logger.d("added");
            String packageName = intent.getData().getSchemeSpecificPart();
            Observable.just(packageName)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<String, Object>() {
                        @Override
                        public Object call(String s) {
                            if(!"com.wiatec.btv_launcher".equals(s) &&
                                    !"com.android.tv.settings".equals(s)&&
                                    !"com.euroandroid.xbox".equals(s)&&
                                    !"com.explusalpha.Snes9xPlus".equals(s)&&
                                    !"com.koushikdutta.superuser".equals(s)&&
                                    !"com.droidlogic.appinstall".equals(s)&&
                                    !F.package_name.market.equals(s)&&
                                    !F.package_name.legacy_antivirus.equals(s)&&
                                    !F.package_name.legacy_privacy.equals(s)&&
                                    !F.package_name.tvplus.equals(s)&&
                                    !F.package_name.bplay.equals(s)&&
                                    !F.package_name.live_net.equals(s)&&
                                    !F.package_name.show_box.equals(s)&&
                                    !F.package_name.tv_house.equals(s)&&
                                    !F.package_name.mx_player.equals(s)&&
                                    !F.package_name.btv.equals(s)){
                                InstalledApp installedApp = new InstalledApp();
                                installedApp.setAppPackageName(s);
                                installedApp.setAppName(ApkCheck.getInstalledApkName(mContext,s));
                                if(F.package_name.legacy_antivirus.equals(s)){
                                    installedApp.setSequence(1);
                                }else if (F.package_name.legacy_privacy.equals(s)){
                                    installedApp.setSequence(2);
                                }else if (F.package_name.cloud.equals(s)){
                                    installedApp.setSequence(3);
                                }else{
                                    installedApp.setSequence(30);
                                }
                                installedAppDao.insertOrUpdateData(installedApp,null);
                            }
                            return null;
                        }
                    })
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {

                        }
                    });
        }else if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
           // Logger.d("remove");
            String packageName = intent.getData().getSchemeSpecificPart();
            Observable.just(packageName)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<String, Object>() {
                        @Override
                        public Object call(String s) {
                            installedAppDao.deleteByPackageName(s);
                            return null;
                        }
                    })
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {

                        }
                    });

        }else if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){

        }
    }
}
