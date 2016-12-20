package com.wiatec.btv_launcher.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ApkInstall {

    /**
     * 后台静默安装已有的apk文件
     * 需要添加依赖：compile 'cn.trinea.android.common:trinea-android-common:4.2.15'
     * @param context
     * @param apkFileFullPath apk文件的完整路径（文件夹路径+文件完整名称-带后缀名字）
     * @return
     */
//    public static boolean silentInstallApk(Context context , String apkFileFullPath) {
//        int i = PackageUtils.installSilent(context , apkFileFullPath);
//        if(i == 1){
//            return true;
//        }else {
//            return false;
//        }
//    }
    //正常安装apk
    public static void installApk (Context context , String apkFilePath , String apkFileName){
        File file = new File(apkFilePath, apkFileName);
        if(file != null) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            context.startActivity(intent);
        }else{
            Toast.makeText(context , "Apk file is not exists" ,Toast.LENGTH_SHORT).show();
        }
    }
}
