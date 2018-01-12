package com.wiatec.btv_launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.px.common.utils.AppUtil;
import com.px.common.utils.CommonApplication;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.sql.InstalledAppDao;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AppInstallReceiver extends BroadcastReceiver {

    private InstalledAppDao installedAppDao;
    private Context mContext;

    public AppInstallReceiver() {
        mContext = CommonApplication.context;
        installedAppDao = InstalledAppDao.getInstance(mContext);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){
            //Logger.d("added");
            String packageName = intent.getData().getSchemeSpecificPart();
            Observable.just(packageName)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<String, Object>() {
                        @Override
                        public Object apply(String s) {
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
                                    !F.package_name.terrarium_tv.equals(s)&&
                                    !F.package_name.popcom.equals(s)&&
                                    !F.package_name.lddream.equals(s)&&
                                    !F.package_name.ldservice.equals(s)&&
                                    !F.package_name.btv.equals(s)){
                                InstalledApp installedApp = new InstalledApp();
                                installedApp.setAppPackageName(s);
                                installedApp.setAppName(AppUtil.getLabelName(s));
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
                            return "";
                        }
                    })
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) {

                        }
                    });
        }else if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
           // Logger.d("remove");
            String packageName = intent.getData().getSchemeSpecificPart();
            Observable.just(packageName)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) {
                            installedAppDao.deleteByPackageName(s);
                            return "";
                        }
                    })
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) {

                        }
                    });

        }else if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){

        }
    }
}
